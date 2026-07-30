package io.github.melin.spark.jobserver.core.dao;

import com.gitee.melin.bee.core.hibernate5.HibernateBaseDaoImpl;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import org.springframework.stereotype.Repository;

/**
 * huaixin 2022/3/28 11:57 AM
 */
@Repository
public class JobInstanceContentDao extends HibernateBaseDaoImpl<JobInstanceContent, Long> {}
