package io.github.melin.spark.jobserver.rest;

import com.gitee.melin.bee.core.support.Result;
import io.github.melin.spark.jobserver.rest.dto.InstanceInfo;
import io.github.melin.spark.jobserver.rest.dto.JobSubmitRequet;
import io.github.melin.spark.jobserver.service.JobServerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Tag(name = "JobServer 接口")
public class JobServerRestApi {

    private static final Logger LOG = LoggerFactory.getLogger(JobServerRestApi.class);

    @Autowired
    private JobServerServiceImpl jobServerService;

    @PostMapping("v1/jobserver/submitJobInstance")
    @Operation(summary = "提交作业实例")
    @ResponseBody
    public Result<String> submitJobInstance(String accessKey, String accessSecret, JobSubmitRequet requet) {
        try {
            String instanceCode = jobServerService.submitJobInstance(requet);
            return Result.successDataResult(instanceCode);
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @GetMapping("v1/jobserver/queryInstanceStatus")
    @Operation(summary = "查询作业实例状态")
    @ResponseBody
    public Result<InstanceInfo> queryInstanceStatus(String accessKey, String accessSecret, String instanceCode) {
        try {
            InstanceInfo info = jobServerService.queryInstanceStatus(instanceCode);
            return Result.successDataResult(info);
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @GetMapping("v1/jobserver/batchQueryInstanceStatus")
    @Operation(summary = "批量查询作业实例状态")
    @ResponseBody
    public Result<List<InstanceInfo>> batchQueryInstanceStatus(String accessKey, String accessSecret, String[] instanceCode) {
        try {
            List<InstanceInfo> infos = jobServerService.batchQueryInstanceStatus(instanceCode);
            return Result.successDataResult(infos);
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @GetMapping("v1/jobserver/queryInstanceLog")
    @Operation(summary = "查询作业实例运行日志")
    @ResponseBody
    public Result<String> queryInstanceLog(String accessKey, String accessSecret, String instanceCode) {
        try {
            String log = jobServerService.queryInstanceLog(instanceCode);
            return Result.successDataResult(log);
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }

    @PostMapping("v1/jobserver/stopInstance")
    @Operation(summary = "停止作业运行")
    @ResponseBody
    public Result<String> stopInstance(String accessKey, String accessSecret, String instanceCode) {
        try {
            String error = jobServerService.stopInstance(instanceCode);
            if (error == null) {
                return Result.successResult();
            } else {
                return Result.failureResult(error);
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }
    }
}
