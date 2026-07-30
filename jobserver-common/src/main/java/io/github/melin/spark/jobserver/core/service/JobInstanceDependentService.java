package io.github.melin.spark.jobserver.core.service;

import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import io.github.melin.spark.jobserver.core.dao.JobInstanceDependentDao;
import io.github.melin.spark.jobserver.core.entity.JobInstanceDependent;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobInstanceDependentService extends BaseServiceImpl<JobInstanceDependent, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(JobInstanceDependentService.class);

    @Autowired
    private JobInstanceDependentDao jobInstanceDependentDao;

    @Override
    public HibernateBaseDao<JobInstanceDependent, Long> getHibernateBaseDao() {
        return jobInstanceDependentDao;
    }

    public List<String> findParentInstanceCode(String instanceCode) {
        return this.findByNamedParam("code", instanceCode).stream()
                .map(JobInstanceDependent::getParentCode)
                .collect(Collectors.toList());
    }
}
