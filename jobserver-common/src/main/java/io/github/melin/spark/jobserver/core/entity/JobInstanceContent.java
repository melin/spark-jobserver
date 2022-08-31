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
@Table(name = "sjs_job_instance_content")
public class JobInstanceContent implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 45)
    private String code;

    @Lob
    @Column(name = "job_text")
    private String jobText;

    @Column(name = "job_config")
    private String jobConfig;

    @Column(name = "error_msg")
    private String errorMsg;

    public JobInstanceContent(String code, String jobText, String jobConfig) {
        this.code = code;
        this.jobText = jobText;
        this.jobConfig = jobConfig;
    }
}
