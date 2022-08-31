package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.dao.JobInstanceDao;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import io.github.melin.spark.jobserver.core.entity.JobInstanceDependent;
import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import com.google.common.collect.Maps;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.*;
import static io.github.melin.spark.jobserver.core.enums.InstanceType.API;
import static io.github.melin.spark.jobserver.core.enums.InstanceType.SCHEDULE;

@Service
@Transactional
public class JobInstanceService extends BaseServiceImpl<JobInstance, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(JobInstanceService.class);

    @Autowired
    private JobInstanceDao jobInstanceDao;

    @Autowired
    private JobInstanceDependentService dependentService;

    @Autowired
    private JobInstanceContentService instanceContentService;

    @Override
    public HibernateBaseDao<JobInstance, Long> getHibernateBaseDao() {
        return jobInstanceDao;
    }

    /**
     * 保存作业内容
     */
    public void saveJobText(String instanceCode, String jobText, String jobConfig) {
        JobInstanceContent instanceContent = new JobInstanceContent(instanceCode, jobText, jobConfig);
        instanceContentService.insertEntity(instanceContent);
    }

    public void startJobInstance(String instanceCode, String applicationId) {
        JobInstance jobInstance = queryJobInstanceByCode(instanceCode);
        jobInstance.setGmtModified(Instant.now());
        jobInstance.setStartTime(Instant.now());
        jobInstance.setEndTime(null);
        jobInstance.setApplicationId(applicationId);
        jobInstance.setStatus(InstanceStatus.RUNNING);
        updateEntity(jobInstance);

        instanceContentService.updateErrorMsg(instanceCode, null);
        LOG.info("update task {} status running", jobInstance.getCode());
    }

    @Transactional
    public void instanceRunEnd(String instanceCode, InstanceStatus status, String errorMsg) {
        JobInstance instance = queryJobInstanceByCode(instanceCode);
        Instant nowDate = Instant.now();
        instance.setEndTime(nowDate);
        long times = nowDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                instance.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        instance.setRunTimes(times);
        if (FAILED == status) {
            instance.setRetryCount(instance.getRetryCount() + 1);
        }
        instance.setStatus(status);
        instance.setGmtModified(Instant.now());
        this.updateEntity(instance);
    }

    /**
     * 保存作业实例依赖信息
     */
    public void saveInstanceDependent(String instanceCode, String[] dependentCodes) {
        Arrays.stream(dependentCodes).forEach(dependentCode ->
                dependentService.insertEntity(new JobInstanceDependent(instanceCode, dependentCode)));
    }

    /**
     * 实例 Code 查询 Job 实例
     */
    @Transactional(readOnly = true)
    public JobInstance queryJobInstanceByCode(String instanceCode) {
        return queryByNamedParam("code", instanceCode);
    }

    /**
     * 乐观锁 控制实例同时被提交一次
     */
    public boolean lockInstance(String instanceCode) {
        int updateCount = jobInstanceDao.deleteOrUpdateByHQL("update JobInstance set status = :status " +
                        "where code = :code and status in (:status1, :status2)",
                new String[]{"status", "code", "status1", "status2"},
                LOCKED, instanceCode, WAITING, SUBMITTING);

        return updateCount > 0;
    }

    /**
     * 乐观锁 控制实例同时被提交一次
     */
    public void unLockInstance(String instanceCode) {
        int updateCount = jobInstanceDao.deleteOrUpdateByHQL("update JobInstance set status = :status " +
                        "where code = :code and status = :status1",
                new String[]{"status", "code", "status1"}, WAITING, instanceCode, LOCKED);

        if (updateCount > 0) {
            LOG.info("instance: {} lock release success", instanceCode);
        }
    }

    /**
     * 查询可以运行的调度实例ID
     * @return 调度实例
     */
    @Transactional(readOnly = true)
    public List<Long> findScheduleInstances() {
        Instant lastDay = Instant.now().minus(7, ChronoUnit.DAYS);
        Criterion lastScheduleTimeCrt = Restrictions.gt("scheduleTime", lastDay); //最近7天实例
        Criterion scheduleTimeCrt = Restrictions.le("scheduleTime", Instant.now());

        Criterion newStatusCrt = Restrictions.eq("status", WAITING);
        Criterion failureStatusCrt = Restrictions.and(Restrictions.eq("status", FAILED),
                Restrictions.gt("retryCount", 0),
                Restrictions.ltProperty("failureCount", "retryCount"));
        Criterion statusCrt = Restrictions.or(newStatusCrt, failureStatusCrt);

        Criterion instanceTypeCrt = Restrictions.in("instanceType", SCHEDULE, API);

        return this.findByCriterion(Projections.property("id"),
                Order.asc("gmtModified"),
                lastScheduleTimeCrt, scheduleTimeCrt, instanceTypeCrt, statusCrt);
    }

    /**
     * 查找当前实例的上游实例，没有执行完成实例数量。
     * @return 没有执行完成实例数量
     */
    @Transactional(readOnly = true)
    public long findParentInstanceNotSuccessCount(String instanceCode) {
        Criterion codeCrt = Restrictions.eq("code", instanceCode);
        Projection projection = Projections.property("parentCode");
        List<String> parentCodes = dependentService.findByCriterion(projection, codeCrt);

        Criterion parentCodesCrt = Restrictions.in("code", parentCodes);
        Criterion statusCrt = Restrictions.ne("status", FINISHED);
        projection = Projections.count("code");
        List<Long> counts = this.findByCriterion(projection, parentCodesCrt, statusCrt);
        return counts.get(0);
    }

    /**
     * 查询作业实例内容
     * @param code 作业实例Code
     * @return 作业实例内容
     */
    @Transactional(readOnly = true)
    public JobInstanceContent queryJobTextByCode(String code) {
        return instanceContentService.queryByCriterions(Restrictions.eq("code", code));
    }

    /**
     * 查询appid 对应运行状态 Instance
     */
    @Transactional(readOnly = true)
    public JobInstance queryInstanceByAppId(String appId) {
        return queryByCriterions(Restrictions.eq("applicationId", appId),
                Restrictions.eq("status", InstanceStatus.RUNNING));
    }

    @Transactional
    public JobInstance updateJobStatusByCode(String instanceCode, InstanceStatus status) {
        JobInstance instance = this.queryJobInstanceByCode(instanceCode);
        instance.setStatus(status);
        instance.setGmtModified(Instant.now());
        this.updateEntity(instance);
        return instance;
    }

    @Transactional(readOnly = true)
    public List<JobInstance> findJobInstanceSubmitTimeOut(Instant date) {
        Criterion gmtModifiedCrt = Restrictions.lt("gmtModified", date);
        Criterion statusCrt = Restrictions.in("status", SUBMITTING, LOCKED);
        return findByCriterions(statusCrt, gmtModifiedCrt);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> queryInstanceStatistics() {
        long runningCount = this.queryCount("status", InstanceStatus.RUNNING);
        long waitingCount = this.queryCount("status", InstanceStatus.WAITING);

        Instant lastDay = Instant.now().minus(1, ChronoUnit.DAYS);
        Criterion lastCreateTimeCrt = Restrictions.gt("gmtCreated", lastDay); //最近1天实例

        long lastDayStoppedCount = this.queryCount("status", InstanceStatus.KILLED, "gmtCreated", lastCreateTimeCrt);
        long lastDayFailedCount = this.queryCount("status", InstanceStatus.FAILED, "gmtCreated", lastCreateTimeCrt);
        long lastDayFinishedCount = this.queryCount("status", InstanceStatus.FINISHED, "gmtCreated", lastCreateTimeCrt);

        Map<String, Long> data = Maps.newHashMap();
        data.put("runningCount", runningCount);
        data.put("waitingCount", waitingCount);
        data.put("lastDayStoppedCount", lastDayStoppedCount);
        data.put("lastDayFailedCount", lastDayFailedCount);
        data.put("lastDayFinishedCount", lastDayFinishedCount);

        return data;
    }
}
