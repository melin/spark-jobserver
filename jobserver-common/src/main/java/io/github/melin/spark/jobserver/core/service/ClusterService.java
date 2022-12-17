package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.dao.ClusterDao;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ClusterService extends BaseServiceImpl<Cluster, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterService.class);

    @Autowired
    private ClusterDao clusterDao;

    @Override
    public HibernateBaseDao<Cluster, Long> getHibernateBaseDao() {
        return clusterDao;
    }

    @Transactional
    public Cluster getClusterByCode(String clusterCode) {
        Cluster cluster = this.queryByNamedParam("code", clusterCode);
        if (cluster == null) {
            LOG.warn("compute cluster: {} not exists", clusterCode);
        }

        return cluster;
    }

    @Transactional
    public long getClusterUpdateTime(String clusterCode) {
        Instant gmtModified = this.queryByCriterions(Projections.property("gmtModified"),
                Restrictions.eq("code", clusterCode));
        if (gmtModified != null) {
            return gmtModified.getEpochSecond();
        } else {
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public boolean isKerberosEnabled(String cluserCode) {
        return this.getClusterByCode(cluserCode).isKerberosEnabled();
    }

    @Transactional(readOnly = true)
    public String queryKerberosUser(String clusterCode) {
        String kerberosUser = this.queryByCriterions(Projections.property("kerberosUser"),
                Restrictions.eq("code", clusterCode));

        if (StringUtils.isBlank(kerberosUser)) {
            throw new IllegalStateException(clusterCode + " 集群 kerberos user 为空");
        }

        return kerberosUser;
    }

    @Transactional(readOnly = true)
    public List<Cluster> queryValidClusters() {
        return this.findByNamedParam("status", true);
    }
}
