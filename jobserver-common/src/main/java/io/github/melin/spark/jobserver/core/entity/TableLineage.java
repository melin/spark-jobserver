package io.github.melin.spark.jobserver.core.entity;

import com.gitee.melin.bee.model.IEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "meta_table_lineage")
public class TableLineage implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "type", length = 64)
    private String type;

    @Column(name = "source_catalog", length = 64)
    private String sourceCatalog;

    @Column(name = "source_table_name", length = 128)
    private String sourceTableName;

    @Column(name = "source_database", length = 64)
    private String sourceDatabase;

    @Column(name = "source_table", length = 128)
    private String sourceTable;

    @Column(name = "target_catalog", length = 64)
    private String targetCatalog;

    @Column(name = "target_database", length = 64)
    private String targetDatabase;

    @Column(name = "target_table", length = 128)
    private String targetTable;

    @Column(name = "job_code", length = 128)
    private String jobCode;

    @Column(name = "creater", nullable = false, length = 64)
    private String creater;

    @Column(name = "modifier", length = 64)
    private String modifier;

    @Column(name = "gmt_created", nullable = false)
    private Instant gmtCreated;

    @Column(name = "gmt_modified")
    private Instant gmtModified;
}
