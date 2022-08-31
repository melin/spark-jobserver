package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.dao.TableLineageDao;
import io.github.melin.spark.jobserver.core.entity.TableLineage;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by admin on 2017/6/29.
 */
@Service
@Transactional
public class TableLineageService extends BaseServiceImpl<TableLineage, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(TableLineageService.class);

    @Autowired
    private TableLineageDao tableLineageDao;

    @Override
    public HibernateBaseDao<TableLineage, Long> getHibernateBaseDao() {
        return tableLineageDao;
    }

}
