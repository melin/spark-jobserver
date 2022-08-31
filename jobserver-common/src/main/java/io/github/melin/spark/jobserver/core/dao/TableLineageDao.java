package io.github.melin.spark.jobserver.core.dao;

import io.github.melin.spark.jobserver.core.entity.TableLineage;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * huaixin 2022/3/28 11:57 AM
 */
@Repository
public class TableLineageDao extends HibernateBaseDaoImpl<TableLineage, Long> {
}
