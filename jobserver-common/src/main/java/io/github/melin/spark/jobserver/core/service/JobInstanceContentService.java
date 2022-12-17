package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.dao.JobInstanceContentDao;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobInstanceContentService extends BaseServiceImpl<JobInstanceContent, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(JobInstanceContentService.class);

    @Autowired
    private JobInstanceContentDao jobInstanceContentDao;

    @Override
    public HibernateBaseDao<JobInstanceContent, Long> getHibernateBaseDao() {
        return jobInstanceContentDao;
    }

    /**
     * 实例 Code 查询 Job 实例
     * @param instanceCode
     * @return
     */
    @Transactional(readOnly = true)
    public JobInstanceContent queryJobInstanceContentByCode(String instanceCode) {
        return queryByNamedParam("code", instanceCode);
    }

    /**
     * 更新统计信息
     * @param instanceCode
     * @param statData
     * @return
     */
    public void updateStatistics(String instanceCode, String statData) {
        String hql = "update JobInstanceContent set statistics = :statistics where code = :code";
        this.deleteOrUpdateByHQL(hql, "statistics", statData, "code", instanceCode);
    }

    public void updateErrorMsg(String instanceCode, String errorMsg) {
        if (errorMsg == null) {
            String hql = "update JobInstanceContent set errorMsg = null where code = :code";
            this.deleteOrUpdateByHQL(hql, "code", instanceCode);
        } else {
            String hql = "update JobInstanceContent set errorMsg = :errorMsg where code = :code";
            this.deleteOrUpdateByHQL(hql, "errorMsg", errorMsg, "code", instanceCode);
        }

    }
}
