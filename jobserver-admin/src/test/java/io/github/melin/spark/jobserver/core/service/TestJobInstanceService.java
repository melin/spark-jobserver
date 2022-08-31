package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * huaixin 2022/4/1 6:49 PM
 */
@ExtendWith(SpringExtension.class)
@DataJdbcTest
@ContextConfiguration(locations = {"classpath*:test-jobserver-context.xml"})
@Transactional
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@Import({JobInstanceService.class})
@EnableAspectJAutoProxy
public class TestJobInstanceService {

    @Autowired
    private JobInstanceService instanceService;

    @Test
    public void getScheduleInstancesTest() {
        JobInstance instance1 = JobInstance.builder().setCode("code1").setName("job1").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.WAITING).setOwner("admin")
                .setJobType(JobType.SPARK_JAR).setInstanceType(InstanceType.SCHEDULE).build();
        instanceService.insertEntity(instance1);

        JobInstance instance2 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.WAITING).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.DEV).build();
        instanceService.insertEntity(instance2);

        JobInstance instance3 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.WAITING).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.SCHEDULE).build();
        instanceService.insertEntity(instance3);

        JobInstance instance4 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.RUNNING).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.SCHEDULE).build();
        instanceService.insertEntity(instance4);

        List<Long> ids = instanceService.findScheduleInstances();
        assertEquals(2, ids.size());

        JobInstance instance5 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minus(8, ChronoUnit.DAYS))
                .setCreater("admin").setStatus(InstanceStatus.WAITING).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.SCHEDULE).build();
        instanceService.insertEntity(instance5);

        ids = instanceService.findScheduleInstances();
        assertEquals(2, ids.size());

        JobInstance instance6 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.FAILED).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.SCHEDULE)
                .setRetryCount(1).build();
        instanceService.insertEntity(instance6);

        ids = instanceService.findScheduleInstances();
        assertEquals(3, ids.size());

        JobInstance instance7 = JobInstance.builder().setCode("code2").setName("job2").setScheduleTime(Instant.now().minusSeconds(10))
                .setCreater("admin").setStatus(InstanceStatus.FAILED).setOwner("admin")
                .setJobType(JobType.SPARK_SQL).setInstanceType(InstanceType.SCHEDULE)
                .setRetryCount(1).setFailureCount(1).build();
        instanceService.insertEntity(instance7);

        ids = instanceService.findScheduleInstances();
        assertEquals(3, ids.size());
    }
}
