package io.github.melin.spark.jobserver.core.entity;

import com.gitee.melin.bee.model.IEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "sjs_job_instance_dependent")
public class JobInstanceDependent implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 45)
    private String code;

    @Column(name = "parent_code", nullable = false, length = 45)
    private String parentCode;

    public JobInstanceDependent(String code, String parentCode) {
        this.code = code;
        this.parentCode = parentCode;
    }
}
