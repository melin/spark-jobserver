let SPARK_CONFIG_OPTIONS = [ {
    "caption" : "spark.shuffle.push.mergersMinStaticThreshold",
    "value" : "spark.shuffle.push.mergersMinStaticThreshold = 5",
    "meta" : "default: 5",
    "version" : "3.2.0",
    "docHTML" : "The static threshold for number of shuffle push merger locations should be available in order to enable push-based shuffle for a stage. Note this config works in conjunction with spark.shuffle.push.mergersMinThresholdRatio. Maximum of spark.shuffle.push.mergersMinStaticThreshold and spark.shuffle.push.mergersMinThresholdRatio ratio number of mergers needed to enable push-based shuffle for a stage. For eg: with 1000 partitions for the child stage with spark.shuffle.push.mergersMinStaticThreshold as 5 and spark.shuffle.push.mergersMinThresholdRatio set to 0.05, we would need at least 50 mergers to enable push-based shuffle for that stage."
}, {
    "caption" : "spark.eventLog.gcMetrics.youngGenerationGarbageCollectors",
    "value" : "spark.eventLog.gcMetrics.youngGenerationGarbageCollectors = ",
    "version" : "3.0.0",
    "docHTML" : "Names of supported young generation garbage collector. A name usually is  the return of GarbageCollectorMXBean.getName. The built-in young generation garbage collectors are List(Copy, PS Scavenge, ParNew, G1 Young Generation)"
}, {
    "caption" : "spark.eventLog.gcMetrics.oldGenerationGarbageCollectors",
    "value" : "spark.eventLog.gcMetrics.oldGenerationGarbageCollectors = ",
    "version" : "3.0.0",
    "docHTML" : "Names of supported old generation garbage collector. A name usually is the return of GarbageCollectorMXBean.getName. The built-in old generation garbage collectors are List(MarkSweepCompact, PS MarkSweep, ConcurrentMarkSweep, G1 Old Generation)"
}, {
    "caption" : "spark.executor.heartbeat.dropZeroAccumulatorUpdates",
    "value" : "spark.executor.heartbeat.dropZeroAccumulatorUpdates = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.storage.decommission.shuffleBlocks.enabled",
    "value" : "spark.storage.decommission.shuffleBlocks.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to transfer shuffle blocks during block manager decommissioning. Requires a migratable shuffle resolver (like sort based shuffle)"
}, {
    "caption" : "spark.storage.decommission.maxReplicationFailuresPerBlock",
    "value" : "spark.storage.decommission.maxReplicationFailuresPerBlock = 3",
    "meta" : "default: 3",
    "version" : "3.1.0",
    "docHTML" : "Maximum number of failures which can be handled for the replication of one RDD block when block manager is decommissioning and trying to move its existing blocks."
}, {
    "caption" : "spark.storage.decommission.replicationReattemptInterval",
    "value" : "spark.storage.decommission.replicationReattemptInterval = 30s",
    "meta" : "default: 30s",
    "version" : "3.1.0",
    "docHTML" : "The interval of time between consecutive cache block replication reattempts happening on each decommissioning executor (due to storage decommissioning)."
}, {
    "caption" : "spark.storage.decommission.fallbackStorage.cleanUp",
    "value" : "spark.storage.decommission.fallbackStorage.cleanUp = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "If true, Spark cleans up its fallback storage data during shutting down."
}, {
    "caption" : "spark.storage.blockManagerMasterDriverHeartbeatTimeoutMs",
    "value" : "spark.storage.blockManagerMasterDriverHeartbeatTimeoutMs = 10m",
    "meta" : "default: 10m",
    "version" : "3.2.0",
    "docHTML" : "A timeout used for block manager master's driver heartbeat endpoint."
}, {
    "caption" : "spark.storage.cleanupFilesAfterExecutorExit",
    "value" : "spark.storage.cleanupFilesAfterExecutorExit = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "Whether or not cleanup the files not served by the external shuffle service on executor exits."
}, {
    "caption" : "spark.dynamicAllocation.cachedExecutorIdleTimeout",
    "value" : "spark.dynamicAllocation.cachedExecutorIdleTimeout = 2147483647000ms",
    "meta" : "default: 2147483647000ms",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.sustainedSchedulerBacklogTimeout",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.files.fetchFailure.unRegisterOutputOnHost",
    "value" : "spark.files.fetchFailure.unRegisterOutputOnHost = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "Whether to un-register all the outputs on the host in condition that we receive  a FetchFailure. This is set default to false, which means, we only un-register the  outputs related to the exact executor(instead of the host) on a FetchFailure."
}, {
    "caption" : "spark.scheduler.listenerbus.metrics.maxListenerClassesTimed",
    "value" : "spark.scheduler.listenerbus.metrics.maxListenerClassesTimed = 128",
    "meta" : "default: 128",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.scheduler.listenerbus.logSlowEvent.threshold",
    "value" : "spark.scheduler.listenerbus.logSlowEvent.threshold = 1s",
    "meta" : "default: 1s",
    "version" : "3.0.0",
    "docHTML" : "The time threshold of whether a event is considered to be taking too much time to process. Log the event if spark.scheduler.listenerbus.logSlowEvent is true."
}, {
    "caption" : "spark.taskMetrics.trackUpdatedBlockStatuses",
    "value" : "spark.taskMetrics.trackUpdatedBlockStatuses = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "Enable tracking of updatedBlockStatuses in the TaskMetrics. Off by default since tracking the block statuses can use a lot of memory and its not used anywhere within spark."
}, {
    "caption" : "spark.shuffle.spill.numElementsForceSpillThreshold",
    "value" : "spark.shuffle.spill.numElementsForceSpillThreshold = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "1.6.0",
    "docHTML" : "The maximum number of elements in memory before forcing the shuffle sorter to spill. By default it's Integer.MAX_VALUE, which means we never force the sorter to spill, until we reach some limitations, like the max page size limitation for the pointer array in the sorter."
}, {
    "caption" : "spark.shuffle.mapOutput.parallelAggregationThreshold",
    "value" : "spark.shuffle.mapOutput.parallelAggregationThreshold = 10000000",
    "meta" : "default: 10000000",
    "version" : "2.3.0",
    "docHTML" : "Multi-thread is used when the number of mappers * shuffle partitions is greater than or equal to this threshold. Note that the actual parallelism is calculated by number of mappers * shuffle partitions / this threshold + 1, so this threshold should be positive."
}, {
    "caption" : "spark.storage.localDiskByExecutors.cacheSize",
    "value" : "spark.storage.localDiskByExecutors.cacheSize = 1000",
    "meta" : "default: 1000",
    "version" : "3.0.0",
    "docHTML" : "The max number of executors for which the local dirs are stored. This size is both applied for the driver and both for the executors side to avoid having an unbounded store. This cache will be used to avoid the network in case of fetching disk persisted RDD blocks or shuffle blocks (when `spark.shuffle.readHostLocalDisk` is set) from the same host."
}, {
    "caption" : "spark.scheduler.barrier.maxConcurrentTasksCheck.interval",
    "value" : "spark.scheduler.barrier.maxConcurrentTasksCheck.interval = 15s",
    "meta" : "default: 15s",
    "version" : "2.4.0",
    "docHTML" : "Time in seconds to wait between a max concurrent tasks check failure and the next check. A max concurrent tasks check ensures the cluster can launch more concurrent tasks than required by a barrier stage on job submitted. The check can fail in case a cluster has just started and not enough executors have registered, so we wait for a little while and try to perform the check again. If the check fails more than a configured max failure times for a job then fail current job submission. Note this config only applies to jobs that contain one or more barrier stages, we won't perform the check on non-barrier jobs."
}, {
    "caption" : "spark.scheduler.barrier.maxConcurrentTasksCheck.maxFailures",
    "value" : "spark.scheduler.barrier.maxConcurrentTasksCheck.maxFailures = 40",
    "meta" : "default: 40",
    "version" : "2.4.0",
    "docHTML" : "Number of max concurrent tasks check failures allowed before fail a job submission. A max concurrent tasks check ensures the cluster can launch more concurrent tasks than required by a barrier stage on job submitted. The check can fail in case a cluster has just started and not enough executors have registered, so we wait for a little while and try to perform the check again. If the check fails more than a configured max failure times for a job then fail current job submission. Note this config only applies to jobs that contain one or more barrier stages, we won't perform the check on non-barrier jobs."
}, {
    "caption" : "spark.cleaner.referenceTracking.blocking.shuffle",
    "value" : "spark.cleaner.referenceTracking.blocking.shuffle = false",
    "meta" : "default: false",
    "version" : "1.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.cleaner.referenceTracking.cleanCheckpoints",
    "value" : "spark.cleaner.referenceTracking.cleanCheckpoints = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.scheduler.maxRegisteredResourcesWaitingTime",
    "value" : "spark.scheduler.maxRegisteredResourcesWaitingTime = 30s",
    "meta" : "default: 30s",
    "version" : "1.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.push.finalize.timeout",
    "value" : "spark.shuffle.push.finalize.timeout = 10s",
    "meta" : "default: 10s",
    "version" : "3.2.0",
    "docHTML" : "The amount of time driver waits, after all mappers have finished for a given shuffle map stage, before it sends merge finalize requests to remote external shuffle services. This gives the external shuffle services extra time to merge blocks. Setting this too long could potentially lead to performance regression"
}, {
    "caption" : "spark.shuffle.push.mergersMinThresholdRatio",
    "value" : "spark.shuffle.push.mergersMinThresholdRatio = 0.05",
    "meta" : "default: 0.05",
    "version" : "3.2.0",
    "docHTML" : "Ratio used to compute the minimum number of shuffle merger locations required for a stage based on the number of partitions for the reducer stage. For example, a reduce stage which has 100 partitions and uses the default value 0.05 requires at least 5 unique merger locations to enable push-based shuffle. Merger locations are currently defined as external shuffle services."
}, {
    "caption" : "spark.shuffle.push.merge.finalizeThreads",
    "value" : "spark.shuffle.push.merge.finalizeThreads = 3",
    "meta" : "default: 3",
    "version" : "3.3.0",
    "docHTML" : "Number of threads used by driver to finalize shuffle merge. Since it could potentially take seconds for a large shuffle to finalize, having multiple threads helps driver to handle concurrent shuffle merge finalize requests when push-based shuffle is enabled."
}, {
    "caption" : "spark.shuffle.push.minShuffleSizeToWait",
    "value" : "spark.shuffle.push.minShuffleSizeToWait = 500m",
    "meta" : "default: 500m",
    "version" : "3.3.0",
    "docHTML" : "Driver will wait for merge finalization to complete only if total shuffle size is more than this threshold. If total shuffle size is less, driver will immediately finalize the shuffle output"
}, {
    "caption" : "spark.resources.discoveryPlugin",
    "value" : "spark.resources.discoveryPlugin = ",
    "version" : "3.0.0",
    "docHTML" : "Comma-separated list of class names implementingorg.apache.spark.api.resource.ResourceDiscoveryPlugin to load into the application.This is for advanced users to replace the resource discovery class with a custom implementation. Spark will try each class specified until one of them returns the resource information for that resource. It tries the discovery script last if none of the plugins return information for that resource."
}, {
    "caption" : "spark.driver.userClassPathFirst",
    "value" : "spark.driver.userClassPathFirst = false",
    "meta" : "default: false",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.driver.memoryOverheadFactor",
    "value" : "spark.driver.memoryOverheadFactor = 0.1",
    "meta" : "default: 0.1",
    "version" : "3.3.0",
    "docHTML" : "Fraction of driver memory to be allocated as additional non-heap memory per driver process in cluster mode. This is memory that accounts for things like VM overheads, interned strings, other native overheads, etc. This tends to grow with the container size. This value defaults to 0.10 except for Kubernetes non-JVM jobs, which defaults to 0.40. This is done as non-JVM tasks need more non-JVM heap space and such tasks commonly fail with \"Memory Overhead Exceeded\" errors. This preempts this error with a higher default. This value is ignored if spark.driver.memoryOverhead is set directly."
}, {
    "caption" : "spark.eventLog.buffer.kb",
    "value" : "spark.eventLog.buffer.kb = 100k",
    "meta" : "default: 100k",
    "version" : "1.0.0",
    "docHTML" : "Buffer size to use when writing to output streams, in KiB unless otherwise specified."
}, {
    "caption" : "spark.eventLog.logStageExecutorMetrics",
    "value" : "spark.eventLog.logStageExecutorMetrics = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether to write per-stage peaks of executor metrics (for each executor) to the event log."
}, {
    "caption" : "spark.eventLog.longForm.enabled",
    "value" : "spark.eventLog.longForm.enabled = false",
    "meta" : "default: false",
    "version" : "2.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.rolling.maxFileSize",
    "value" : "spark.eventLog.rolling.maxFileSize = 128m",
    "meta" : "default: 128m",
    "version" : "3.0.0",
    "docHTML" : "When spark.eventLog.rolling.enabled=true, specifies the max size of event log file to be rolled over."
}, {
    "caption" : "spark.executor.heartbeatInterval",
    "value" : "spark.executor.heartbeatInterval = 10s",
    "meta" : "default: 10s",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.heartbeat.maxFailures",
    "value" : "spark.executor.heartbeat.maxFailures = 60",
    "meta" : "default: 60",
    "version" : "1.6.2",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.processTreeMetrics.enabled",
    "value" : "spark.executor.processTreeMetrics.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether to collect process tree metrics (from the /proc filesystem) when collecting executor metrics."
}, {
    "caption" : "spark.executor.metrics.pollingInterval",
    "value" : "spark.executor.metrics.pollingInterval = 0",
    "meta" : "default: 0",
    "version" : "3.0.0",
    "docHTML" : "How often to collect executor metrics (in milliseconds). If 0, the polling is done on executor heartbeats. If positive, the polling is done at this interval."
}, {
    "caption" : "spark.executor.metrics.fileSystemSchemes",
    "value" : "spark.executor.metrics.fileSystemSchemes = file,hdfs",
    "meta" : "default: file,hdfs",
    "version" : "3.1.0",
    "docHTML" : "The file system schemes to report in executor metrics."
}, {
    "caption" : "spark.executor.userClassPathFirst",
    "value" : "spark.executor.userClassPathFirst = false",
    "meta" : "default: false",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.memoryOverheadFactor",
    "value" : "spark.executor.memoryOverheadFactor = 0.1",
    "meta" : "default: 0.1",
    "version" : "3.3.0",
    "docHTML" : "Fraction of executor memory to be allocated as additional non-heap memory per executor process. This is memory that accounts for things like VM overheads, interned strings, other native overheads, etc. This tends to grow with the container size. This value defaults to 0.10 except for Kubernetes non-JVM jobs, which defaults to 0.40. This is done as non-JVM tasks need more non-JVM heap space and such tasks commonly fail with \"Memory Overhead Exceeded\" errors. This preempts this error with a higher default. This value is ignored if spark.executor.memoryOverhead is set directly."
}, {
    "caption" : "spark.storage.unrollMemoryThreshold",
    "value" : "spark.storage.unrollMemoryThreshold = 1048576",
    "meta" : "default: 1048576",
    "version" : "1.1.0",
    "docHTML" : "Initial memory to request before unrolling any block"
}, {
    "caption" : "spark.storage.replication.proactive",
    "value" : "spark.storage.replication.proactive = true",
    "meta" : "default: true",
    "version" : "2.2.0",
    "docHTML" : "Enables proactive block replication for RDD blocks. Cached RDD block replicas lost due to executor failures are replenished if there are any existing available replicas. This tries to get the replication level of the block to the initial number"
}, {
    "caption" : "spark.storage.memoryMapThreshold",
    "value" : "spark.storage.memoryMapThreshold = 2m",
    "meta" : "default: 2m",
    "version" : "0.9.2",
    "docHTML" : "Size in bytes of a block above which Spark memory maps when reading a block from disk. This prevents Spark from memory mapping very small blocks. In general, memory mapping has high overhead for blocks close to or below the page size of the operating system."
}, {
    "caption" : "spark.storage.replication.policy",
    "value" : "spark.storage.replication.policy = org.apache.spark.storage.RandomBlockReplicationPolicy",
    "meta" : "default: org.apache.spark.storage.RandomBlockReplicationPolicy",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.storage.replication.topologyMapper",
    "value" : "spark.storage.replication.topologyMapper = org.apache.spark.storage.DefaultTopologyMapper",
    "meta" : "default: org.apache.spark.storage.DefaultTopologyMapper",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.storage.maxReplicationFailures",
    "value" : "spark.storage.maxReplicationFailures = 1",
    "meta" : "default: 1",
    "version" : "1.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.storage.decommission.enabled",
    "value" : "spark.storage.decommission.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to decommission the block manager when decommissioning executor"
}, {
    "caption" : "spark.storage.decommission.shuffleBlocks.maxThreads",
    "value" : "spark.storage.decommission.shuffleBlocks.maxThreads = 8",
    "meta" : "default: 8",
    "version" : "3.1.0",
    "docHTML" : "Maximum number of threads to use in migrating shuffle files."
}, {
    "caption" : "spark.storage.decommission.rddBlocks.enabled",
    "value" : "spark.storage.decommission.rddBlocks.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to transfer RDD blocks during block manager decommissioning."
}, {
    "caption" : "spark.storage.exceptionOnPinLeak",
    "value" : "spark.storage.exceptionOnPinLeak = false",
    "meta" : "default: false",
    "version" : "1.6.2",
    "docHTML" : ""
}, {
    "caption" : "spark.storage.blockManagerTimeoutIntervalMs",
    "value" : "spark.storage.blockManagerTimeoutIntervalMs = 60s",
    "meta" : "default: 60s",
    "version" : "0.7.3",
    "docHTML" : ""
}, {
    "caption" : "spark.diskStore.subDirectories",
    "value" : "spark.diskStore.subDirectories = 64",
    "meta" : "default: 64",
    "version" : "0.6.0",
    "docHTML" : "Number of subdirectories inside each path listed in spark.local.dir for hashing Block files into."
}, {
    "caption" : "spark.block.failures.beforeLocationRefresh",
    "value" : "spark.block.failures.beforeLocationRefresh = 5",
    "meta" : "default: 5",
    "version" : "2.0.0",
    "docHTML" : "Max number of failures before this block manager refreshes the block locations from the driver."
}, {
    "caption" : "spark.dynamicAllocation.minExecutors",
    "value" : "spark.dynamicAllocation.minExecutors = 0",
    "meta" : "default: 0",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.initialExecutors",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.maxExecutors",
    "value" : "spark.dynamicAllocation.maxExecutors = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.executorAllocationRatio",
    "value" : "spark.dynamicAllocation.executorAllocationRatio = 1.0",
    "meta" : "default: 1.0",
    "version" : "2.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.executorIdleTimeout",
    "value" : "spark.dynamicAllocation.executorIdleTimeout = 60000ms",
    "meta" : "default: 60000ms",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.shuffleTracking.enabled",
    "value" : "spark.dynamicAllocation.shuffleTracking.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.shuffleTracking.timeout",
    "value" : "spark.dynamicAllocation.shuffleTracking.timeout = 9223372036854775807ms",
    "meta" : "default: 9223372036854775807ms",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.schedulerBacklogTimeout",
    "value" : "spark.dynamicAllocation.schedulerBacklogTimeout = 1000ms",
    "meta" : "default: 1000ms",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.locality.wait.legacyResetOnTaskLaunch",
    "value" : "spark.locality.wait.legacyResetOnTaskLaunch = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to use the legacy behavior of locality wait, which resets the delay timer anytime a task is scheduled. See Delay Scheduling section of TaskSchedulerImpl's class documentation for more details."
}, {
    "caption" : "spark.shuffle.service.removeShuffle",
    "value" : "spark.shuffle.service.removeShuffle = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Whether to use the ExternalShuffleService for deleting shuffle blocks for deallocated executors when the shuffle is no longer needed. Without this enabled, shuffle data on executors that are deallocated will remain on disk until the application ends."
}, {
    "caption" : "spark.shuffle.service.fetch.rdd.enabled",
    "value" : "spark.shuffle.service.fetch.rdd.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether to use the ExternalShuffleService for fetching disk persisted RDD blocks. In case of dynamic allocation if this feature is enabled executors having only disk persisted blocks are considered idle after 'spark.dynamicAllocation.executorIdleTimeout' and will be released accordingly."
}, {
    "caption" : "spark.shuffle.service.db.enabled",
    "value" : "spark.shuffle.service.db.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to use db in ExternalShuffleService. Note that this only affects standalone mode."
}, {
    "caption" : "spark.kerberos.renewal.credentials",
    "value" : "spark.kerberos.renewal.credentials = keytab",
    "meta" : "default: keytab",
    "version" : "3.0.0",
    "docHTML" : "Which credentials to use when renewing delegation tokens for executors. Can be either 'keytab', the default, which requires a keytab to be provided, or 'ccache', which uses the local credentials cache."
}, {
    "caption" : "spark.kerberos.access.hadoopFileSystems",
    "value" : "spark.kerberos.access.hadoopFileSystems = ",
    "version" : "3.0.0",
    "docHTML" : "Extra Hadoop filesystem URLs for which to request delegation tokens. The filesystem that hosts fs.defaultFS does not need to be listed here."
}, {
    "caption" : "spark.yarn.kerberos.renewal.excludeHadoopFileSystems",
    "value" : "spark.yarn.kerberos.renewal.excludeHadoopFileSystems = ",
    "version" : "3.2.0",
    "docHTML" : "The list of Hadoop filesystem URLs whose hosts will be excluded from delegation token renewal at resource scheduler. Currently this is known to work under YARN, so YARN Resource Manager won't renew tokens for the application. Note that as resource scheduler does not renew token, so any application running longer than the original token expiration that tries to use that token will likely fail."
}, {
    "caption" : "spark.task.maxDirectResultSize",
    "value" : "spark.task.maxDirectResultSize = 1048576b",
    "meta" : "default: 1048576b",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.task.reaper.pollingInterval",
    "value" : "spark.task.reaper.pollingInterval = 10s",
    "meta" : "default: 10s",
    "version" : "2.0.3",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.task.maxTaskAttemptsPerExecutor",
    "value" : "spark.excludeOnFailure.task.maxTaskAttemptsPerExecutor = 1",
    "meta" : "default: 1",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.task.maxTaskAttemptsPerNode",
    "value" : "spark.excludeOnFailure.task.maxTaskAttemptsPerNode = 2",
    "meta" : "default: 2",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.stage.maxFailedTasksPerExecutor",
    "value" : "spark.excludeOnFailure.stage.maxFailedTasksPerExecutor = 2",
    "meta" : "default: 2",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.stage.maxFailedExecutorsPerNode",
    "value" : "spark.excludeOnFailure.stage.maxFailedExecutorsPerNode = 2",
    "meta" : "default: 2",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.killExcludedExecutors",
    "value" : "spark.excludeOnFailure.killExcludedExecutors = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.killExcludedExecutors.decommission",
    "value" : "spark.excludeOnFailure.killExcludedExecutors.decommission = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "Attempt decommission of excluded nodes instead of going directly to kill"
}, {
    "caption" : "spark.excludeOnFailure.application.fetchFailure.enabled",
    "value" : "spark.excludeOnFailure.application.fetchFailure.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.scheduler.listenerbus.eventqueue.capacity",
    "value" : "spark.scheduler.listenerbus.eventqueue.capacity = 10000",
    "meta" : "default: 10000",
    "version" : "2.3.0",
    "docHTML" : "The default capacity for event queues. Spark will try to initialize an event queue using capacity specified by `spark.scheduler.listenerbus.eventqueue.queueName.capacity` first. If it's not configured, Spark will use the default capacity specified by this config."
}, {
    "caption" : "spark.scheduler.listenerbus.logSlowEvent",
    "value" : "spark.scheduler.listenerbus.logSlowEvent = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When enabled, log the event that takes too much time to process. This helps us discover the event types that cause performance bottlenecks. The time threshold is controlled by spark.scheduler.listenerbus.logSlowEvent.threshold."
}, {
    "caption" : "spark.metrics.executorMetricsSource.enabled",
    "value" : "spark.metrics.executorMetricsSource.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to register the ExecutorMetrics source with the metrics system."
}, {
    "caption" : "spark.metrics.staticSources.enabled",
    "value" : "spark.metrics.staticSources.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to register static sources with the metrics system."
}, {
    "caption" : "spark.io.encryption.keygen.algorithm",
    "value" : "spark.io.encryption.keygen.algorithm = HmacSHA1",
    "meta" : "default: HmacSHA1",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.io.encryption.keySizeBits",
    "value" : "spark.io.encryption.keySizeBits = 128",
    "meta" : "default: 128",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.io.crypto.cipher.transformation",
    "value" : "spark.io.crypto.cipher.transformation = AES/CTR/NoPadding",
    "meta" : "default: AES/CTR/NoPadding",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.driver.blockManager.port",
    "version" : "2.1.0",
    "docHTML" : "Port to use for the block manager on the driver."
}, {
    "caption" : "spark.files.maxPartitionBytes",
    "value" : "spark.files.maxPartitionBytes = 134217728b",
    "meta" : "default: 134217728b",
    "version" : "2.1.0",
    "docHTML" : "The maximum number of bytes to pack into a single partition when reading files."
}, {
    "caption" : "spark.hadoopRDD.ignoreEmptySplits",
    "value" : "spark.hadoopRDD.ignoreEmptySplits = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "When true, HadoopRDD/NewHadoopRDD will not create partitions for empty input splits."
}, {
    "caption" : "spark.authenticate.secret.executor.file",
    "version" : "3.0.0",
    "docHTML" : "Path to a file that contains the authentication secret to use. Loaded by the executors only. In Kubernetes client mode it is often useful to set a different secret path for the driver vs. the executors, since the driver may not be running in a pod unlike the executors. If this is set, an accompanying secret file must be specified for the executors. The fallback configuration allows the same path to be used for both the driver and the executors when running in cluster mode. File-based secret keys are only allowed when using Kubernetes."
}, {
    "caption" : "spark.shuffle.accurateBlockThreshold",
    "value" : "spark.shuffle.accurateBlockThreshold = 104857600b",
    "meta" : "default: 104857600b",
    "version" : "2.2.1",
    "docHTML" : "Threshold in bytes above which the size of shuffle blocks in HighlyCompressedMapStatus is accurately recorded. This helps to prevent OOM by avoiding underestimating shuffle block size when fetch shuffle blocks."
}, {
    "caption" : "spark.shuffle.accurateBlockSkewedFactor",
    "value" : "spark.shuffle.accurateBlockSkewedFactor = -1.0",
    "meta" : "default: -1.0",
    "version" : "3.3.0",
    "docHTML" : "A shuffle block is considered as skewed and will be accurately recorded in HighlyCompressedMapStatus if its size is larger than this factor multiplying the median shuffle block size or SHUFFLE_ACCURATE_BLOCK_THRESHOLD. It is recommended to set this parameter to be the same as SKEW_JOIN_SKEWED_PARTITION_FACTOR.Set to -1.0 to disable this feature by default."
}, {
    "caption" : "spark.shuffle.maxAccurateSkewedBlockNumber",
    "value" : "spark.shuffle.maxAccurateSkewedBlockNumber = 100",
    "meta" : "default: 100",
    "version" : "3.3.0",
    "docHTML" : "Max skewed shuffle blocks allowed to be accurately recorded in HighlyCompressedMapStatus if its size is larger than SHUFFLE_ACCURATE_BLOCK_SKEWED_FACTOR multiplying the median shuffle block size or SHUFFLE_ACCURATE_BLOCK_THRESHOLD."
}, {
    "caption" : "spark.shuffle.registration.timeout",
    "value" : "spark.shuffle.registration.timeout = 5000ms",
    "meta" : "default: 5000ms",
    "version" : "2.3.0",
    "docHTML" : "Timeout in milliseconds for registration to the external shuffle service."
}, {
    "caption" : "spark.shuffle.registration.maxAttempts",
    "value" : "spark.shuffle.registration.maxAttempts = 3",
    "meta" : "default: 3",
    "version" : "2.3.0",
    "docHTML" : "When we fail to register to the external shuffle service, we will retry for maxAttempts times."
}, {
    "caption" : "spark.shuffle.maxAttemptsOnNettyOOM",
    "value" : "spark.shuffle.maxAttemptsOnNettyOOM = 10",
    "meta" : "default: 10",
    "version" : "3.2.0",
    "docHTML" : "The max attempts of a shuffle block would retry on Netty OOM issue before throwing the shuffle fetch failure."
}, {
    "caption" : "spark.reducer.maxBlocksInFlightPerAddress",
    "value" : "spark.reducer.maxBlocksInFlightPerAddress = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.2.1",
    "docHTML" : "This configuration limits the number of remote blocks being fetched per reduce task from a given host port. When a large number of blocks are being requested from a given address in a single fetch or simultaneously, this could crash the serving executor or Node Manager. This is especially useful to reduce the load on the Node Manager when external shuffle is enabled. You can mitigate the issue by setting it to a lower value."
}, {
    "caption" : "spark.network.maxRemoteBlockSizeFetchToMem",
    "value" : "spark.network.maxRemoteBlockSizeFetchToMem = 200m",
    "meta" : "default: 200m",
    "version" : "3.0.0",
    "docHTML" : "Remote block will be fetched to disk when size of the block is above this threshold in bytes. This is to avoid a giant request takes too much memory. Note this configuration will affect both shuffle fetch and block manager remote block fetch. For users who enabled external shuffle service, this feature can only work when external shuffle service is at least 2.3.0."
}, {
    "caption" : "spark.shuffle.unsafe.file.output.buffer",
    "value" : "spark.shuffle.unsafe.file.output.buffer = 32k",
    "meta" : "default: 32k",
    "version" : "2.3.0",
    "docHTML" : "The file system for this buffer size after each partition is written in unsafe shuffle writer. In KiB unless otherwise specified."
}, {
    "caption" : "spark.shuffle.spill.diskWriteBufferSize",
    "value" : "spark.shuffle.spill.diskWriteBufferSize = 1048576b",
    "meta" : "default: 1048576b",
    "version" : "2.3.0",
    "docHTML" : "The buffer size, in bytes, to use when writing the sorted records to an on-disk file."
}, {
    "caption" : "spark.storage.unrollMemoryCheckPeriod",
    "value" : "spark.storage.unrollMemoryCheckPeriod = 16",
    "meta" : "default: 16",
    "version" : "2.3.0",
    "docHTML" : "The memory check period is used to determine how often we should check whether there is a need to request more memory when we try to unroll the given block in memory."
}, {
    "caption" : "spark.storage.unrollMemoryGrowthFactor",
    "value" : "spark.storage.unrollMemoryGrowthFactor = 1.5",
    "meta" : "default: 1.5",
    "version" : "2.3.0",
    "docHTML" : "Memory to request as a multiple of the size that used to unroll the block."
}, {
    "caption" : "spark.security.credentials.renewalRatio",
    "value" : "spark.security.credentials.renewalRatio = 0.75",
    "meta" : "default: 0.75",
    "version" : "2.4.0",
    "docHTML" : "Ratio of the credential's expiration time when Spark should fetch new credentials."
}, {
    "caption" : "spark.security.credentials.retryWait",
    "value" : "spark.security.credentials.retryWait = 1h",
    "meta" : "default: 1h",
    "version" : "2.4.0",
    "docHTML" : "How long to wait before retrying to fetch new credentials after a failure."
}, {
    "caption" : "spark.shuffle.sort.initialBufferSize",
    "value" : "spark.shuffle.sort.initialBufferSize = 4096b",
    "meta" : "default: 4096b",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.checksum.algorithm",
    "value" : "spark.shuffle.checksum.algorithm = ADLER32",
    "meta" : "default: ADLER32",
    "version" : "3.2.0",
    "docHTML" : "The algorithm is used to calculate the shuffle checksum. Currently, it only supports built-in algorithms of JDK."
}, {
    "caption" : "spark.shuffle.mapStatus.compression.codec",
    "value" : "spark.shuffle.mapStatus.compression.codec = zstd",
    "meta" : "default: zstd",
    "version" : "3.0.0",
    "docHTML" : "The codec used to compress MapStatus, which is generated by ShuffleMapTask. By default, Spark provides four codecs: lz4, lzf, snappy, and zstd. You can also use fully qualified class names to specify the codec."
}, {
    "caption" : "spark.shuffle.spill.initialMemoryThreshold",
    "value" : "spark.shuffle.spill.initialMemoryThreshold = 5242880b",
    "meta" : "default: 5242880b",
    "version" : "1.1.1",
    "docHTML" : "Initial threshold for the size of a collection before we start tracking its memory usage."
}, {
    "caption" : "spark.shuffle.sort.bypassMergeThreshold",
    "value" : "spark.shuffle.sort.bypassMergeThreshold = 200",
    "meta" : "default: 200",
    "version" : "1.1.1",
    "docHTML" : "In the sort-based shuffle manager, avoid merge-sorting data if there is no map-side aggregation and there are at most this many reduce partitions"
}, {
    "caption" : "spark.shuffle.reduceLocality.enabled",
    "value" : "spark.shuffle.reduceLocality.enabled = true",
    "meta" : "default: true",
    "version" : "1.5.0",
    "docHTML" : "Whether to compute locality preferences for reduce tasks"
}, {
    "caption" : "spark.shuffle.mapOutput.minSizeForBroadcast",
    "value" : "spark.shuffle.mapOutput.minSizeForBroadcast = 512k",
    "meta" : "default: 512k",
    "version" : "2.0.0",
    "docHTML" : "The size at which we use Broadcast to send the map output statuses to the executors."
}, {
    "caption" : "spark.shuffle.mapOutput.dispatcher.numThreads",
    "value" : "spark.shuffle.mapOutput.dispatcher.numThreads = 8",
    "meta" : "default: 8",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.detectCorrupt.useExtraMemory",
    "value" : "spark.shuffle.detectCorrupt.useExtraMemory = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "If enabled, part of a compressed/encrypted stream will be de-compressed/de-crypted by using extra memory to detect early corruption. Any IOException thrown will cause the task to be retried once and if it fails again with same exception, then FetchFailedException will be thrown to retry previous stage"
}, {
    "caption" : "spark.shuffle.unsafe.fastMergeEnabled",
    "value" : "spark.shuffle.unsafe.fastMergeEnabled = true",
    "meta" : "default: true",
    "version" : "1.4.0",
    "docHTML" : "Whether to perform a fast spill merge."
}, {
    "caption" : "spark.shuffle.sort.useRadixSort",
    "value" : "spark.shuffle.sort.useRadixSort = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "Whether to use radix sort for sorting in-memory partition ids. Radix sort is much faster, but requires additional memory to be reserved memory as pointers are added."
}, {
    "caption" : "spark.shuffle.minNumPartitionsToHighlyCompress",
    "value" : "spark.shuffle.minNumPartitionsToHighlyCompress = 2000",
    "meta" : "default: 2000",
    "version" : "2.4.0",
    "docHTML" : "Number of partitions to determine if MapStatus should use HighlyCompressedMapStatus"
}, {
    "caption" : "spark.shuffle.useOldFetchProtocol",
    "value" : "spark.shuffle.useOldFetchProtocol = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether to use the old protocol while doing the shuffle block fetching. It is only enabled while we need the compatibility in the scenario of new Spark version job fetching shuffle blocks from old version external shuffle service."
}, {
    "caption" : "spark.shuffle.readHostLocalDisk",
    "value" : "spark.shuffle.readHostLocalDisk = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "If enabled (and `spark.shuffle.useOldFetchProtocol` is disabled, shuffle blocks requested from those block managers which are running on the same host are read from the disk directly instead of being fetched as remote blocks over the network."
}, {
    "caption" : "spark.storage.memoryMapLimitForTests",
    "value" : "spark.storage.memoryMapLimitForTests = 2147483632b",
    "meta" : "default: 2147483632b",
    "version" : "2.3.0",
    "docHTML" : "For testing only, controls the size of chunks when memory mapping a file"
}, {
    "caption" : "spark.scheduler.excludeOnFailure.unschedulableTaskSetTimeout",
    "value" : "spark.scheduler.excludeOnFailure.unschedulableTaskSetTimeout = 120000ms",
    "meta" : "default: 120000ms",
    "version" : "3.1.0",
    "docHTML" : "The timeout in seconds to wait to acquire a new executor and schedule a task before aborting a TaskSet which is unschedulable because all executors are excluded due to failures."
}, {
    "caption" : "spark.unsafe.exceptionOnMemoryLeak",
    "value" : "spark.unsafe.exceptionOnMemoryLeak = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.unsafe.sorter.spill.read.ahead.enabled",
    "value" : "spark.unsafe.sorter.spill.read.ahead.enabled = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.unsafe.sorter.spill.reader.buffer.size",
    "value" : "spark.unsafe.sorter.spill.reader.buffer.size = 1048576b",
    "meta" : "default: 1048576b",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.cleaner.periodicGC.interval",
    "value" : "spark.cleaner.periodicGC.interval = 30min",
    "meta" : "default: 30min",
    "version" : "1.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.cleaner.referenceTracking",
    "value" : "spark.cleaner.referenceTracking = true",
    "meta" : "default: true",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.cleaner.referenceTracking.blocking",
    "value" : "spark.cleaner.referenceTracking.blocking = true",
    "meta" : "default: true",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.logs.rolling.strategy",
    "value" : "spark.executor.logs.rolling.strategy = ",
    "meta" : "default: ",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.logs.rolling.time.interval",
    "value" : "spark.executor.logs.rolling.time.interval = daily",
    "meta" : "default: daily",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.logs.rolling.maxSize",
    "value" : "spark.executor.logs.rolling.maxSize = 1048576",
    "meta" : "default: 1048576",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.logs.rolling.maxRetainedFiles",
    "value" : "spark.executor.logs.rolling.maxRetainedFiles = -1",
    "meta" : "default: -1",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.logs.rolling.enableCompression",
    "value" : "spark.executor.logs.rolling.enableCompression = false",
    "meta" : "default: false",
    "version" : "2.0.2",
    "docHTML" : ""
}, {
    "caption" : "spark.master.rest.enabled",
    "value" : "spark.master.rest.enabled = false",
    "meta" : "default: false",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.io.compression.snappy.blockSize",
    "value" : "spark.io.compression.snappy.blockSize = 32k",
    "meta" : "default: 32k",
    "version" : "1.4.0",
    "docHTML" : "Block size in bytes used in Snappy compression, in the case when Snappy compression codec is used. Lowering this block size will also lower shuffle memory usage when Snappy is used"
}, {
    "caption" : "spark.io.compression.lz4.blockSize",
    "value" : "spark.io.compression.lz4.blockSize = 32k",
    "meta" : "default: 32k",
    "version" : "1.4.0",
    "docHTML" : "Block size in bytes used in LZ4 compression, in the case when LZ4 compressioncodec is used. Lowering this block size will also lower shuffle memory usage when LZ4 is used."
}, {
    "caption" : "spark.io.compression.zstd.bufferSize",
    "value" : "spark.io.compression.zstd.bufferSize = 32k",
    "meta" : "default: 32k",
    "version" : "2.3.0",
    "docHTML" : "Buffer size in bytes used in Zstd compression, in the case when Zstd compression codec is used. Lowering this size will lower the shuffle memory usage when Zstd is used, but it might increase the compression cost because of excessive JNI call overhead"
}, {
    "caption" : "spark.io.compression.zstd.bufferPool.enabled",
    "value" : "spark.io.compression.zstd.bufferPool.enabled = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "If true, enable buffer pool of ZSTD JNI library."
}, {
    "caption" : "spark.io.compression.zstd.level",
    "value" : "spark.io.compression.zstd.level = 1",
    "meta" : "default: 1",
    "version" : "2.3.0",
    "docHTML" : "Compression level for Zstd compression codec. Increasing the compression level will result in better compression at the expense of more CPU and memory"
}, {
    "caption" : "spark.io.warning.largeFileThreshold",
    "value" : "spark.io.warning.largeFileThreshold = 1073741824b",
    "meta" : "default: 1073741824b",
    "version" : "3.0.0",
    "docHTML" : "If the size in bytes of a file loaded by Spark exceeds this threshold, a warning is logged with the possible reasons."
}, {
    "caption" : "spark.eventLog.compression.codec",
    "value" : "spark.eventLog.compression.codec = zstd",
    "meta" : "default: zstd",
    "version" : "3.0.0",
    "docHTML" : "The codec used to compress event log. By default, Spark provides four codecs: lz4, lzf, snappy, and zstd. You can also use fully qualified class names to specify the codec."
}, {
    "caption" : "spark.reducer.maxSizeInFlight",
    "value" : "spark.reducer.maxSizeInFlight = 48m",
    "meta" : "default: 48m",
    "version" : "1.4.0",
    "docHTML" : "Maximum size of map outputs to fetch simultaneously from each reduce task, in MiB unless otherwise specified. Since each output requires us to create a buffer to receive it, this represents a fixed memory overhead per reduce task, so keep it small unless you have a large amount of memory"
}, {
    "caption" : "spark.reducer.maxReqsInFlight",
    "value" : "spark.reducer.maxReqsInFlight = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.0.0",
    "docHTML" : "This configuration limits the number of remote requests to fetch blocks at any given point. When the number of hosts in the cluster increase, it might lead to very large number of inbound connections to one or more nodes, causing the workers to fail under load. By allowing it to limit the number of fetch requests, this scenario can be mitigated"
}, {
    "caption" : "spark.broadcast.UDFCompressionThreshold",
    "value" : "spark.broadcast.UDFCompressionThreshold = 1048576b",
    "meta" : "default: 1048576b",
    "version" : "3.0.0",
    "docHTML" : "The threshold at which user-defined functions (UDFs) and Python RDD commands are compressed by broadcast in bytes unless otherwise specified"
}, {
    "caption" : "spark.rdd.parallelListingThreshold",
    "value" : "spark.rdd.parallelListingThreshold = 10",
    "meta" : "default: 10",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.rdd.limit.scaleUpFactor",
    "value" : "spark.rdd.limit.scaleUpFactor = 4",
    "meta" : "default: 4",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.serializer.objectStreamReset",
    "value" : "spark.serializer.objectStreamReset = 100",
    "meta" : "default: 100",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.serializer.extraDebugInfo",
    "value" : "spark.serializer.extraDebugInfo = true",
    "meta" : "default: true",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.speculation.minTaskRuntime",
    "value" : "spark.speculation.minTaskRuntime = 100ms",
    "meta" : "default: 100ms",
    "version" : "3.2.0",
    "docHTML" : "Minimum amount of time a task runs before being considered for speculation. This can be used to avoid launching speculative copies of tasks that are very short."
}, {
    "caption" : "spark.executor.decommission.signal",
    "value" : "spark.executor.decommission.signal = PWR",
    "meta" : "default: PWR",
    "version" : "3.2.0",
    "docHTML" : "The signal that used to trigger the executor to start decommission."
}, {
    "caption" : "spark.scheduler.resource.profileMergeConflicts",
    "value" : "spark.scheduler.resource.profileMergeConflicts = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "If set to true, Spark will merge ResourceProfiles when different profiles are specified in RDDs that get combined into a single stage. When they are merged, Spark chooses the maximum of each resource and creates a new ResourceProfile. The default of false results in Spark throwing an exception if multiple different ResourceProfiles are found in RDDs going into the same stage."
}, {
    "caption" : "spark.standalone.submit.waitAppCompletion",
    "value" : "spark.standalone.submit.waitAppCompletion = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "In standalone cluster mode, controls whether the client waits to exit until the application completes. If set to true, the client process will stay alive polling the driver's status. Otherwise, the client process will exit after submission."
}, {
    "caption" : "spark.executor.allowSparkContext",
    "value" : "spark.executor.allowSparkContext = false",
    "meta" : "default: false",
    "version" : "3.0.1",
    "docHTML" : "If set to true, SparkContext can be created in executors."
}, {
    "caption" : "spark.executor.killOnFatalError.depth",
    "value" : "spark.executor.killOnFatalError.depth = 5",
    "meta" : "default: 5",
    "version" : "3.1.0",
    "docHTML" : "The max depth of the exception chain in a failed task Spark will search for a fatal error to check whether it should kill an executor. 0 means not checking any fatal error, 1 means checking only the exception but not the cause, and so on."
}, {
    "caption" : "spark.shuffle.push.enabled",
    "value" : "spark.shuffle.push.enabled = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "Set to true to enable push-based shuffle on the client side and this works in conjunction with the server side flag spark.shuffle.push.server.mergedShuffleFileManagerImpl which needs to be set with the appropriate org.apache.spark.network.shuffle.MergedShuffleFileManager implementation for push-based shuffle to be enabled"
}, {
    "caption" : "spark.shuffle.push.results.timeout",
    "value" : "spark.shuffle.push.results.timeout = 10s",
    "meta" : "default: 10s",
    "version" : "3.2.0",
    "docHTML" : "The maximum amount of time driver waits in seconds for the merge results to be received from all remote external shuffle services for a given shuffle. Driver submits following stages if not all results are received within the timeout. Setting this too long could potentially lead to performance regression"
}, {
    "caption" : "spark.shuffle.push.maxRetainedMergerLocations",
    "value" : "spark.shuffle.push.maxRetainedMergerLocations = 500",
    "meta" : "default: 500",
    "version" : "3.2.0",
    "docHTML" : "Maximum number of merger locations cached for push-based shuffle. Currently, merger locations are hosts of external shuffle services responsible for handling pushed blocks, merging them and serving merged blocks for later shuffle fetch."
}, {
    "caption" : "spark.shuffle.push.maxBlockSizeToPush",
    "value" : "spark.shuffle.push.maxBlockSizeToPush = 1m",
    "meta" : "default: 1m",
    "version" : "3.2.0",
    "docHTML" : "The max size of an individual block to push to the remote external shuffle services. Blocks larger than this threshold are not pushed to be merged remotely. These shuffle blocks will be fetched by the executors in the original manner."
}, {
    "caption" : "spark.shuffle.push.maxBlockBatchSize",
    "value" : "spark.shuffle.push.maxBlockBatchSize = 3m",
    "meta" : "default: 3m",
    "version" : "3.2.0",
    "docHTML" : "The max size of a batch of shuffle blocks to be grouped into a single push request."
}, {
    "caption" : "spark.shuffle.push.minCompletedPushRatio",
    "value" : "spark.shuffle.push.minCompletedPushRatio = 1.0",
    "meta" : "default: 1.0",
    "version" : "3.3.0",
    "docHTML" : "Fraction of map partitions that should be push complete before driver starts shuffle merge finalization during push based shuffle"
}, {
    "caption" : "spark.worker.executorStateSync.maxAttempts",
    "value" : "spark.worker.executorStateSync.maxAttempts = 5",
    "meta" : "default: 5",
    "version" : "3.3.0",
    "docHTML" : "The max attempts the worker will try to sync the ExecutorState to the Master, if the failed attempts reach the max attempts limit, the worker will give up and exit."
}, {
    "caption" : "spark.yarn.dist.pyFiles",
    "value" : "spark.yarn.dist.pyFiles = ",
    "version" : "2.2.1",
    "docHTML" : ""
}, {
    "caption" : "spark.files",
    "value" : "spark.files = ",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.archives",
    "value" : "spark.archives = ",
    "version" : "3.1.0",
    "docHTML" : "Comma-separated list of archives to be extracted into the working directory of each executor. .jar, .tar.gz, .tgz and .zip are supported. You can specify the directory name to unpack via adding '#' after the file name to unpack, for example, 'file.zip#directory'. This configuration is experimental."
}, {
    "caption" : "spark.jars",
    "value" : "spark.jars = ",
    "version" : "0.9.0",
    "docHTML" : ""
}, {
    "caption" : "spark.plugins",
    "value" : "spark.plugins = ",
    "version" : "3.0.0",
    "docHTML" : "Comma-separated list of class names implementing org.apache.spark.api.plugin.SparkPlugin to load into the application."
}, {
    "caption" : "spark.buffer.size",
    "value" : "spark.buffer.size = 65536",
    "meta" : "default: 65536",
    "version" : "0.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.driver.cores",
    "value" : "spark.driver.cores = 1",
    "meta" : "default: 1",
    "version" : "1.3.0",
    "docHTML" : "Number of cores to use for the driver process, only in cluster mode."
}, {
    "caption" : "spark.driver.memory",
    "value" : "spark.driver.memory = 1g",
    "meta" : "default: 1g",
    "version" : "1.1.1",
    "docHTML" : "Amount of memory to use for the driver process, in MiB unless otherwise specified."
}, {
    "caption" : "spark.driver.log.persistToDfs.enabled",
    "value" : "spark.driver.log.persistToDfs.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.driver.log.allowErasureCoding",
    "value" : "spark.driver.log.allowErasureCoding = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.enabled",
    "value" : "spark.eventLog.enabled = false",
    "meta" : "default: false",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.dir",
    "value" : "spark.eventLog.dir = /tmp/spark-events",
    "meta" : "default: /tmp/spark-events",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.compress",
    "value" : "spark.eventLog.compress = false",
    "meta" : "default: false",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.logBlockUpdates.enabled",
    "value" : "spark.eventLog.logBlockUpdates.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.erasureCoding.enabled",
    "value" : "spark.eventLog.erasureCoding.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.testing",
    "value" : "spark.eventLog.testing = false",
    "meta" : "default: false",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.overwrite",
    "value" : "spark.eventLog.overwrite = false",
    "meta" : "default: false",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.eventLog.rolling.enabled",
    "value" : "spark.eventLog.rolling.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether rolling over event log files is enabled. If set to true, it cuts down each event log file to the configured size."
}, {
    "caption" : "spark.executor.cores",
    "value" : "spark.executor.cores = 1",
    "meta" : "default: 1",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.executor.memory",
    "value" : "spark.executor.memory = 1g",
    "meta" : "default: 1g",
    "version" : "0.7.0",
    "docHTML" : "Amount of memory to use per executor process, in MiB unless otherwise specified."
}, {
    "caption" : "spark.memory.offHeap.enabled",
    "value" : "spark.memory.offHeap.enabled = false",
    "meta" : "default: false",
    "version" : "1.6.0",
    "docHTML" : "If true, Spark will attempt to use off-heap memory for certain operations. If off-heap memory use is enabled, then spark.memory.offHeap.size must be positive."
}, {
    "caption" : "spark.memory.offHeap.size",
    "value" : "spark.memory.offHeap.size = 0b",
    "meta" : "default: 0b",
    "version" : "1.6.0",
    "docHTML" : "The absolute amount of memory which can be used for off-heap allocation,  in bytes unless otherwise specified. This setting has no impact on heap memory usage, so if your executors' total memory consumption must fit within some hard limit then be sure to shrink your JVM heap size accordingly. This must be set to a positive value when spark.memory.offHeap.enabled=true."
}, {
    "caption" : "spark.memory.storageFraction",
    "value" : "spark.memory.storageFraction = 0.5",
    "meta" : "default: 0.5",
    "version" : "1.6.0",
    "docHTML" : "Amount of storage memory immune to eviction, expressed as a fraction of the size of the region set aside by spark.memory.fraction. The higher this is, the less working memory may be available to execution and tasks may spill to disk more often. Leaving this at the default value is recommended. "
}, {
    "caption" : "spark.memory.fraction",
    "value" : "spark.memory.fraction = 0.6",
    "meta" : "default: 0.6",
    "version" : "1.6.0",
    "docHTML" : "Fraction of (heap space - 300MB) used for execution and storage. The lower this is, the more frequently spills and cached data eviction occur. The purpose of this config is to set aside memory for internal metadata, user data structures, and imprecise size estimation in the case of sparse, unusually large records. Leaving this at the default value is recommended.  "
}, {
    "caption" : "spark.storage.cachedPeersTtl",
    "value" : "spark.storage.cachedPeersTtl = 60000",
    "meta" : "default: 60000",
    "version" : "1.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.yarn.isPython",
    "value" : "spark.yarn.isPython = false",
    "meta" : "default: false",
    "version" : "1.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.task.cpus",
    "value" : "spark.task.cpus = 1",
    "meta" : "default: 1",
    "version" : "0.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.enabled",
    "value" : "spark.dynamicAllocation.enabled = false",
    "meta" : "default: false",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.dynamicAllocation.testing",
    "value" : "spark.dynamicAllocation.testing = false",
    "meta" : "default: false",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.locality.wait",
    "value" : "spark.locality.wait = 3s",
    "meta" : "default: 3s",
    "version" : "0.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.service.enabled",
    "value" : "spark.shuffle.service.enabled = false",
    "meta" : "default: false",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.service.port",
    "value" : "spark.shuffle.service.port = 7337",
    "meta" : "default: 7337",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.service.name",
    "value" : "spark.shuffle.service.name = spark_shuffle",
    "meta" : "default: spark_shuffle",
    "version" : "3.2.0",
    "docHTML" : "The configured name of the Spark shuffle service the client should communicate with. This must match the name used to configure the Shuffle within the YARN NodeManager configuration (`yarn.nodemanager.aux-services`). Only takes effect when ConfigEntry(key=spark.shuffle.service.enabled, defaultValue=false, doc=, public=true, version=1.2.0) is set to true."
}, {
    "caption" : "spark.kerberos.relogin.period",
    "value" : "spark.kerberos.relogin.period = 1m",
    "meta" : "default: 1m",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.task.maxFailures",
    "value" : "spark.task.maxFailures = 4",
    "meta" : "default: 4",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.task.reaper.enabled",
    "value" : "spark.task.reaper.enabled = false",
    "meta" : "default: false",
    "version" : "2.0.3",
    "docHTML" : ""
}, {
    "caption" : "spark.task.reaper.killTimeout",
    "value" : "spark.task.reaper.killTimeout = -1ms",
    "meta" : "default: -1ms",
    "version" : "2.0.3",
    "docHTML" : ""
}, {
    "caption" : "spark.task.reaper.threadDump",
    "value" : "spark.task.reaper.threadDump = true",
    "meta" : "default: true",
    "version" : "2.0.3",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.application.maxFailedTasksPerExecutor",
    "value" : "spark.excludeOnFailure.application.maxFailedTasksPerExecutor = 2",
    "meta" : "default: 2",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.excludeOnFailure.application.maxFailedExecutorsPerNode",
    "value" : "spark.excludeOnFailure.application.maxFailedExecutorsPerNode = 2",
    "meta" : "default: 2",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.maxApplications",
    "value" : "spark.history.ui.maxApplications = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.io.encryption.enabled",
    "value" : "spark.io.encryption.enabled = false",
    "meta" : "default: false",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.driver.host",
    "value" : "spark.driver.host = 172.18.2.2",
    "meta" : "default: 172.18.2.2",
    "version" : "0.7.0",
    "docHTML" : "Address of driver endpoints."
}, {
    "caption" : "spark.driver.port",
    "value" : "spark.driver.port = 0",
    "meta" : "default: 0",
    "version" : "0.7.0",
    "docHTML" : "Port of driver endpoints."
}, {
    "caption" : "spark.driver.supervise",
    "value" : "spark.driver.supervise = false",
    "meta" : "default: false",
    "version" : "1.3.0",
    "docHTML" : "If true, restarts the driver automatically if it fails with a non-zero exit status. Only has effect in Spark standalone mode or Mesos cluster deploy mode."
}, {
    "caption" : "spark.driver.bindAddress",
    "version" : "2.1.0",
    "docHTML" : "Address where to bind network listen sockets on the driver."
}, {
    "caption" : "spark.blockManager.port",
    "value" : "spark.blockManager.port = 0",
    "meta" : "default: 0",
    "version" : "1.1.0",
    "docHTML" : "Port to use for the block manager when a more specific setting is not provided."
}, {
    "caption" : "spark.files.ignoreCorruptFiles",
    "value" : "spark.files.ignoreCorruptFiles = false",
    "meta" : "default: false",
    "version" : "2.1.0",
    "docHTML" : "Whether to ignore corrupt files. If true, the Spark jobs will continue to run when encountering corrupted or non-existing files and contents that have been read will still be returned."
}, {
    "caption" : "spark.files.ignoreMissingFiles",
    "value" : "spark.files.ignoreMissingFiles = false",
    "meta" : "default: false",
    "version" : "2.4.0",
    "docHTML" : "Whether to ignore missing files. If true, the Spark jobs will continue to run when encountering missing files and the contents that have been read will still be returned."
}, {
    "caption" : "spark.files.openCostInBytes",
    "value" : "spark.files.openCostInBytes = 4194304b",
    "meta" : "default: 4194304b",
    "version" : "2.1.0",
    "docHTML" : "The estimated cost to open a file, measured by the number of bytes could be scanned in the same time. This is used when putting multiple files into a partition. It's better to over estimate, then the partitions with small files will be faster than partitions with bigger files."
}, {
    "caption" : "spark.redaction.regex",
    "value" : "spark.redaction.regex = ",
    "version" : "2.1.2",
    "docHTML" : "Regex to decide which Spark configuration properties and environment variables in driver and executor environments contain sensitive information. When this regex matches a property key or value, the value is redacted from the environment UI and various logs like YARN and event logs."
}, {
    "caption" : "spark.authenticate.secretBitLength",
    "value" : "spark.authenticate.secretBitLength = 256",
    "meta" : "default: 256",
    "version" : "1.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.authenticate",
    "value" : "spark.authenticate = false",
    "meta" : "default: false",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.authenticate.enableSaslEncryption",
    "value" : "spark.authenticate.enableSaslEncryption = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.authenticate.secret.driver.file",
    "version" : "3.0.0",
    "docHTML" : "Path to a file that contains the authentication secret to use. Loaded by the driver. In Kubernetes client mode it is often useful to set a different secret path for the driver vs. the executors, since the driver may not be running in a pod unlike the executors. If this is set, an accompanying secret file must be specified for the executors. The fallback configuration allows the same path to be used for both the driver and the executors when running in cluster mode. File-based secret keys are only allowed when using Kubernetes."
}, {
    "caption" : "spark.buffer.write.chunkSize",
    "value" : "spark.buffer.write.chunkSize = 67108864b",
    "meta" : "default: 67108864b",
    "version" : "2.3.0",
    "docHTML" : "The chunk size in bytes during writing out the bytes of ChunkedByteBuffer."
}, {
    "caption" : "spark.checkpoint.compress",
    "value" : "spark.checkpoint.compress = false",
    "meta" : "default: false",
    "version" : "2.2.0",
    "docHTML" : "Whether to compress RDD checkpoints. Generally a good idea. Compression will use spark.io.compression.codec."
}, {
    "caption" : "spark.shuffle.sort.io.plugin.class",
    "value" : "spark.shuffle.sort.io.plugin.class = org.apache.spark.shuffle.sort.io.LocalDiskShuffleDataIO",
    "meta" : "default: org.apache.spark.shuffle.sort.io.LocalDiskShuffleDataIO",
    "version" : "3.0.0",
    "docHTML" : "Name of the class to use for shuffle IO."
}, {
    "caption" : "spark.shuffle.file.buffer",
    "value" : "spark.shuffle.file.buffer = 32k",
    "meta" : "default: 32k",
    "version" : "1.4.0",
    "docHTML" : "Size of the in-memory buffer for each shuffle file output stream, in KiB unless otherwise specified. These buffers reduce the number of disk seeks and system calls made in creating intermediate shuffle files."
}, {
    "caption" : "spark.yarn.dist.forceDownloadSchemes",
    "value" : "spark.yarn.dist.forceDownloadSchemes = ",
    "version" : "2.3.0",
    "docHTML" : "Comma-separated list of schemes for which resources will be downloaded to the local disk prior to being added to YARN's distributed cache. For use in cases where the YARN service does not support schemes that are supported by Spark, like http, https and ftp, or jars required to be in the local YARN client's classpath. Wildcard '*' is denoted to download resources for all the schemes."
}, {
    "caption" : "spark.driver.maxResultSize",
    "value" : "spark.driver.maxResultSize = 1g",
    "meta" : "default: 1g",
    "version" : "1.2.0",
    "docHTML" : "Size limit for results."
}, {
    "caption" : "spark.shuffle.checksum.enabled",
    "value" : "spark.shuffle.checksum.enabled = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "Whether to calculate the checksum of shuffle data. If enabled, Spark will calculate the checksum values for each partition data within the map output file and store the values in a checksum file on the disk. When there's shuffle data corruption detected, Spark will try to diagnose the cause (e.g., network issue, disk issue, etc.) of the corruption by using the checksum file."
}, {
    "caption" : "spark.shuffle.compress",
    "value" : "spark.shuffle.compress = true",
    "meta" : "default: true",
    "version" : "0.6.0",
    "docHTML" : "Whether to compress shuffle output. Compression will use spark.io.compression.codec."
}, {
    "caption" : "spark.shuffle.spill.compress",
    "value" : "spark.shuffle.spill.compress = true",
    "meta" : "default: true",
    "version" : "0.9.0",
    "docHTML" : "Whether to compress data spilled during shuffles. Compression will use spark.io.compression.codec."
}, {
    "caption" : "spark.shuffle.spill.batchSize",
    "value" : "spark.shuffle.spill.batchSize = 10000",
    "meta" : "default: 10000",
    "version" : "0.9.0",
    "docHTML" : "Size of object batches when reading/writing from serializers."
}, {
    "caption" : "spark.shuffle.manager",
    "value" : "spark.shuffle.manager = sort",
    "meta" : "default: sort",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.shuffle.detectCorrupt",
    "value" : "spark.shuffle.detectCorrupt = true",
    "meta" : "default: true",
    "version" : "2.2.0",
    "docHTML" : "Whether to detect any corruption in fetched blocks."
}, {
    "caption" : "spark.shuffle.sync",
    "value" : "spark.shuffle.sync = false",
    "meta" : "default: false",
    "version" : "0.8.0",
    "docHTML" : "Whether to force outstanding writes to disk."
}, {
    "caption" : "spark.barrier.sync.timeout",
    "value" : "spark.barrier.sync.timeout = 365d",
    "meta" : "default: 365d",
    "version" : "2.4.0",
    "docHTML" : "The timeout in seconds for each barrier() call from a barrier task. If the coordinator didn't receive all the sync messages from barrier tasks within the configured time, throw a SparkException to fail all the tasks. The default value is set to 31536000(3600 * 24 * 365) so the barrier() call shall wait for one year."
}, {
    "caption" : "spark.master.rest.port",
    "value" : "spark.master.rest.port = 6066",
    "meta" : "default: 6066",
    "version" : "1.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.master.ui.port",
    "value" : "spark.master.ui.port = 8080",
    "meta" : "default: 8080",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.io.compression.codec",
    "value" : "spark.io.compression.codec = lz4",
    "meta" : "default: lz4",
    "version" : "0.8.0",
    "docHTML" : "The codec used to compress internal data such as RDD partitions, event log, broadcast variables and shuffle outputs. By default, Spark provides four codecs: lz4, lzf, snappy, and zstd. You can also use fully qualified class names to specify the codec"
}, {
    "caption" : "spark.locality.wait.process",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.locality.wait.node",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.locality.wait.rack",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.broadcast.compress",
    "value" : "spark.broadcast.compress = true",
    "meta" : "default: true",
    "version" : "0.6.0",
    "docHTML" : "Whether to compress broadcast variables before sending them. Generally a good idea. Compression will use spark.io.compression.codec"
}, {
    "caption" : "spark.broadcast.blockSize",
    "value" : "spark.broadcast.blockSize = 4m",
    "meta" : "default: 4m",
    "version" : "0.5.0",
    "docHTML" : "Size of each piece of a block for TorrentBroadcastFactory, in KiB unless otherwise specified. Too large a value decreases parallelism during broadcast (makes it slower); however, if it is too small, BlockManager might take a performance hit"
}, {
    "caption" : "spark.broadcast.checksum",
    "value" : "spark.broadcast.checksum = true",
    "meta" : "default: true",
    "version" : "2.1.1",
    "docHTML" : "Whether to enable checksum for broadcast. If enabled, broadcasts will include a checksum, which can help detect corrupted blocks, at the cost of computing and sending a little more data. It's possible to disable it if the network has other mechanisms to guarantee data won't be corrupted during broadcast"
}, {
    "caption" : "spark.rdd.compress",
    "value" : "spark.rdd.compress = false",
    "meta" : "default: false",
    "version" : "0.6.0",
    "docHTML" : "Whether to compress serialized RDD partitions (e.g. for StorageLevel.MEMORY_ONLY_SER in Scala or StorageLevel.MEMORY_ONLY in Python). Can save substantial space at the cost of some extra CPU time. Compression will use spark.io.compression.codec"
}, {
    "caption" : "spark.serializer",
    "value" : "spark.serializer = org.apache.spark.serializer.JavaSerializer",
    "meta" : "default: org.apache.spark.serializer.JavaSerializer",
    "version" : "0.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.submit.deployMode",
    "value" : "spark.submit.deployMode = client",
    "meta" : "default: client",
    "version" : "1.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.submit.pyFiles",
    "value" : "spark.submit.pyFiles = ",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.scheduler.mode",
    "value" : "spark.scheduler.mode = FIFO",
    "meta" : "default: FIFO",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.speculation",
    "value" : "spark.speculation = false",
    "meta" : "default: false",
    "version" : "0.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.speculation.interval",
    "value" : "spark.speculation.interval = 100ms",
    "meta" : "default: 100ms",
    "version" : "0.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.speculation.multiplier",
    "value" : "spark.speculation.multiplier = 1.5",
    "meta" : "default: 1.5",
    "version" : "0.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.speculation.quantile",
    "value" : "spark.speculation.quantile = 0.75",
    "meta" : "default: 0.75",
    "version" : "0.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.decommission.enabled",
    "value" : "spark.decommission.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When decommission enabled, Spark will try its best to shutdown the executor gracefully. Spark will try to migrate all the RDD blocks (controlled by spark.storage.decommission.rddBlocks.enabled) and shuffle blocks (controlled by spark.storage.decommission.shuffleBlocks.enabled) from the decommissioning executor to a remote executor when spark.storage.decommission.enabled is enabled. With decommission enabled, Spark will also decommission an executor instead of killing when spark.dynamicAllocation.enabled enabled."
}, {
    "caption" : "spark.jars.packages",
    "value" : "spark.jars.packages = ",
    "version" : "1.5.0",
    "docHTML" : "Comma-separated list of Maven coordinates of jars to include on the driver and executor classpaths. The coordinates should be groupId:artifactId:version. If spark.jars.ivySettings is given artifacts will be resolved according to the configuration in the file, otherwise artifacts will be searched for in the local maven repo, then maven central and finally any additional remote repositories given by the command-line option --repositories. For more details, see Advanced Dependency Management."
}, {
    "caption" : "spark.jars.excludes",
    "value" : "spark.jars.excludes = ",
    "version" : "1.5.0",
    "docHTML" : "Comma-separated list of groupId:artifactId, to exclude while resolving the dependencies provided in spark.jars.packages to avoid dependency conflicts."
}, {
    "caption" : "spark.jars.repositories",
    "value" : "spark.jars.repositories = ",
    "version" : "2.3.0",
    "docHTML" : "Comma-separated list of additional remote repositories to search for the maven coordinates given with --packages or spark.jars.packages."
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.applicationSideScanSizeThreshold",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.applicationSideScanSizeThreshold = 10GB",
    "meta" : "default: 10GB",
    "version" : "3.3.0",
    "docHTML" : "Byte size threshold of the Bloom filter application side plan's aggregated scan size. Aggregated scan byte size of the Bloom filter application side needs to be over this value to inject a bloom filter."
}, {
    "caption" : "spark.sql.streaming.sessionWindow.merge.sessions.in.local.partition",
    "value" : "spark.sql.streaming.sessionWindow.merge.sessions.in.local.partition = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When true, streaming session window sorts and merge sessions in local partition prior to shuffle. This is to reduce the rows to shuffle, but only beneficial when there're lots of rows in a batch being assigned to same sessions."
}, {
    "caption" : "spark.sql.optimizer.dynamicPartitionPruning.fallbackFilterRatio",
    "value" : "spark.sql.optimizer.dynamicPartitionPruning.fallbackFilterRatio = 0.5",
    "meta" : "default: 0.5",
    "version" : "3.0.0",
    "docHTML" : "When statistics are not available or configured not to be used, this config will be used as the fallback filter ratio for computing the data size of the partitioned table after dynamic partition pruning, in order to evaluate if it is worth adding an extra subquery as the pruning filter if broadcast reuse is not applicable."
}, {
    "caption" : "spark.sql.optimizer.dynamicPartitionPruning.reuseBroadcastOnly",
    "value" : "spark.sql.optimizer.dynamicPartitionPruning.reuseBroadcastOnly = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, dynamic partition pruning will only apply when the broadcast exchange of a broadcast hash join operation can be reused as the dynamic pruning filter."
}, {
    "caption" : "spark.sql.optimizer.runtimeFilter.semiJoinReduction.enabled",
    "value" : "spark.sql.optimizer.runtimeFilter.semiJoinReduction.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true and if one side of a shuffle join has a selective predicate, we attempt to insert a semi join in the other side to reduce the amount of shuffle data."
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.creationSideThreshold",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.creationSideThreshold = 10MB",
    "meta" : "default: 10MB",
    "version" : "3.3.0",
    "docHTML" : "Size threshold of the bloom filter creation side plan. Estimated size needs to be under this value to try to inject bloom filter."
}, {
    "caption" : "spark.sql.requireAllClusterKeysForCoPartition",
    "value" : "spark.sql.requireAllClusterKeysForCoPartition = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, the planner requires all the clustering keys as the hash partition keys of the children, to eliminate the shuffles for the operator that needs its children to be co-partitioned, such as JOIN node. This is to avoid data skews which can lead to significant performance regression if shuffles are eliminated."
}, {
    "caption" : "spark.sql.requireAllClusterKeysForDistribution",
    "value" : "spark.sql.requireAllClusterKeysForDistribution = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, the planner requires all the clustering keys as the partition keys (with same ordering) of the children, to eliminate the shuffle for the operator that requires its children be clustered distributed, such as AGGREGATE and WINDOW node. This is to avoid data skews which can lead to significant performance regression if shuffle is eliminated."
}, {
    "caption" : "spark.sql.adaptive.nonEmptyPartitionRatioForBroadcastJoin",
    "value" : "spark.sql.adaptive.nonEmptyPartitionRatioForBroadcastJoin = 0.2",
    "meta" : "default: 0.2",
    "version" : "3.0.0",
    "docHTML" : "The relation with a non-empty partition ratio lower than this config will not be considered as the build side of a broadcast-hash join in adaptive execution regardless of its size.This configuration only has an effect when 'spark.sql.adaptive.enabled' is true."
}, {
    "caption" : "spark.sql.adaptive.maxShuffledHashJoinLocalMapThreshold",
    "value" : "spark.sql.adaptive.maxShuffledHashJoinLocalMapThreshold = 0b",
    "meta" : "default: 0b",
    "version" : "3.2.0",
    "docHTML" : "Configures the maximum size in bytes per partition that can be allowed to build local hash map. If this value is not smaller than spark.sql.adaptive.advisoryPartitionSizeInBytes and all the partition size are not larger than this config, join selection prefer to use shuffled hash join instead of sort merge join regardless of the value of spark.sql.join.preferSortMergeJoin."
}, {
    "caption" : "spark.sql.adaptive.optimizeSkewsInRebalancePartitions.enabled",
    "value" : "spark.sql.adaptive.optimizeSkewsInRebalancePartitions.enabled = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "When true and 'spark.sql.adaptive.enabled' is true, Spark will optimize the skewed shuffle partitions in RebalancePartitions and split them to smaller ones according to the target size (specified by 'spark.sql.adaptive.advisoryPartitionSizeInBytes'), to avoid data skew."
}, {
    "caption" : "spark.sql.adaptive.rebalancePartitionsSmallPartitionFactor",
    "value" : "spark.sql.adaptive.rebalancePartitionsSmallPartitionFactor = 0.2",
    "meta" : "default: 0.2",
    "version" : "3.3.0",
    "docHTML" : "A partition will be merged during splitting if its size is small than this factor multiply spark.sql.adaptive.advisoryPartitionSizeInBytes."
}, {
    "caption" : "spark.sql.subexpressionElimination.cache.maxEntries",
    "value" : "spark.sql.subexpressionElimination.cache.maxEntries = 100",
    "meta" : "default: 100",
    "version" : "3.1.0",
    "docHTML" : "The maximum entries of the cache used for interpreted subexpression elimination."
}, {
    "caption" : "spark.sql.parquet.filterPushdown.timestamp",
    "value" : "spark.sql.parquet.filterPushdown.timestamp = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If true, enables Parquet filter push-down optimization for Timestamp. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled and Timestamp stored as TIMESTAMP_MICROS or TIMESTAMP_MILLIS type."
}, {
    "caption" : "spark.sql.parquet.filterPushdown.string.startsWith",
    "value" : "spark.sql.parquet.filterPushdown.string.startsWith = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If true, enables Parquet filter push-down optimization for string startsWith function. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled."
}, {
    "caption" : "spark.sql.parquet.pushdown.inFilterThreshold",
    "value" : "spark.sql.parquet.pushdown.inFilterThreshold = 10",
    "meta" : "default: 10",
    "version" : "2.4.0",
    "docHTML" : "For IN predicate, Parquet filter will push-down a set of OR clauses if its number of values not exceeds this threshold. Otherwise, Parquet filter will push-down a value greater than or equal to its minimum value and less than or equal to its maximum value. By setting this value to 0 this feature can be disabled. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled."
}, {
    "caption" : "spark.sql.parquet.enableNestedColumnVectorizedReader",
    "value" : "spark.sql.parquet.enableNestedColumnVectorizedReader = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Enables vectorized Parquet decoding for nested columns (e.g., struct, list, map). Requires spark.sql.parquet.enableVectorizedReader to be enabled."
}, {
    "caption" : "spark.sql.orc.enableNestedColumnVectorizedReader",
    "value" : "spark.sql.orc.enableNestedColumnVectorizedReader = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "Enables vectorized orc decoding for nested column."
}, {
    "caption" : "spark.sql.hive.metastorePartitionPruningInSetThreshold",
    "value" : "spark.sql.hive.metastorePartitionPruningInSetThreshold = 1000",
    "meta" : "default: 1000",
    "version" : "3.1.0",
    "docHTML" : "The threshold of set size for InSet predicate when pruning partitions through Hive Metastore. When the set size exceeds the threshold, we rewrite the InSet predicate to be greater than or equal to the minimum value in set and less than or equal to the maximum value in set. Larger values may cause Hive Metastore stack overflow. But for InSet inside Not with values exceeding the threshold, we won't push it to Hive Metastore."
}, {
    "caption" : "spark.sql.hive.metastorePartitionPruningFallbackOnException",
    "value" : "spark.sql.hive.metastorePartitionPruningFallbackOnException = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Whether to fallback to get all partitions from Hive metastore and perform partition pruning on Spark client side, when encountering MetaException from the metastore. Note that Spark query performance may degrade if this is enabled and there are many partitions to be listed. If this is disabled, Spark will fail the query instead."
}, {
    "caption" : "spark.sql.hive.metastorePartitionPruningFastFallback",
    "value" : "spark.sql.hive.metastorePartitionPruningFastFallback = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When this config is enabled, if the predicates are not supported by Hive or Spark does fallback due to encountering MetaException from the metastore, Spark will instead prune partitions by getting the partition names first and then evaluating the filter expressions on the client side. Note that the predicates with TimeZoneAwareExpression is not supported."
}, {
    "caption" : "spark.sql.hive.filesourcePartitionFileCacheSize",
    "value" : "spark.sql.hive.filesourcePartitionFileCacheSize = 262144000",
    "meta" : "default: 262144000",
    "version" : "2.1.1",
    "docHTML" : "When nonzero, enable caching of partition file metadata in memory. All tables share a cache that can use up to specified num bytes for file metadata. This conf only has an effect when hive filesource partition management is enabled."
}, {
    "caption" : "spark.sql.optimizer.canChangeCachedPlanOutputPartitioning",
    "value" : "spark.sql.optimizer.canChangeCachedPlanOutputPartitioning = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "Whether to forcibly enable some optimization rules that can change the output partitioning of a cached query when executing it for caching. If it is set to true, queries may need an extra shuffle to read the cached data. This configuration is disabled by default. Currently, the optimization rules enabled by this configuration are spark.sql.adaptive.enabled and spark.sql.sources.bucketing.autoBucketedScan.enabled."
}, {
    "caption" : "spark.sql.selfJoinAutoResolveAmbiguity",
    "value" : "spark.sql.selfJoinAutoResolveAmbiguity = true",
    "meta" : "default: true",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.sql.codegen.splitConsumeFuncByOperator",
    "value" : "spark.sql.codegen.splitConsumeFuncByOperator = true",
    "meta" : "default: true",
    "version" : "2.3.1",
    "docHTML" : "When true, whole stage codegen would put the logic of consuming rows of each physical operator into individual methods, instead of a single big method. This can be used to avoid oversized function that can miss the opportunity of JIT optimization."
}, {
    "caption" : "spark.sql.streaming.flatMapGroupsWithState.stateFormatVersion",
    "value" : "spark.sql.streaming.flatMapGroupsWithState.stateFormatVersion = 2",
    "meta" : "default: 2",
    "version" : "2.4.0",
    "docHTML" : "State format version used by flatMapGroupsWithState operation in a streaming query"
}, {
    "caption" : "spark.sql.streaming.aggregation.stateFormatVersion",
    "value" : "spark.sql.streaming.aggregation.stateFormatVersion = 2",
    "meta" : "default: 2",
    "version" : "2.4.0",
    "docHTML" : "State format version used by streaming aggregation operations in a streaming query. State between versions are tend to be incompatible, so state format version shouldn't be modified after running."
}, {
    "caption" : "spark.sql.streaming.sessionWindow.stateFormatVersion",
    "value" : "spark.sql.streaming.sessionWindow.stateFormatVersion = 1",
    "meta" : "default: 1",
    "version" : "3.2.0",
    "docHTML" : "State format version used by streaming session window in a streaming query. State between versions are tend to be incompatible, so state format version shouldn't be modified after running."
}, {
    "caption" : "spark.sql.streaming.statefulOperator.checkCorrectness.enabled",
    "value" : "spark.sql.streaming.statefulOperator.checkCorrectness.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, the stateful operators for streaming query will be checked for possible correctness issue due to global watermark. The correctness issue comes from queries containing stateful operation which can emit rows older than the current watermark plus allowed late record delay, which are \"late rows\" in downstream stateful operations and these rows can be discarded. Please refer the programming guide doc for more details. Once the issue is detected, Spark will throw analysis exception. When this config is disabled, Spark will just print warning message for users. Prior to Spark 3.1.0, the behavior is disabling this config."
}, {
    "caption" : "spark.sql.streaming.statefulOperator.useStrictDistribution",
    "value" : "spark.sql.streaming.statefulOperator.useStrictDistribution = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "The purpose of this config is only compatibility; DO NOT MANUALLY CHANGE THIS!!! When true, the stateful operator for streaming query will use StatefulOpClusteredDistribution which guarantees stable state partitioning as long as the operator provides consistent grouping keys across the lifetime of query. When false, the stateful operator for streaming query will use ClusteredDistribution which is not sufficient to guarantee stable state partitioning despite the operator provides consistent grouping keys across the lifetime of query. This config will be set to true for new streaming queries to guarantee stable state partitioning, and set to false for existing streaming queries to not break queries which are restored from existing checkpoints. Please refer SPARK-38204 for details."
}, {
    "caption" : "spark.sql.streaming.stateStore.skipNullsForStreamStreamJoins.enabled",
    "value" : "spark.sql.streaming.stateStore.skipNullsForStreamStreamJoins.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, this config will skip null values in hash based stream-stream joins."
}, {
    "caption" : "spark.sql.codegen.join.fullOuterShuffledHashJoin.enabled",
    "value" : "spark.sql.codegen.join.fullOuterShuffledHashJoin.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, enable code-gen for FULL OUTER shuffled hash join."
}, {
    "caption" : "spark.sql.codegen.join.fullOuterSortMergeJoin.enabled",
    "value" : "spark.sql.codegen.join.fullOuterSortMergeJoin.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, enable code-gen for FULL OUTER sort merge join."
}, {
    "caption" : "spark.sql.legacy.allowStarWithSingleTableIdentifierInCount",
    "value" : "spark.sql.legacy.allowStarWithSingleTableIdentifierInCount = false",
    "meta" : "default: false",
    "version" : "3.2",
    "docHTML" : "When true, the SQL function 'count' is allowed to take single 'tblName.*' as parameter"
}, {
    "caption" : "spark.sql.streaming.noDataProgressEventInterval",
    "value" : "spark.sql.streaming.noDataProgressEventInterval = 10000ms",
    "meta" : "default: 10000ms",
    "version" : "2.1.1",
    "docHTML" : "How long to wait between two progress events when there is no data"
}, {
    "caption" : "spark.sql.streaming.checkpoint.escapedPathCheck.enabled",
    "value" : "spark.sql.streaming.checkpoint.escapedPathCheck.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to detect a streaming query may pick up an incorrect checkpoint path due to SPARK-26824."
}, {
    "caption" : "spark.sql.statistics.parallelFileListingInStatsComputation.enabled",
    "value" : "spark.sql.statistics.parallelFileListingInStatsComputation.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.1",
    "docHTML" : "When true, SQL commands use parallel file listing, as opposed to single thread listing. This usually speeds up commands that need to list many directories."
}, {
    "caption" : "spark.sql.sessionWindow.buffer.in.memory.threshold",
    "value" : "spark.sql.sessionWindow.buffer.in.memory.threshold = 4096",
    "meta" : "default: 4096",
    "version" : "3.2.0",
    "docHTML" : "Threshold for number of windows guaranteed to be held in memory by the session window operator. Note that the buffer is used only for the query Spark cannot apply aggregations on determining session window."
}, {
    "caption" : "spark.sql.sortMergeJoinExec.buffer.in.memory.threshold",
    "value" : "spark.sql.sortMergeJoinExec.buffer.in.memory.threshold = 2147483632",
    "meta" : "default: 2147483632",
    "version" : "2.2.1",
    "docHTML" : "Threshold for number of rows guaranteed to be held in memory by the sort merge join operator"
}, {
    "caption" : "spark.sql.sortMergeJoinExec.buffer.spill.threshold",
    "value" : "spark.sql.sortMergeJoinExec.buffer.spill.threshold = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.2.0",
    "docHTML" : "Threshold for number of rows to be spilled by sort merge join operator"
}, {
    "caption" : "spark.sql.cartesianProductExec.buffer.in.memory.threshold",
    "value" : "spark.sql.cartesianProductExec.buffer.in.memory.threshold = 4096",
    "meta" : "default: 4096",
    "version" : "2.2.1",
    "docHTML" : "Threshold for number of rows guaranteed to be held in memory by the cartesian product operator"
}, {
    "caption" : "spark.sql.cartesianProductExec.buffer.spill.threshold",
    "value" : "spark.sql.cartesianProductExec.buffer.spill.threshold = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.2.0",
    "docHTML" : "Threshold for number of rows to be spilled by cartesian product operator"
}, {
    "caption" : "spark.sql.legacy.execution.pandas.groupedMap.assignColumnsByName",
    "value" : "spark.sql.legacy.execution.pandas.groupedMap.assignColumnsByName = true",
    "meta" : "default: true",
    "version" : "2.4.1",
    "docHTML" : "When true, columns will be looked up by name if labeled with a string and fallback to use position if not. When false, a grouped map Pandas UDF will assign columns from the returned Pandas DataFrame based on position, regardless of column label type. This configuration will be deprecated in future releases."
}, {
    "caption" : "spark.sql.streaming.continuous.epochBacklogQueueSize",
    "value" : "spark.sql.streaming.continuous.epochBacklogQueueSize = 10000",
    "meta" : "default: 10000",
    "version" : "3.0.0",
    "docHTML" : "The max number of entries to be stored in queue to wait for late epochs. If this parameter is exceeded by the size of the queue, stream will stop with an error."
}, {
    "caption" : "spark.sql.streaming.continuous.executorPollIntervalMs",
    "value" : "spark.sql.streaming.continuous.executorPollIntervalMs = 100ms",
    "meta" : "default: 100ms",
    "version" : "2.3.0",
    "docHTML" : "The interval at which continuous execution readers will poll to check whether the epoch has advanced on the driver."
}, {
    "caption" : "spark.sql.optimizer.nestedPredicatePushdown.supportedFileSources",
    "value" : "spark.sql.optimizer.nestedPredicatePushdown.supportedFileSources = parquet,orc",
    "meta" : "default: parquet,orc",
    "version" : "3.0.0",
    "docHTML" : "A comma-separated list of data source short names or fully qualified data source implementation class names for which Spark tries to push down predicates for nested columns and/or names containing `dots` to data sources. This configuration is only effective with file-based data sources in DSv1. Currently, Parquet and ORC implement both optimizations. The other data sources don't support this feature yet. So the default value is 'parquet,orc'."
}, {
    "caption" : "spark.sql.legacy.respectNullabilityInTextDatasetConversion",
    "value" : "spark.sql.legacy.respectNullabilityInTextDatasetConversion = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, the nullability in the user-specified schema for `DataFrameReader.schema(schema).json(jsonDataset)` and `DataFrameReader.schema(schema).csv(csvDataset)` is respected. Otherwise, they are turned to a nullable schema forcibly."
}, {
    "caption" : "spark.sql.codegen.aggregate.fastHashMap.capacityBit",
    "value" : "spark.sql.codegen.aggregate.fastHashMap.capacityBit = 16",
    "meta" : "default: 16",
    "version" : "2.4.0",
    "docHTML" : "Capacity for the max number of rows to be held in memory by the fast hash aggregate product operator. The bit is not for actual value, but the actual numBuckets is determined by loadFactor (e.g: default bit value 16 , the actual numBuckets is ((1 << 16) / 0.5)."
}, {
    "caption" : "spark.sql.legacy.parseNullPartitionSpecAsStringLiteral",
    "value" : "spark.sql.legacy.parseNullPartitionSpecAsStringLiteral = false",
    "meta" : "default: false",
    "version" : "3.0.2",
    "docHTML" : "If it is set to true, `PARTITION(col=null)` is parsed as a string literal of its text representation, e.g., string 'null', when the partition column is string type. Otherwise, it is always parsed as a null literal in the partition spec."
}, {
    "caption" : "spark.sql.legacy.replaceDatabricksSparkAvro.enabled",
    "value" : "spark.sql.legacy.replaceDatabricksSparkAvro.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If it is set to true, the data source provider com.databricks.spark.avro is mapped to the built-in but external Avro data source module for backward compatibility."
}, {
    "caption" : "spark.sql.legacy.exponentLiteralAsDecimal.enabled",
    "value" : "spark.sql.legacy.exponentLiteralAsDecimal.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, a literal with an exponent (e.g. 1E-30) would be parsed as Decimal rather than Double."
}, {
    "caption" : "spark.sql.legacy.allowNegativeScaleOfDecimal",
    "value" : "spark.sql.legacy.allowNegativeScaleOfDecimal = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, negative scale of Decimal type is allowed. For example, the type of number 1E10BD under legacy mode is DecimalType(2, -9), but is Decimal(11, 0) in non legacy mode."
}, {
    "caption" : "spark.sql.legacy.bucketedTableScan.outputOrdering",
    "value" : "spark.sql.legacy.bucketedTableScan.outputOrdering = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, the bucketed table scan will list files during planning to figure out the output ordering, which is expensive and may make the planning quite slow."
}, {
    "caption" : "spark.sql.legacy.createEmptyCollectionUsingStringType",
    "value" : "spark.sql.legacy.createEmptyCollectionUsingStringType = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, Spark returns an empty collection with `StringType` as element type if the `array`/`map` function is called without any parameters. Otherwise, Spark returns an empty collection with `NullType` as element type."
}, {
    "caption" : "spark.sql.legacy.followThreeValuedLogicInArrayExists",
    "value" : "spark.sql.legacy.followThreeValuedLogicInArrayExists = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, the ArrayExists will follow the three-valued boolean logic."
}, {
    "caption" : "spark.sql.legacy.mssqlserver.numericMapping.enabled",
    "value" : "spark.sql.legacy.mssqlserver.numericMapping.enabled = false",
    "meta" : "default: false",
    "version" : "2.4.5",
    "docHTML" : "When true, use legacy MySqlServer SMALLINT and REAL type mapping."
}, {
    "caption" : "spark.sql.legacy.groupingIdWithAppendedUserGroupBy",
    "value" : "spark.sql.legacy.groupingIdWithAppendedUserGroupBy = false",
    "meta" : "default: false",
    "version" : "3.2.3",
    "docHTML" : "When true, grouping_id() returns values based on grouping set columns plus user-given group-by expressions order like Spark 3.2.0, 3.2.1, 3.2.2, and 3.3.0."
}, {
    "caption" : "spark.sql.bucketing.coalesceBucketsInJoin.maxBucketRatio",
    "value" : "spark.sql.bucketing.coalesceBucketsInJoin.maxBucketRatio = 4",
    "meta" : "default: 4",
    "version" : "3.1.0",
    "docHTML" : "The ratio of the number of two buckets being coalesced should be less than or equal to this value for bucket coalescing to be applied. This configuration only has an effect when 'spark.sql.bucketing.coalesceBucketsInJoin.enabled' is set to true."
}, {
    "caption" : "spark.sql.execution.broadcastHashJoin.outputPartitioningExpandLimit",
    "value" : "spark.sql.execution.broadcastHashJoin.outputPartitioningExpandLimit = 8",
    "meta" : "default: 8",
    "version" : "3.1.0",
    "docHTML" : "The maximum number of partitionings that a HashPartitioning can be expanded to. This configuration is applicable only for BroadcastHashJoin inner joins and can be set to '0' to disable this feature."
}, {
    "caption" : "spark.sql.legacy.nullValueWrittenAsQuotedEmptyStringCsv",
    "value" : "spark.sql.legacy.nullValueWrittenAsQuotedEmptyStringCsv = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When set to false, nulls are written as unquoted empty strings in CSV data source. If set to true, it restores the legacy behavior that nulls were written as quoted empty strings, `\"\"`."
}, {
    "caption" : "spark.sql.legacy.allowNullComparisonResultInArraySort",
    "value" : "spark.sql.legacy.allowNullComparisonResultInArraySort = false",
    "meta" : "default: false",
    "version" : "3.2.2",
    "docHTML" : "When set to false, `array_sort` function throws an error if the comparator function returns null. If set to true, it restores the legacy behavior that handles null as zero (equal)."
}, {
    "caption" : "spark.sql.files.maxPartitionBytes",
    "value" : "spark.sql.files.maxPartitionBytes = 128MB",
    "meta" : "default: 128MB",
    "version" : "2.0.0",
    "docHTML" : "The maximum number of bytes to pack into a single partition when reading files. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC."
}, {
    "caption" : "spark.sql.optimizer.inSetConversionThreshold",
    "value" : "spark.sql.optimizer.inSetConversionThreshold = 10",
    "meta" : "default: 10",
    "version" : "2.0.0",
    "docHTML" : "The threshold of set size for InSet conversion."
}, {
    "caption" : "spark.sql.optimizer.inSetSwitchThreshold",
    "value" : "spark.sql.optimizer.inSetSwitchThreshold = 400",
    "meta" : "default: 400",
    "version" : "3.0.0",
    "docHTML" : "Configures the max set size in InSet for which Spark will generate code with switch statements. This is applicable only to bytes, shorts, ints, dates."
}, {
    "caption" : "spark.sql.optimizer.dynamicPartitionPruning.enabled",
    "value" : "spark.sql.optimizer.dynamicPartitionPruning.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, we will generate predicate for partition column when it's used as join key"
}, {
    "caption" : "spark.sql.optimizer.dynamicPartitionPruning.useStats",
    "value" : "spark.sql.optimizer.dynamicPartitionPruning.useStats = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, distinct count statistics will be used for computing the data size of the partitioned table after dynamic partition pruning, in order to evaluate if it is worth adding an extra subquery as the pruning filter if broadcast reuse is not applicable."
}, {
    "caption" : "spark.sql.optimizer.runtimeFilter.number.threshold",
    "value" : "spark.sql.optimizer.runtimeFilter.number.threshold = 10",
    "meta" : "default: 10",
    "version" : "3.3.0",
    "docHTML" : "The total number of injected runtime filters (non-DPP) for a single query. This is to prevent driver OOMs with too many Bloom filters."
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.enabled",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true and if one side of a shuffle join has a selective predicate, we attempt to insert a bloom filter in the other side to reduce the amount of shuffle data."
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.expectedNumItems",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.expectedNumItems = 1000000",
    "meta" : "default: 1000000",
    "version" : "3.3.0",
    "docHTML" : "The default number of expected items for the runtime bloomfilter"
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.maxNumItems",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.maxNumItems = 4000000",
    "meta" : "default: 4000000",
    "version" : "3.3.0",
    "docHTML" : "The max allowed number of expected items for the runtime bloom filter"
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.numBits",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.numBits = 8388608",
    "meta" : "default: 8388608",
    "version" : "3.3.0",
    "docHTML" : "The default number of bits to use for the runtime bloom filter"
}, {
    "caption" : "spark.sql.optimizer.runtime.bloomFilter.maxNumBits",
    "value" : "spark.sql.optimizer.runtime.bloomFilter.maxNumBits = 67108864",
    "meta" : "default: 67108864",
    "version" : "3.3.0",
    "docHTML" : "The max number of bits to use for the runtime bloom filter"
}, {
    "caption" : "spark.sql.inMemoryColumnarStorage.partitionPruning",
    "value" : "spark.sql.inMemoryColumnarStorage.partitionPruning = true",
    "meta" : "default: true",
    "version" : "1.2.0",
    "docHTML" : "When true, enable partition pruning for in-memory columnar tables."
}, {
    "caption" : "spark.sql.inMemoryTableScanStatistics.enable",
    "value" : "spark.sql.inMemoryTableScanStatistics.enable = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, enable in-memory table scan accumulators."
}, {
    "caption" : "spark.sql.inMemoryColumnarStorage.enableVectorizedReader",
    "value" : "spark.sql.inMemoryColumnarStorage.enableVectorizedReader = true",
    "meta" : "default: true",
    "version" : "2.3.1",
    "docHTML" : "Enables vectorized reader for columnar caching."
}, {
    "caption" : "spark.sql.columnVector.offheap.enabled",
    "value" : "spark.sql.columnVector.offheap.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "When true, use OffHeapColumnVector in ColumnarBatch."
}, {
    "caption" : "spark.sql.autoBroadcastJoinThreshold",
    "value" : "spark.sql.autoBroadcastJoinThreshold = 10MB",
    "meta" : "default: 10MB",
    "version" : "1.1.0",
    "docHTML" : "Configures the maximum size in bytes for a table that will be broadcast to all worker nodes when performing a join.  By setting this value to -1 broadcasting can be disabled. Note that currently statistics are only supported for Hive Metastore tables where the command `ANALYZE TABLE <tableName> COMPUTE STATISTICS noscan` has been run, and file-based data source tables where the statistics are computed directly on the files of data."
}, {
    "caption" : "spark.sql.hive.advancedPartitionPredicatePushdown.enabled",
    "value" : "spark.sql.hive.advancedPartitionPredicatePushdown.enabled = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "When true, advanced partition predicate pushdown into Hive metastore is enabled."
}, {
    "caption" : "spark.sql.adaptive.shuffle.targetPostShuffleInputSize",
    "value" : "spark.sql.adaptive.shuffle.targetPostShuffleInputSize = 64MB",
    "meta" : "default: 64MB",
    "version" : "1.6.0",
    "docHTML" : "(Deprecated since Spark 3.0)"
}, {
    "caption" : "spark.sql.adaptive.enabled",
    "value" : "spark.sql.adaptive.enabled = true",
    "meta" : "default: true",
    "version" : "1.6.0",
    "docHTML" : "When true, enable adaptive query execution, which re-optimizes the query plan in the middle of query execution, based on accurate runtime statistics."
}, {
    "caption" : "spark.sql.adaptive.forceApply",
    "value" : "spark.sql.adaptive.forceApply = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Adaptive query execution is skipped when the query does not have exchanges or sub-queries. By setting this config to true (together with 'spark.sql.adaptive.enabled' set to true), Spark will force apply adaptive query execution for all supported queries."
}, {
    "caption" : "spark.sql.adaptive.logLevel",
    "value" : "spark.sql.adaptive.logLevel = debug",
    "meta" : "default: debug",
    "version" : "3.0.0",
    "docHTML" : "Configures the log level for adaptive execution logging of plan changes. The value can be 'trace', 'debug', 'info', 'warn', or 'error'. The default log level is 'debug'."
}, {
    "caption" : "spark.sql.adaptive.advisoryPartitionSizeInBytes",
    "version" : "3.0.0",
    "docHTML" : "The advisory size in bytes of the shuffle partition during adaptive optimization (when spark.sql.adaptive.enabled is true). It takes effect when Spark coalesces small shuffle partitions or splits skewed shuffle partition."
}, {
    "caption" : "spark.sql.adaptive.coalescePartitions.enabled",
    "value" : "spark.sql.adaptive.coalescePartitions.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true and 'spark.sql.adaptive.enabled' is true, Spark will coalesce contiguous shuffle partitions according to the target size (specified by 'spark.sql.adaptive.advisoryPartitionSizeInBytes'), to avoid too many small tasks."
}, {
    "caption" : "spark.sql.adaptive.coalescePartitions.parallelismFirst",
    "value" : "spark.sql.adaptive.coalescePartitions.parallelismFirst = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "When true, Spark does not respect the target size specified by 'spark.sql.adaptive.advisoryPartitionSizeInBytes' (default 64MB) when coalescing contiguous shuffle partitions, but adaptively calculate the target size according to the default parallelism of the Spark cluster. The calculated size is usually smaller than the configured target size. This is to maximize the parallelism and avoid performance regression when enabling adaptive query execution. It's recommended to set this config to false and respect the configured target size."
}, {
    "caption" : "spark.sql.adaptive.coalescePartitions.minPartitionSize",
    "value" : "spark.sql.adaptive.coalescePartitions.minPartitionSize = 1MB",
    "meta" : "default: 1MB",
    "version" : "3.2.0",
    "docHTML" : "The minimum size of shuffle partitions after coalescing. This is useful when the adaptively calculated target size is too small during partition coalescing."
}, {
    "caption" : "spark.sql.adaptive.fetchShuffleBlocksInBatch",
    "value" : "spark.sql.adaptive.fetchShuffleBlocksInBatch = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to fetch the contiguous shuffle blocks in batch. Instead of fetching blocks one by one, fetching contiguous shuffle blocks for the same map task in batch can reduce IO and improve performance. Note, multiple contiguous blocks exist in single fetch request only happen when 'spark.sql.adaptive.enabled' and 'spark.sql.adaptive.coalescePartitions.enabled' are both true. This feature also depends on a relocatable serializer, the concatenation support codec in use, the new version shuffle fetch protocol and io encryption is disabled."
}, {
    "caption" : "spark.sql.adaptive.localShuffleReader.enabled",
    "value" : "spark.sql.adaptive.localShuffleReader.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true and 'spark.sql.adaptive.enabled' is true, Spark tries to use local shuffle reader to read the shuffle data when the shuffle partitioning is not needed, for example, after converting sort-merge join to broadcast-hash join."
}, {
    "caption" : "spark.sql.adaptive.skewJoin.skewedPartitionFactor",
    "value" : "spark.sql.adaptive.skewJoin.skewedPartitionFactor = 5",
    "meta" : "default: 5",
    "version" : "3.0.0",
    "docHTML" : "A partition is considered as skewed if its size is larger than this factor multiplying the median partition size and also larger than 'spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes'"
}, {
    "caption" : "spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes",
    "value" : "spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes = 256MB",
    "meta" : "default: 256MB",
    "version" : "3.0.0",
    "docHTML" : "A partition is considered as skewed if its size in bytes is larger than this threshold and also larger than 'spark.sql.adaptive.skewJoin.skewedPartitionFactor' multiplying the median partition size. Ideally this config should be set larger than 'spark.sql.adaptive.advisoryPartitionSizeInBytes'."
}, {
    "caption" : "spark.sql.adaptive.forceOptimizeSkewedJoin",
    "value" : "spark.sql.adaptive.forceOptimizeSkewedJoin = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, force enable OptimizeSkewedJoin even if it introduces extra shuffle."
}, {
    "caption" : "spark.sql.subexpressionElimination.enabled",
    "value" : "spark.sql.subexpressionElimination.enabled = true",
    "meta" : "default: true",
    "version" : "1.6.0",
    "docHTML" : "When true, common subexpressions will be eliminated."
}, {
    "caption" : "spark.sql.constraintPropagation.enabled",
    "value" : "spark.sql.constraintPropagation.enabled = true",
    "meta" : "default: true",
    "version" : "2.2.0",
    "docHTML" : "When true, the query optimizer will infer and propagate data constraints in the query plan to optimize them. Constraint propagation can sometimes be computationally expensive for certain kinds of query plans (such as those with a large number of predicates and aliases) which might negatively impact overall runtime."
}, {
    "caption" : "spark.sql.optimizer.propagateDistinctKeys.enabled",
    "value" : "spark.sql.optimizer.propagateDistinctKeys.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, the query optimizer will propagate a set of distinct attributes from the current node and use it to optimize query."
}, {
    "caption" : "spark.sql.parquet.mergeSchema",
    "value" : "spark.sql.parquet.mergeSchema = false",
    "meta" : "default: false",
    "version" : "1.5.0",
    "docHTML" : "When true, the Parquet data source merges schemas collected from all data files, otherwise the schema is picked from the summary file or a random data file if no summary file is available."
}, {
    "caption" : "spark.sql.parquet.respectSummaryFiles",
    "value" : "spark.sql.parquet.respectSummaryFiles = false",
    "meta" : "default: false",
    "version" : "1.5.0",
    "docHTML" : "When true, we make assumption that all part-files of Parquet are consistent with summary files and we will ignore them when merging schema. Otherwise, if this is false, which is the default, we will merge all part-files. This should be considered as expert-only option, and shouldn't be enabled before knowing what it means exactly."
}, {
    "caption" : "spark.sql.parquet.int96AsTimestamp",
    "value" : "spark.sql.parquet.int96AsTimestamp = true",
    "meta" : "default: true",
    "version" : "1.3.0",
    "docHTML" : "Some Parquet-producing systems, in particular Impala, store Timestamp into INT96. Spark would also store Timestamp as INT96 because we need to avoid precision lost of the nanoseconds field. This flag tells Spark SQL to interpret INT96 data as a timestamp to provide compatibility with these systems."
}, {
    "caption" : "spark.sql.parquet.int96TimestampConversion",
    "value" : "spark.sql.parquet.int96TimestampConversion = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "This controls whether timestamp adjustments should be applied to INT96 data when converting to timestamps, for data written by Impala.  This is necessary because Impala stores INT96 data with a different timezone offset than Hive & Spark."
}, {
    "caption" : "spark.sql.parquet.outputTimestampType",
    "value" : "spark.sql.parquet.outputTimestampType = INT96",
    "meta" : "default: INT96",
    "version" : "2.3.0",
    "docHTML" : "Sets which Parquet timestamp type to use when Spark writes data to Parquet files. INT96 is a non-standard but commonly used timestamp type in Parquet. TIMESTAMP_MICROS is a standard timestamp type in Parquet, which stores number of microseconds from the Unix epoch. TIMESTAMP_MILLIS is also standard, but with millisecond precision, which means Spark has to truncate the microsecond portion of its timestamp value."
}, {
    "caption" : "spark.sql.parquet.filterPushdown",
    "value" : "spark.sql.parquet.filterPushdown = true",
    "meta" : "default: true",
    "version" : "1.2.0",
    "docHTML" : "Enables Parquet filter push-down optimization when set to true."
}, {
    "caption" : "spark.sql.parquet.filterPushdown.date",
    "value" : "spark.sql.parquet.filterPushdown.date = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If true, enables Parquet filter push-down optimization for Date. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled."
}, {
    "caption" : "spark.sql.parquet.filterPushdown.decimal",
    "value" : "spark.sql.parquet.filterPushdown.decimal = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If true, enables Parquet filter push-down optimization for Decimal. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled."
}, {
    "caption" : "spark.sql.parquet.aggregatePushdown",
    "value" : "spark.sql.parquet.aggregatePushdown = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "If true, aggregates will be pushed down to Parquet for optimization. Support MIN, MAX and COUNT as aggregate expression. For MIN/MAX, support boolean, integer, float and date type. For COUNT, support all data types. If statistics is missing from any Parquet file footer, exception would be thrown."
}, {
    "caption" : "spark.sql.parquet.writeLegacyFormat",
    "value" : "spark.sql.parquet.writeLegacyFormat = false",
    "meta" : "default: false",
    "version" : "1.6.0",
    "docHTML" : "If true, data will be written in a way of Spark 1.4 and earlier. For example, decimal values will be written in Apache Parquet's fixed-length byte array format, which other systems such as Apache Hive and Apache Impala use. If false, the newer format in Parquet will be used. For example, decimals will be written in int-based format. If Parquet output is intended for use with systems that do not support this newer format, set to true."
}, {
    "caption" : "spark.sql.parquet.output.committer.class",
    "value" : "spark.sql.parquet.output.committer.class = org.apache.parquet.hadoop.ParquetOutputCommitter",
    "meta" : "default: org.apache.parquet.hadoop.ParquetOutputCommitter",
    "version" : "1.5.0",
    "docHTML" : "The output committer class used by Parquet. The specified class needs to be a subclass of org.apache.hadoop.mapreduce.OutputCommitter. Typically, it's also a subclass of org.apache.parquet.hadoop.ParquetOutputCommitter. If it is not, then metadata summaries will never be created, irrespective of the value of parquet.summary.metadata.level"
}, {
    "caption" : "spark.sql.parquet.enableVectorizedReader",
    "value" : "spark.sql.parquet.enableVectorizedReader = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "Enables vectorized parquet decoding."
}, {
    "caption" : "spark.sql.parquet.recordLevelFilter.enabled",
    "value" : "spark.sql.parquet.recordLevelFilter.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "If true, enables Parquet's native record-level filtering using the pushed down filters. This configuration only has an effect when 'spark.sql.parquet.filterPushdown' is enabled and the vectorized reader is not used. You can ensure the vectorized reader is not used by setting 'spark.sql.parquet.enableVectorizedReader' to false."
}, {
    "caption" : "spark.sql.parquet.columnarReaderBatchSize",
    "value" : "spark.sql.parquet.columnarReaderBatchSize = 4096",
    "meta" : "default: 4096",
    "version" : "2.4.0",
    "docHTML" : "The number of rows to include in a parquet vectorized reader batch. The number should be carefully chosen to minimize overhead and avoid OOMs in reading data."
}, {
    "caption" : "spark.sql.parquet.fieldId.write.enabled",
    "value" : "spark.sql.parquet.fieldId.write.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "Field ID is a native field of the Parquet schema spec. When enabled, Parquet writers will populate the field Id metadata (if present) in the Spark schema to the Parquet schema."
}, {
    "caption" : "spark.sql.parquet.fieldId.read.enabled",
    "value" : "spark.sql.parquet.fieldId.read.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Field ID is a native field of the Parquet schema spec. When enabled, Parquet readers will use field IDs (if present) in the requested Spark schema to look up Parquet fields instead of using column names"
}, {
    "caption" : "spark.sql.parquet.fieldId.read.ignoreMissing",
    "value" : "spark.sql.parquet.fieldId.read.ignoreMissing = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When the Parquet file doesn't have any field IDs but the Spark read schema is using field IDs to read, we will silently return nulls when this flag is enabled, or error otherwise."
}, {
    "caption" : "spark.sql.orc.enableVectorizedReader",
    "value" : "spark.sql.orc.enableVectorizedReader = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "Enables vectorized orc decoding."
}, {
    "caption" : "spark.sql.orc.columnarReaderBatchSize",
    "value" : "spark.sql.orc.columnarReaderBatchSize = 4096",
    "meta" : "default: 4096",
    "version" : "2.4.0",
    "docHTML" : "The number of rows to include in a orc vectorized reader batch. The number should be carefully chosen to minimize overhead and avoid OOMs in reading data."
}, {
    "caption" : "spark.sql.orc.filterPushdown",
    "value" : "spark.sql.orc.filterPushdown = true",
    "meta" : "default: true",
    "version" : "1.4.0",
    "docHTML" : "When true, enable filter pushdown for ORC files."
}, {
    "caption" : "spark.sql.orc.aggregatePushdown",
    "value" : "spark.sql.orc.aggregatePushdown = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "If true, aggregates will be pushed down to ORC for optimization. Support MIN, MAX and COUNT as aggregate expression. For MIN/MAX, support boolean, integer, float and date type. For COUNT, support all data types. If statistics is missing from any ORC file footer, exception would be thrown."
}, {
    "caption" : "spark.sql.orc.mergeSchema",
    "value" : "spark.sql.orc.mergeSchema = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, the Orc data source merges schemas collected from all data files, otherwise the schema is picked from a random data file."
}, {
    "caption" : "spark.sql.hive.verifyPartitionPath",
    "value" : "spark.sql.hive.verifyPartitionPath = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : "When true, check all the partition paths under the table's root directory when reading data stored in HDFS. This configuration will be deprecated in the future releases and replaced by spark.files.ignoreMissingFiles."
}, {
    "caption" : "spark.sql.hive.metastorePartitionPruning",
    "value" : "spark.sql.hive.metastorePartitionPruning = true",
    "meta" : "default: true",
    "version" : "1.5.0",
    "docHTML" : "When true, some predicates will be pushed down into the Hive metastore so that unmatching partitions can be eliminated earlier."
}, {
    "caption" : "spark.sql.hive.manageFilesourcePartitions",
    "value" : "spark.sql.hive.manageFilesourcePartitions = true",
    "meta" : "default: true",
    "version" : "2.1.1",
    "docHTML" : "When true, enable metastore partition management for file source tables as well. This includes both datasource and converted Hive tables. When partition management is enabled, datasource tables store partition in the Hive metastore, and use the metastore to prune partitions during query planning when spark.sql.hive.metastorePartitionPruning is set to true."
}, {
    "caption" : "spark.sql.hive.caseSensitiveInferenceMode",
    "value" : "spark.sql.hive.caseSensitiveInferenceMode = NEVER_INFER",
    "meta" : "default: NEVER_INFER",
    "version" : "2.1.1",
    "docHTML" : "Sets the action to take when a case-sensitive schema cannot be read from a Hive Serde table's properties when reading the table with Spark native data sources. Valid options include INFER_AND_SAVE (infer the case-sensitive schema from the underlying data files and write it back to the table properties), INFER_ONLY (infer the schema but don't attempt to write it to the table properties) and NEVER_INFER (the default mode-- fallback to using the case-insensitive metastore schema instead of inferring)."
}, {
    "caption" : "spark.sql.columnNameOfCorruptRecord",
    "value" : "spark.sql.columnNameOfCorruptRecord = _corrupt_record",
    "meta" : "default: _corrupt_record",
    "version" : "1.2.0",
    "docHTML" : "The name of internal column for storing raw/un-parsed JSON and CSV records that fail to parse."
}, {
    "caption" : "spark.sql.thriftServer.incrementalCollect",
    "value" : "spark.sql.thriftServer.incrementalCollect = false",
    "meta" : "default: false",
    "version" : "2.0.3",
    "docHTML" : "When true, enable incremental collection for execution in Thrift Server."
}, {
    "caption" : "spark.sql.thriftServer.interruptOnCancel",
    "value" : "spark.sql.thriftServer.interruptOnCancel = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When true, all running tasks will be interrupted if one cancels a query. When false, all running tasks will remain until finished."
}, {
    "caption" : "spark.sql.thriftServer.queryTimeout",
    "value" : "spark.sql.thriftServer.queryTimeout = 0ms",
    "meta" : "default: 0ms",
    "version" : "3.1.0",
    "docHTML" : "Set a query duration timeout in seconds in Thrift Server. If the timeout is set to a positive value, a running query will be cancelled automatically when the timeout is exceeded, otherwise the query continues to run till completion. If timeout values are set for each statement via `java.sql.Statement.setQueryTimeout` and they are smaller than this configuration value, they take precedence. If you set this timeout and prefer to cancel the queries right away without waiting task to finish, consider enabling spark.sql.thriftServer.interruptOnCancel together."
}, {
    "caption" : "spark.sql.thriftserver.ui.retainedStatements",
    "value" : "spark.sql.thriftserver.ui.retainedStatements = 200",
    "meta" : "default: 200",
    "version" : "1.4.0",
    "docHTML" : "The number of SQL statements kept in the JDBC/ODBC web UI history."
}, {
    "caption" : "spark.sql.thriftserver.ui.retainedSessions",
    "value" : "spark.sql.thriftserver.ui.retainedSessions = 200",
    "meta" : "default: 200",
    "version" : "1.4.0",
    "docHTML" : "The number of SQL client sessions kept in the JDBC/ODBC web UI history."
}, {
    "caption" : "spark.sql.sources.partitionColumnTypeInference.enabled",
    "value" : "spark.sql.sources.partitionColumnTypeInference.enabled = true",
    "meta" : "default: true",
    "version" : "1.5.0",
    "docHTML" : "When true, automatically infer the data types for partitioned columns."
}, {
    "caption" : "spark.sql.sources.bucketing.autoBucketedScan.enabled",
    "value" : "spark.sql.sources.bucketing.autoBucketedScan.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, decide whether to do bucketed scan on input tables based on query plan automatically. Do not use bucketed scan if 1. query does not have operators to utilize bucketing (e.g. join, group-by, etc), or 2. there's an exchange operator between these operators and table scan. Note when 'spark.sql.sources.bucketing.enabled' is set to false, this configuration does not take any effect."
}, {
    "caption" : "spark.sql.sources.commitProtocolClass",
    "value" : "spark.sql.sources.commitProtocolClass = org.apache.spark.sql.execution.datasources.SQLHadoopMapReduceCommitProtocol",
    "meta" : "default: org.apache.spark.sql.execution.datasources.SQLHadoopMapReduceCommitProtocol",
    "version" : "2.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.sql.sources.parallelPartitionDiscovery.threshold",
    "value" : "spark.sql.sources.parallelPartitionDiscovery.threshold = 32",
    "meta" : "default: 32",
    "version" : "1.5.0",
    "docHTML" : "The maximum number of paths allowed for listing files at driver side. If the number of detected paths exceeds this value during partition discovery, it tries to list the files with another Spark distributed job. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC."
}, {
    "caption" : "spark.sql.sources.parallelPartitionDiscovery.parallelism",
    "value" : "spark.sql.sources.parallelPartitionDiscovery.parallelism = 10000",
    "meta" : "default: 10000",
    "version" : "2.1.1",
    "docHTML" : "The number of parallelism to list a collection of path recursively, Set the number to prevent file listing from generating too many tasks."
}, {
    "caption" : "spark.sql.analyzer.failAmbiguousSelfJoin",
    "value" : "spark.sql.analyzer.failAmbiguousSelfJoin = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, fail the Dataset query if it contains ambiguous self-join."
}, {
    "caption" : "spark.sql.retainGroupColumns",
    "value" : "spark.sql.retainGroupColumns = true",
    "meta" : "default: true",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.sql.pivotMaxValues",
    "value" : "spark.sql.pivotMaxValues = 10000",
    "meta" : "default: 10000",
    "version" : "1.6.0",
    "docHTML" : "When doing a pivot without specifying values for the pivot column this is the maximum number of (distinct) values that will be collected without error."
}, {
    "caption" : "spark.sql.codegen.wholeStage",
    "value" : "spark.sql.codegen.wholeStage = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, the whole stage (of multiple operators) will be compiled into single java method."
}, {
    "caption" : "spark.sql.codegen.useIdInClassName",
    "value" : "spark.sql.codegen.useIdInClassName = true",
    "meta" : "default: true",
    "version" : "2.3.1",
    "docHTML" : "When true, embed the (whole-stage) codegen stage ID into the class name of the generated class as a suffix"
}, {
    "caption" : "spark.sql.codegen.maxFields",
    "value" : "spark.sql.codegen.maxFields = 100",
    "meta" : "default: 100",
    "version" : "2.0.0",
    "docHTML" : "The maximum number of fields (including nested fields) that will be supported before deactivating whole-stage codegen."
}, {
    "caption" : "spark.sql.codegen.logging.maxLines",
    "value" : "spark.sql.codegen.logging.maxLines = 1000",
    "meta" : "default: 1000",
    "version" : "2.3.0",
    "docHTML" : "The maximum number of codegen lines to log when errors occur. Use -1 for unlimited."
}, {
    "caption" : "spark.sql.codegen.hugeMethodLimit",
    "value" : "spark.sql.codegen.hugeMethodLimit = 65535",
    "meta" : "default: 65535",
    "version" : "2.3.0",
    "docHTML" : "The maximum bytecode size of a single compiled Java function generated by whole-stage codegen. When the compiled function exceeds this threshold, the whole-stage codegen is deactivated for this subtree of the current query plan. The default value is 65535, which is the largest bytecode size possible for a valid Java method. When running on HotSpot, it may be preferable to set the value to 8000 to match HotSpot's implementation."
}, {
    "caption" : "spark.sql.codegen.methodSplitThreshold",
    "value" : "spark.sql.codegen.methodSplitThreshold = 1024",
    "meta" : "default: 1024",
    "version" : "3.0.0",
    "docHTML" : "The threshold of source-code splitting in the codegen. When the number of characters in a single Java function (without comment) exceeds the threshold, the function will be automatically split to multiple smaller ones. We cannot know how many bytecode will be generated, so use the code length as metric. When running on HotSpot, a function's bytecode should not go beyond 8KB, otherwise it will not be JITted; it also should not be too small, otherwise there will be many function calls."
}, {
    "caption" : "spark.sql.execution.removeRedundantProjects",
    "value" : "spark.sql.execution.removeRedundantProjects = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "Whether to remove redundant project exec node based on children's output and ordering requirement."
}, {
    "caption" : "spark.sql.execution.removeRedundantSorts",
    "value" : "spark.sql.execution.removeRedundantSorts = true",
    "meta" : "default: true",
    "version" : "2.4.8",
    "docHTML" : "Whether to remove redundant physical sort node"
}, {
    "caption" : "spark.sql.execution.replaceHashWithSortAgg",
    "value" : "spark.sql.execution.replaceHashWithSortAgg = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Whether to replace hash aggregate node with sort aggregate based on children's ordering"
}, {
    "caption" : "spark.sql.streaming.stateStore.providerClass",
    "value" : "spark.sql.streaming.stateStore.providerClass = org.apache.spark.sql.execution.streaming.state.HDFSBackedStateStoreProvider",
    "meta" : "default: org.apache.spark.sql.execution.streaming.state.HDFSBackedStateStoreProvider",
    "version" : "2.3.0",
    "docHTML" : "The class used to manage state data in stateful streaming queries. This class must be a subclass of StateStoreProvider, and must have a zero-arg constructor. Note: For structured streaming, this configuration cannot be changed between query restarts from the same checkpoint location."
}, {
    "caption" : "spark.sql.streaming.stateStore.stateSchemaCheck",
    "value" : "spark.sql.streaming.stateStore.stateSchemaCheck = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, Spark will validate the state schema against schema on existing state and fail query if it's incompatible."
}, {
    "caption" : "spark.sql.streaming.stateStore.minDeltasForSnapshot",
    "value" : "spark.sql.streaming.stateStore.minDeltasForSnapshot = 10",
    "meta" : "default: 10",
    "version" : "2.0.0",
    "docHTML" : "Minimum number of state store delta files that needs to be generated before they consolidated into snapshots."
}, {
    "caption" : "spark.sql.streaming.stateStore.formatValidation.enabled",
    "value" : "spark.sql.streaming.stateStore.formatValidation.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, check if the data from state store is valid or not when running streaming queries. This can happen if the state store format has been changed. Note, the feature is only effective in the build-in HDFS state store provider now."
}, {
    "caption" : "spark.sql.streaming.forceDeleteTempCheckpointLocation",
    "value" : "spark.sql.streaming.forceDeleteTempCheckpointLocation = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, enable temporary checkpoint locations force delete."
}, {
    "caption" : "spark.sql.streaming.maxBatchesToRetainInMemory",
    "value" : "spark.sql.streaming.maxBatchesToRetainInMemory = 2",
    "meta" : "default: 2",
    "version" : "2.4.0",
    "docHTML" : "The maximum number of batches which will be retained in memory to avoid loading from files. The value adjusts a trade-off between memory usage vs cache miss: '2' covers both success and direct failure cases, '1' covers only success case, and '0' covers extreme case - disable cache to maximize memory size of executors."
}, {
    "caption" : "spark.sql.streaming.stateStore.maintenanceInterval",
    "value" : "spark.sql.streaming.stateStore.maintenanceInterval = 60000ms",
    "meta" : "default: 60000ms",
    "version" : "2.0.0",
    "docHTML" : "The interval in milliseconds between triggering maintenance tasks in StateStore. The maintenance task executes background maintenance task in all the loaded store providers if they are still the active instances according to the coordinator. If not, inactive instances of store providers will be closed."
}, {
    "caption" : "spark.sql.streaming.stateStore.compression.codec",
    "value" : "spark.sql.streaming.stateStore.compression.codec = lz4",
    "meta" : "default: lz4",
    "version" : "3.1.0",
    "docHTML" : "The codec used to compress delta and snapshot files generated by StateStore. By default, Spark provides four codecs: lz4, lzf, snappy, and zstd. You can also use fully qualified class names to specify the codec. Default codec is lz4."
}, {
    "caption" : "spark.sql.streaming.stateStore.rocksdb.formatVersion",
    "value" : "spark.sql.streaming.stateStore.rocksdb.formatVersion = 5",
    "meta" : "default: 5",
    "version" : "3.2.0",
    "docHTML" : "Set the RocksDB format version. This will be stored in the checkpoint when starting a streaming query. The checkpoint will use this RocksDB format version in the entire lifetime of the query."
}, {
    "caption" : "spark.sql.streaming.stopActiveRunOnRestart",
    "value" : "spark.sql.streaming.stopActiveRunOnRestart = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Running multiple runs of the same streaming query concurrently is not supported. If we find a concurrent active run for a streaming query (in the same or different SparkSessions on the same cluster) and this flag is true, we will stop the old streaming query run to start the new one."
}, {
    "caption" : "spark.sql.streaming.join.stateFormatVersion",
    "value" : "spark.sql.streaming.join.stateFormatVersion = 2",
    "meta" : "default: 2",
    "version" : "3.0.0",
    "docHTML" : "State format version used by streaming join operations in a streaming query. State between versions are tend to be incompatible, so state format version shouldn't be modified after running."
}, {
    "caption" : "spark.sql.streaming.unsupportedOperationCheck",
    "value" : "spark.sql.streaming.unsupportedOperationCheck = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, the logical plan for streaming query will be checked for unsupported operations."
}, {
    "caption" : "spark.sql.streaming.kafka.useDeprecatedOffsetFetching",
    "value" : "spark.sql.streaming.kafka.useDeprecatedOffsetFetching = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, the deprecated Consumer based offset fetching used which could cause infinite wait in Spark queries. Such cases query restart is the only workaround. For further details please see Offset Fetching chapter of Structured Streaming Kafka Integration Guide."
}, {
    "caption" : "spark.sql.streaming.fileStreamSink.ignoreMetadata",
    "value" : "spark.sql.streaming.fileStreamSink.ignoreMetadata = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "If this is enabled, when Spark reads from the results of a streaming query written by `FileStreamSink`, Spark will ignore the metadata log and treat it as normal path to read, e.g. listing files using HDFS APIs."
}, {
    "caption" : "spark.sql.variable.substitute",
    "value" : "spark.sql.variable.substitute = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "This enables substitution using syntax like `${var}`, `${system:var}`, and `${env:var}`."
}, {
    "caption" : "spark.sql.codegen.aggregate.map.twolevel.partialOnly",
    "value" : "spark.sql.codegen.aggregate.map.twolevel.partialOnly = true",
    "meta" : "default: true",
    "version" : "3.2.1",
    "docHTML" : "Enable two-level aggregate hash map for partial aggregate only, because final aggregate might get more distinct keys compared to partial aggregate. Overhead of looking up 1st-level map might dominate when having a lot of distinct keys."
}, {
    "caption" : "spark.sql.codegen.aggregate.map.vectorized.enable",
    "value" : "spark.sql.codegen.aggregate.map.vectorized.enable = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Enable vectorized aggregate hash map. This is for testing/benchmarking only."
}, {
    "caption" : "spark.sql.codegen.aggregate.splitAggregateFunc.enabled",
    "value" : "spark.sql.codegen.aggregate.splitAggregateFunc.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, the code generator would split aggregate code into individual methods instead of a single big method. This can be used to avoid oversized function that can miss the opportunity of JIT optimization."
}, {
    "caption" : "spark.sql.codegen.aggregate.sortAggregate.enabled",
    "value" : "spark.sql.codegen.aggregate.sortAggregate.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, enable code-gen for sort aggregate."
}, {
    "caption" : "spark.sql.codegen.join.existenceSortMergeJoin.enabled",
    "value" : "spark.sql.codegen.join.existenceSortMergeJoin.enabled = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true, enable code-gen for Existence sort merge join."
}, {
    "caption" : "spark.sql.legacy.allowParameterlessCount",
    "value" : "spark.sql.legacy.allowParameterlessCount = false",
    "meta" : "default: false",
    "version" : "3.1.1",
    "docHTML" : "When true, the SQL function 'count' is allowed to take no parameters."
}, {
    "caption" : "spark.sql.legacy.allowNonEmptyLocationInCTAS",
    "value" : "spark.sql.legacy.allowNonEmptyLocationInCTAS = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When false, CTAS with LOCATION throws an analysis exception if the location is not empty."
}, {
    "caption" : "spark.sql.legacy.allowZeroIndexInFormatString",
    "value" : "spark.sql.legacy.allowZeroIndexInFormatString = false",
    "meta" : "default: false",
    "version" : "3.3",
    "docHTML" : "When false, the `strfmt` in `format_string(strfmt, obj, ...)` and `printf(strfmt, obj, ...)` will no longer support to use \"0$\" to specify the first argument, the first argument should always reference by \"1$\" when use argument index to indicating the position of the argument in the argument list."
}, {
    "caption" : "spark.sql.legacy.useCurrentConfigsForView",
    "value" : "spark.sql.legacy.useCurrentConfigsForView = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, SQL Configs of the current active SparkSession instead of the captured ones will be applied during the parsing and analysis phases of the view resolution."
}, {
    "caption" : "spark.sql.legacy.storeAnalyzedPlanForView",
    "value" : "spark.sql.legacy.storeAnalyzedPlanForView = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, analyzed plan instead of SQL text will be stored when creating temporary view"
}, {
    "caption" : "spark.sql.legacy.allowAutoGeneratedAliasForView",
    "value" : "spark.sql.legacy.allowAutoGeneratedAliasForView = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When true, it's allowed to use a input query without explicit alias when creating a permanent view."
}, {
    "caption" : "spark.sql.streaming.commitProtocolClass",
    "value" : "spark.sql.streaming.commitProtocolClass = org.apache.spark.sql.execution.streaming.ManifestFileCommitProtocol",
    "meta" : "default: org.apache.spark.sql.execution.streaming.ManifestFileCommitProtocol",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.sql.streaming.multipleWatermarkPolicy",
    "value" : "spark.sql.streaming.multipleWatermarkPolicy = min",
    "meta" : "default: min",
    "version" : "2.4.0",
    "docHTML" : "Policy to calculate the global watermark value when there are multiple watermark operators in a streaming query. The default value is 'min' which chooses the minimum watermark reported across multiple operators. Other alternative value is 'max' which chooses the maximum across multiple operators. Note: This configuration cannot be changed between query restarts from the same checkpoint location."
}, {
    "caption" : "spark.sql.objectHashAggregate.sortBased.fallbackThreshold",
    "value" : "spark.sql.objectHashAggregate.sortBased.fallbackThreshold = 128",
    "meta" : "default: 128",
    "version" : "2.2.0",
    "docHTML" : "In the case of ObjectHashAggregateExec, when the size of the in-memory hash map grows too large, we will fall back to sort-based aggregation. This option sets a row count threshold for the size of the hash map."
}, {
    "caption" : "spark.sql.jsonGenerator.ignoreNullFields",
    "value" : "spark.sql.jsonGenerator.ignoreNullFields = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to ignore null fields when generating JSON objects in JSON data source and JSON functions such as to_json. If false, it generates null for null fields in JSON objects."
}, {
    "caption" : "spark.sql.optimizer.enableJsonExpressionOptimization",
    "value" : "spark.sql.optimizer.enableJsonExpressionOptimization = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "Whether to optimize JSON expressions in SQL optimizer. It includes pruning unnecessary columns from from_json, simplifying from_json + to_json, to_json + named_struct(from_json.col1, from_json.col2, ....)."
}, {
    "caption" : "spark.sql.optimizer.enableCsvExpressionOptimization",
    "value" : "spark.sql.optimizer.enableCsvExpressionOptimization = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "Whether to optimize CSV expressions in SQL optimizer. It includes pruning unnecessary columns from from_csv."
}, {
    "caption" : "spark.sql.optimizer.collapseProjectAlwaysInline",
    "value" : "spark.sql.optimizer.collapseProjectAlwaysInline = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Whether to always collapse two adjacent projections and inline expressions even if it causes extra duplication."
}, {
    "caption" : "spark.sql.streaming.fileSink.log.compactInterval",
    "value" : "spark.sql.streaming.fileSink.log.compactInterval = 10",
    "meta" : "default: 10",
    "version" : "2.0.0",
    "docHTML" : "Number of log files after which all the previous files are compacted into the next log file."
}, {
    "caption" : "spark.sql.streaming.fileSink.log.cleanupDelay",
    "value" : "spark.sql.streaming.fileSink.log.cleanupDelay = 600000ms",
    "meta" : "default: 600000ms",
    "version" : "2.0.0",
    "docHTML" : "How long that a file is guaranteed to be visible for all readers."
}, {
    "caption" : "spark.sql.streaming.fileSource.log.compactInterval",
    "value" : "spark.sql.streaming.fileSource.log.compactInterval = 10",
    "meta" : "default: 10",
    "version" : "2.0.1",
    "docHTML" : "Number of log files after which all the previous files are compacted into the next log file."
}, {
    "caption" : "spark.sql.streaming.fileSource.log.cleanupDelay",
    "value" : "spark.sql.streaming.fileSource.log.cleanupDelay = 600000ms",
    "meta" : "default: 600000ms",
    "version" : "2.0.1",
    "docHTML" : "How long in milliseconds a file is guaranteed to be visible for all readers."
}, {
    "caption" : "spark.sql.streaming.fileSource.schema.forceNullable",
    "value" : "spark.sql.streaming.fileSource.schema.forceNullable = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, force the schema of streaming file source to be nullable (including all the fields). Otherwise, the schema might not be compatible with actual data, which leads to corruptions."
}, {
    "caption" : "spark.sql.streaming.fileSource.cleaner.numThreads",
    "value" : "spark.sql.streaming.fileSource.cleaner.numThreads = 1",
    "meta" : "default: 1",
    "version" : "3.0.0",
    "docHTML" : "Number of threads used in the file source completed file cleaner."
}, {
    "caption" : "spark.sql.streaming.schemaInference",
    "value" : "spark.sql.streaming.schemaInference = false",
    "meta" : "default: false",
    "version" : "2.0.0",
    "docHTML" : "Whether file-based streaming sources will infer its own schema"
}, {
    "caption" : "spark.sql.streaming.noDataMicroBatches.enabled",
    "value" : "spark.sql.streaming.noDataMicroBatches.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.1",
    "docHTML" : "Whether streaming micro-batch engine will execute batches without data for eager state management for stateful streaming queries."
}, {
    "caption" : "spark.sql.streaming.metricsEnabled",
    "value" : "spark.sql.streaming.metricsEnabled = false",
    "meta" : "default: false",
    "version" : "2.0.2",
    "docHTML" : "Whether Dropwizard/Codahale metrics will be reported for active streaming queries."
}, {
    "caption" : "spark.sql.streaming.numRecentProgressUpdates",
    "value" : "spark.sql.streaming.numRecentProgressUpdates = 100",
    "meta" : "default: 100",
    "version" : "2.1.1",
    "docHTML" : "The number of progress updates to retain for a streaming query"
}, {
    "caption" : "spark.sql.statistics.fallBackToHdfs",
    "value" : "spark.sql.statistics.fallBackToHdfs = false",
    "meta" : "default: false",
    "version" : "2.0.0",
    "docHTML" : "When true, it will fall back to HDFS if the table statistics are not available from table metadata. This is useful in determining if a table is small enough to use broadcast joins. This flag is effective only for non-partitioned Hive tables. For non-partitioned data source tables, it will be automatically recalculated if table statistics are not available. For partitioned data source and partitioned Hive tables, It is 'spark.sql.defaultSizeInBytes' if table statistics are not available."
}, {
    "caption" : "spark.sql.cbo.joinReorder.dp.threshold",
    "value" : "spark.sql.cbo.joinReorder.dp.threshold = 12",
    "meta" : "default: 12",
    "version" : "2.2.0",
    "docHTML" : "The maximum number of joined nodes allowed in the dynamic programming algorithm."
}, {
    "caption" : "spark.sql.cbo.joinReorder.dp.star.filter",
    "value" : "spark.sql.cbo.joinReorder.dp.star.filter = false",
    "meta" : "default: false",
    "version" : "2.2.0",
    "docHTML" : "Applies star-join filter heuristics to cost based join enumeration."
}, {
    "caption" : "spark.sql.cbo.starJoinFTRatio",
    "value" : "spark.sql.cbo.starJoinFTRatio = 0.9",
    "meta" : "default: 0.9",
    "version" : "2.2.0",
    "docHTML" : "Specifies the upper limit of the ratio between the largest fact tables for a star join to be considered. "
}, {
    "caption" : "spark.sql.windowExec.buffer.in.memory.threshold",
    "value" : "spark.sql.windowExec.buffer.in.memory.threshold = 4096",
    "meta" : "default: 4096",
    "version" : "2.2.1",
    "docHTML" : "Threshold for number of rows guaranteed to be held in memory by the window operator"
}, {
    "caption" : "spark.sql.windowExec.buffer.spill.threshold",
    "value" : "spark.sql.windowExec.buffer.spill.threshold = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.2.0",
    "docHTML" : "Threshold for number of rows to be spilled by window operator"
}, {
    "caption" : "spark.sql.sessionWindow.buffer.spill.threshold",
    "value" : "spark.sql.sessionWindow.buffer.spill.threshold = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.2.0",
    "docHTML" : "Threshold for number of rows to be spilled by window operator. Note that the buffer is used only for the query Spark cannot apply aggregations on determining session window."
}, {
    "caption" : "spark.sql.parser.quotedRegexColumnNames",
    "value" : "spark.sql.parser.quotedRegexColumnNames = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "When true, quoted Identifiers (using backticks) in SELECT statement are interpreted as regular expressions."
}, {
    "caption" : "spark.sql.execution.rangeExchange.sampleSizePerPartition",
    "value" : "spark.sql.execution.rangeExchange.sampleSizePerPartition = 100",
    "meta" : "default: 100",
    "version" : "2.3.0",
    "docHTML" : "Number of points to sample per partition in order to determine the range boundaries for range partitioning, typically used in global sorting (without limit)."
}, {
    "caption" : "spark.sql.execution.arrow.pyspark.enabled",
    "version" : "3.0.0",
    "docHTML" : "When true, make use of Apache Arrow for columnar data transfers in PySpark. This optimization applies to: 1. pyspark.sql.DataFrame.toPandas 2. pyspark.sql.SparkSession.createDataFrame when its input is a Pandas DataFrame The following data types are unsupported: ArrayType of TimestampType, and nested StructType."
}, {
    "caption" : "spark.sql.execution.arrow.pyspark.selfDestruct.enabled",
    "value" : "spark.sql.execution.arrow.pyspark.selfDestruct.enabled = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "(Experimental) When true, make use of Apache Arrow's self-destruct and split-blocks options for columnar data transfers in PySpark, when converting from Arrow to Pandas. This reduces memory usage at the cost of some CPU time. This optimization applies to: pyspark.sql.DataFrame.toPandas when 'spark.sql.execution.arrow.pyspark.enabled' is set."
}, {
    "caption" : "spark.sql.pyspark.jvmStacktrace.enabled",
    "value" : "spark.sql.pyspark.jvmStacktrace.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, it shows the JVM stacktrace in the user-facing PySpark exception together with Python stacktrace. By default, it is disabled and hides JVM stacktrace and shows a Python-friendly exception only."
}, {
    "caption" : "spark.sql.execution.arrow.sparkr.enabled",
    "value" : "spark.sql.execution.arrow.sparkr.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, make use of Apache Arrow for columnar data transfers in SparkR. This optimization applies to: 1. createDataFrame when its input is an R DataFrame 2. collect 3. dapply 4. gapply The following data types are unsupported: FloatType, BinaryType, ArrayType, StructType and MapType."
}, {
    "caption" : "spark.sql.execution.arrow.pyspark.fallback.enabled",
    "version" : "3.0.0",
    "docHTML" : "When true, optimizations enabled by 'spark.sql.execution.arrow.pyspark.enabled' will fallback automatically to non-optimized implementations if an error occurs."
}, {
    "caption" : "spark.sql.execution.arrow.maxRecordsPerBatch",
    "value" : "spark.sql.execution.arrow.maxRecordsPerBatch = 10000",
    "meta" : "default: 10000",
    "version" : "2.3.0",
    "docHTML" : "When using Apache Arrow, limit the maximum number of records that can be written to a single ArrowRecordBatch in memory. If set to zero or negative there is no limit."
}, {
    "caption" : "spark.sql.execution.pyspark.udf.simplifiedTraceback.enabled",
    "value" : "spark.sql.execution.pyspark.udf.simplifiedTraceback.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, the traceback from Python UDFs is simplified. It hides the Python worker, (de)serialization, etc from PySpark in tracebacks, and only shows the exception messages from UDFs. Note that this works only with CPython 3.7+."
}, {
    "caption" : "spark.sql.execution.pandas.convertToArrowArraySafely",
    "value" : "spark.sql.execution.pandas.convertToArrowArraySafely = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, Arrow will perform safe type conversion when converting Pandas.Series to Arrow array during serialization. Arrow will raise errors when detecting unsafe type conversion like overflow. When false, disabling Arrow's type check and do type conversions anyway. This config only works for Arrow 0.11.0+."
}, {
    "caption" : "spark.sql.optimizer.replaceExceptWithFilter",
    "value" : "spark.sql.optimizer.replaceExceptWithFilter = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "When true, the apply function of the rule verifies whether the right node of the except operation is of type Filter or Project followed by Filter. If yes, the rule further verifies 1) Excluding the filter operations from the right (as well as the left node, if any) on the top, whether both the nodes evaluates to a same result. 2) The left and right nodes don't contain any SubqueryExpressions. 3) The output column names of the left node are distinct. If all the conditions are met, the rule will replace the except operation with a Filter by flipping the filter condition(s) of the right node."
}, {
    "caption" : "spark.sql.decimalOperations.allowPrecisionLoss",
    "value" : "spark.sql.decimalOperations.allowPrecisionLoss = true",
    "meta" : "default: true",
    "version" : "2.3.1",
    "docHTML" : "When true (default), establishing the result type of an arithmetic operation happens according to Hive behavior and SQL ANSI 2011 specification, i.e. rounding the decimal part of the result if an exact representation is not possible. Otherwise, NULL is returned in those cases, as previously."
}, {
    "caption" : "spark.sql.legacy.literal.pickMinimumPrecision",
    "value" : "spark.sql.legacy.literal.pickMinimumPrecision = true",
    "meta" : "default: true",
    "version" : "2.3.3",
    "docHTML" : "When integral literal is used in decimal operations, pick a minimum precision required by the literal if this config is true, to make the resulting precision and/or scale smaller. This can reduce the possibility of precision lose and/or overflow."
}, {
    "caption" : "spark.sql.redaction.options.regex",
    "value" : "spark.sql.redaction.options.regex = ",
    "version" : "2.2.2",
    "docHTML" : "Regex to decide which keys in a Spark SQL command's options map contain sensitive information. The values of options whose names that match this regex will be redacted in the explain output. This redaction is applied on top of the global redaction configuration defined by spark.redaction.regex."
}, {
    "caption" : "spark.sql.redaction.string.regex",
    "version" : "2.3.0",
    "docHTML" : "Regex to decide which parts of strings produced by Spark contain sensitive information. When this regex matches a string part, that string part is replaced by a dummy value. This is currently used to redact the output of SQL explain commands. When this conf is not set, the value from `spark.redaction.string.regex` is used."
}, {
    "caption" : "spark.sql.sources.validatePartitionColumns",
    "value" : "spark.sql.sources.validatePartitionColumns = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When this option is set to true, partition column values will be validated with user-specified schema. If the validation fails, a runtime exception is thrown. When this option is set to false, the partition column value will be converted to null if it can not be casted to corresponding user-specified schema."
}, {
    "caption" : "spark.sql.streaming.continuous.executorQueueSize",
    "value" : "spark.sql.streaming.continuous.executorQueueSize = 1024",
    "meta" : "default: 1024",
    "version" : "2.3.0",
    "docHTML" : "The size (measured in number of rows) of the queue used in continuous execution to buffer the results of a ContinuousDataReader."
}, {
    "caption" : "spark.sql.streaming.disabledV2Writers",
    "value" : "spark.sql.streaming.disabledV2Writers = ",
    "meta" : "default: ",
    "version" : "2.3.1",
    "docHTML" : "A comma-separated list of fully qualified data source register class names for which StreamWriteSupport is disabled. Writes to these sources will fall back to the V1 Sinks."
}, {
    "caption" : "spark.sql.streaming.disabledV2MicroBatchReaders",
    "value" : "spark.sql.streaming.disabledV2MicroBatchReaders = ",
    "meta" : "default: ",
    "version" : "2.4.0",
    "docHTML" : "A comma-separated list of fully qualified data source register class names for which MicroBatchReadSupport is disabled. Reads from these sources will fall back to the V1 Sources."
}, {
    "caption" : "spark.sql.execution.fastFailOnFileFormatOutput",
    "value" : "spark.sql.execution.fastFailOnFileFormatOutput = false",
    "meta" : "default: false",
    "version" : "3.0.2",
    "docHTML" : "Whether to fast fail task execution when writing output to FileFormat datasource. If this is enabled, in `FileFormatWriter` we will catch `FileAlreadyExistsException` and fast fail output task without further task retry. Only enabling this if you know the `FileAlreadyExistsException` of the output task is unrecoverable, i.e., further task attempts won't be able to success. If the `FileAlreadyExistsException` might be recoverable, you should keep this as disabled and let Spark to retry output tasks. This is disabled by default."
}, {
    "caption" : "spark.sql.ansi.enforceReservedKeywords",
    "value" : "spark.sql.ansi.enforceReservedKeywords = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true and 'spark.sql.ansi.enabled' is true, the Spark SQL parser enforces the ANSI reserved keywords and forbids SQL queries that use reserved keywords as alias names and/or identifiers for table, view, function, etc."
}, {
    "caption" : "spark.sql.ansi.strictIndexOperator",
    "value" : "spark.sql.ansi.strictIndexOperator = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When true and 'spark.sql.ansi.enabled' is true, accessing complex SQL types via [] operator will throw an exception if array index is out of bound, or map key does not exist. Otherwise, Spark will return a null result when accessing an invalid index."
}, {
    "caption" : "spark.sql.optimizer.nestedSchemaPruning.enabled",
    "value" : "spark.sql.optimizer.nestedSchemaPruning.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.1",
    "docHTML" : "Prune nested fields from a logical relation's output which are unnecessary in satisfying a query. This optimization allows columnar file format readers to avoid reading unnecessary nested column data. Currently Parquet and ORC are the data sources that implement this optimization."
}, {
    "caption" : "spark.sql.optimizer.serializer.nestedSchemaPruning.enabled",
    "value" : "spark.sql.optimizer.serializer.nestedSchemaPruning.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Prune nested fields from object serialization operator which are unnecessary in satisfying a query. This optimization allows object serializers to avoid executing unnecessary nested expressions."
}, {
    "caption" : "spark.sql.optimizer.expression.nestedPruning.enabled",
    "value" : "spark.sql.optimizer.expression.nestedPruning.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Prune nested fields from expressions in an operator which are unnecessary in satisfying a query. Note that this optimization doesn't prune nested fields from physical data source scanning. For pruning nested fields from scanning, please use `spark.sql.optimizer.nestedSchemaPruning.enabled` config."
}, {
    "caption" : "spark.sql.optimizer.decorrelateInnerQuery.enabled",
    "value" : "spark.sql.optimizer.decorrelateInnerQuery.enabled = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "Decorrelate inner query by eliminating correlated references and build domain joins."
}, {
    "caption" : "spark.sql.optimizer.optimizeOneRowRelationSubquery",
    "value" : "spark.sql.optimizer.optimizeOneRowRelationSubquery = true",
    "meta" : "default: true",
    "version" : "3.2.0",
    "docHTML" : "When true, the optimizer will inline subqueries with OneRowRelation as leaf nodes."
}, {
    "caption" : "spark.sql.execution.topKSortFallbackThreshold",
    "value" : "spark.sql.execution.topKSortFallbackThreshold = 2147483632",
    "meta" : "default: 2147483632",
    "version" : "2.4.0",
    "docHTML" : "In SQL queries with a SORT followed by a LIMIT like 'SELECT x FROM t ORDER BY y LIMIT m', if m is under this threshold, do a top-K sort in memory, otherwise do a global sort which spills to disk if necessary."
}, {
    "caption" : "spark.sql.csv.parser.columnPruning.enabled",
    "value" : "spark.sql.csv.parser.columnPruning.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If it is set to true, column names of the requested schema are passed to CSV parser. Other column values can be ignored during parsing even if they are malformed."
}, {
    "caption" : "spark.sql.repl.eagerEval.maxNumRows",
    "value" : "spark.sql.repl.eagerEval.maxNumRows = 20",
    "meta" : "default: 20",
    "version" : "2.4.0",
    "docHTML" : "The max number of rows that are returned by eager evaluation. This only takes effect when spark.sql.repl.eagerEval.enabled is set to true. The valid range of this config is from 0 to (Int.MaxValue - 1), so the invalid config like negative and greater than (Int.MaxValue - 1) will be normalized to 0 and (Int.MaxValue - 1)."
}, {
    "caption" : "spark.sql.legacy.setopsPrecedence.enabled",
    "value" : "spark.sql.legacy.setopsPrecedence.enabled = false",
    "meta" : "default: false",
    "version" : "2.4.0",
    "docHTML" : "When set to true and the order of evaluation is not specified by parentheses, the set operations are performed from left to right as they appear in the query. When set to false and order of evaluation is not specified by parentheses, INTERSECT operations are performed before any UNION, EXCEPT and MINUS operations."
}, {
    "caption" : "spark.sql.legacy.parser.havingWithoutGroupByAsWhere",
    "value" : "spark.sql.legacy.parser.havingWithoutGroupByAsWhere = false",
    "meta" : "default: false",
    "version" : "2.4.1",
    "docHTML" : "If it is set to true, the parser will treat HAVING without GROUP BY as a normal WHERE, which does not follow SQL standard."
}, {
    "caption" : "spark.sql.legacy.json.allowEmptyString.enabled",
    "value" : "spark.sql.legacy.json.allowEmptyString.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, the parser of JSON data source treats empty strings as null for some data types such as `IntegerType`."
}, {
    "caption" : "spark.sql.legacy.allowUntypedScalaUDF",
    "value" : "spark.sql.legacy.allowUntypedScalaUDF = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, user is allowed to use org.apache.spark.sql.functions.udf(f: AnyRef, dataType: DataType). Otherwise, an exception will be thrown at runtime."
}, {
    "caption" : "spark.sql.legacy.statisticalAggregate",
    "value" : "spark.sql.legacy.statisticalAggregate = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When set to true, statistical aggregate function returns Double.NaN if divide by zero occurred during expression evaluation, otherwise, it returns null. Before version 3.1.0, it returns NaN in divideByZero case by default."
}, {
    "caption" : "spark.sql.truncateTable.ignorePermissionAcl.enabled",
    "value" : "spark.sql.truncateTable.ignorePermissionAcl.enabled = false",
    "meta" : "default: false",
    "version" : "2.4.6",
    "docHTML" : "When set to true, TRUNCATE TABLE command will not try to set back original permission and ACLs when re-creating the table/partition paths."
}, {
    "caption" : "spark.sql.legacy.dataset.nameNonStructGroupingKeyAsValue",
    "value" : "spark.sql.legacy.dataset.nameNonStructGroupingKeyAsValue = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, the key attribute resulted from running `Dataset.groupByKey` for non-struct key type, will be named as `value`, following the behavior of Spark version 2.4 and earlier."
}, {
    "caption" : "spark.sql.maxMetadataStringLength",
    "value" : "spark.sql.maxMetadataStringLength = 100",
    "meta" : "default: 100",
    "version" : "3.1.0",
    "docHTML" : "Maximum number of characters to output for a metadata string. e.g. file location in `DataSourceScanExec`, every value will be abbreviated if exceed length."
}, {
    "caption" : "spark.sql.legacy.setCommandRejectsSparkCoreConfs",
    "value" : "spark.sql.legacy.setCommandRejectsSparkCoreConfs = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "If it is set to true, SET command will fail when the key is registered as a SparkConf entry."
}, {
    "caption" : "spark.sql.datetime.java8API.enabled",
    "value" : "spark.sql.datetime.java8API.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "If the configuration property is set to true, java.time.Instant and java.time.LocalDate classes of Java 8 API are used as external types for Catalyst's TimestampType and DateType. If it is set to false, java.sql.Timestamp and java.sql.Date are used for the same purpose."
}, {
    "caption" : "spark.sql.sources.binaryFile.maxLength",
    "value" : "spark.sql.sources.binaryFile.maxLength = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.0.0",
    "docHTML" : "The max length of a file that can be read by the binary file data source. Spark will fail fast and not attempt to read the file if its length exceeds this value. The theoretical max is Int.MaxValue, though VMs might implement a smaller max."
}, {
    "caption" : "spark.sql.legacy.typeCoercion.datetimeToString.enabled",
    "value" : "spark.sql.legacy.typeCoercion.datetimeToString.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "If it is set to true, date/timestamp will cast to string in binary comparisons with String when spark.sql.ansi.enabled is false."
}, {
    "caption" : "spark.sql.legacy.ctePrecedencePolicy",
    "value" : "spark.sql.legacy.ctePrecedencePolicy = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, outer CTE definitions takes precedence over inner definitions. If set to CORRECTED, inner CTE definitions take precedence. The default value is EXCEPTION, AnalysisException is thrown while name conflict is detected in nested CTE. This config will be removed in future versions and CORRECTED will be the only behavior."
}, {
    "caption" : "spark.sql.legacy.timeParserPolicy",
    "value" : "spark.sql.legacy.timeParserPolicy = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, java.text.SimpleDateFormat is used for formatting and parsing dates/timestamps in a locale-sensitive manner, which is the approach before Spark 3.0. When set to CORRECTED, classes from java.time.* packages are used for the same purpose. The default value is EXCEPTION, RuntimeException is thrown when we will get different results."
}, {
    "caption" : "spark.sql.maven.additionalRemoteRepositories",
    "value" : "spark.sql.maven.additionalRemoteRepositories = https://maven-central.storage-download.googleapis.com/maven2/",
    "meta" : "default: https://maven-central.storage-download.googleapis.com/maven2/",
    "version" : "3.0.0",
    "docHTML" : "A comma-delimited string config of the optional additional remote Maven mirror repositories. This is only used for downloading Hive jars in IsolatedClientLoader if the default Maven Central repo is unreachable."
}, {
    "caption" : "spark.sql.legacy.fromDayTimeString.enabled",
    "value" : "spark.sql.legacy.fromDayTimeString.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, the `from` bound is not taken into account in conversion of a day-time string to an interval, and the `to` bound is used to skip all interval units out of the specified range. If it is set to `false`, `ParseException` is thrown if the input does not match to the pattern defined by `from` and `to`."
}, {
    "caption" : "spark.sql.legacy.notReserveProperties",
    "value" : "spark.sql.legacy.notReserveProperties = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, all database and table properties are not reserved and available for create/alter syntaxes. But please be aware that the reserved properties will be silently removed."
}, {
    "caption" : "spark.sql.legacy.addSingleFileInAddFile",
    "value" : "spark.sql.legacy.addSingleFileInAddFile = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, only a single file can be added using ADD FILE. If false, then users can add directory by passing directory path to ADD FILE."
}, {
    "caption" : "spark.sql.csv.filterPushdown.enabled",
    "value" : "spark.sql.csv.filterPushdown.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, enable filter pushdown to CSV datasource."
}, {
    "caption" : "spark.sql.json.filterPushdown.enabled",
    "value" : "spark.sql.json.filterPushdown.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, enable filter pushdown to JSON datasource."
}, {
    "caption" : "spark.sql.avro.filterPushdown.enabled",
    "value" : "spark.sql.avro.filterPushdown.enabled = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, enable filter pushdown to Avro datasource."
}, {
    "caption" : "spark.sql.legacy.allowHashOnMapType",
    "value" : "spark.sql.legacy.allowHashOnMapType = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When set to true, hash expressions can be applied on elements of MapType. Otherwise, an analysis exception will be thrown."
}, {
    "caption" : "spark.sql.legacy.integerGroupingId",
    "value" : "spark.sql.legacy.integerGroupingId = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, grouping_id() returns int values instead of long values."
}, {
    "caption" : "spark.sql.parquet.int96RebaseModeInWrite",
    "value" : "spark.sql.parquet.int96RebaseModeInWrite = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.1.0",
    "docHTML" : "When LEGACY, Spark will rebase INT96 timestamps from Proleptic Gregorian calendar to the legacy hybrid (Julian + Gregorian) calendar when writing Parquet files. When CORRECTED, Spark will not do rebase and write the timestamps as it is. When EXCEPTION, which is the default, Spark will fail the writing if it sees ancient timestamps that are ambiguous between the two calendars."
}, {
    "caption" : "spark.sql.parquet.datetimeRebaseModeInWrite",
    "value" : "spark.sql.parquet.datetimeRebaseModeInWrite = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, Spark will rebase dates/timestamps from Proleptic Gregorian calendar to the legacy hybrid (Julian + Gregorian) calendar when writing Parquet files. When CORRECTED, Spark will not do rebase and write the dates/timestamps as it is. When EXCEPTION, which is the default, Spark will fail the writing if it sees ancient dates/timestamps that are ambiguous between the two calendars. This config influences on writes of the following parquet logical types: DATE, TIMESTAMP_MILLIS, TIMESTAMP_MICROS. The INT96 type has the separate config: spark.sql.parquet.int96RebaseModeInWrite."
}, {
    "caption" : "spark.sql.parquet.int96RebaseModeInRead",
    "value" : "spark.sql.parquet.int96RebaseModeInRead = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.1.0",
    "docHTML" : "When LEGACY, Spark will rebase INT96 timestamps from the legacy hybrid (Julian + Gregorian) calendar to Proleptic Gregorian calendar when reading Parquet files. When CORRECTED, Spark will not do rebase and read the timestamps as it is. When EXCEPTION, which is the default, Spark will fail the reading if it sees ancient timestamps that are ambiguous between the two calendars. This config is only effective if the writer info (like Spark, Hive) of the Parquet files is unknown."
}, {
    "caption" : "spark.sql.parquet.datetimeRebaseModeInRead",
    "value" : "spark.sql.parquet.datetimeRebaseModeInRead = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, Spark will rebase dates/timestamps from the legacy hybrid (Julian + Gregorian) calendar to Proleptic Gregorian calendar when reading Parquet files. When CORRECTED, Spark will not do rebase and read the dates/timestamps as it is. When EXCEPTION, which is the default, Spark will fail the reading if it sees ancient dates/timestamps that are ambiguous between the two calendars. This config is only effective if the writer info (like Spark, Hive) of the Parquet files is unknown. This config influences on reads of the following parquet logical types: DATE, TIMESTAMP_MILLIS, TIMESTAMP_MICROS. The INT96 type has the separate config: spark.sql.parquet.int96RebaseModeInRead."
}, {
    "caption" : "spark.sql.avro.datetimeRebaseModeInWrite",
    "value" : "spark.sql.avro.datetimeRebaseModeInWrite = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, Spark will rebase dates/timestamps from Proleptic Gregorian calendar to the legacy hybrid (Julian + Gregorian) calendar when writing Avro files. When CORRECTED, Spark will not do rebase and write the dates/timestamps as it is. When EXCEPTION, which is the default, Spark will fail the writing if it sees ancient dates/timestamps that are ambiguous between the two calendars."
}, {
    "caption" : "spark.sql.scriptTransformation.exitTimeoutInSeconds",
    "value" : "spark.sql.scriptTransformation.exitTimeoutInSeconds = 10000ms",
    "meta" : "default: 10000ms",
    "version" : "3.0.0",
    "docHTML" : "Timeout for executor to wait for the termination of transformation script when EOF."
}, {
    "caption" : "spark.sql.bucketing.coalesceBucketsInJoin.enabled",
    "value" : "spark.sql.bucketing.coalesceBucketsInJoin.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, if two bucketed tables with the different number of buckets are joined, the side with a bigger number of buckets will be coalesced to have the same number of buckets as the other side. Bigger number of buckets is divisible by the smaller number of buckets. Bucket coalescing is applied to sort-merge joins and shuffled hash join. Note: Coalescing bucketed table can avoid unnecessary shuffling in join, but it also reduces parallelism and could possibly cause OOM for shuffled hash join."
}, {
    "caption" : "spark.sql.optimizeNullAwareAntiJoin",
    "value" : "spark.sql.optimizeNullAwareAntiJoin = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When true, NULL-aware anti join execution will be planed into BroadcastHashJoinExec with flag isNullAwareAntiJoin enabled, optimized from O(M*N) calculation into O(M) calculation using Hash lookup instead of Looping lookup.Only support for singleColumn NAAJ for now."
}, {
    "caption" : "spark.sql.legacy.castComplexTypesToString.enabled",
    "value" : "spark.sql.legacy.castComplexTypesToString.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, maps and structs are wrapped by [] in casting to strings, and NULL elements of structs/maps/arrays will be omitted while converting to strings. Otherwise, if this is false, which is the default, maps and structs are wrapped by {}, and NULL elements will be converted to \"null\"."
}, {
    "caption" : "spark.sql.legacy.pathOptionBehavior.enabled",
    "value" : "spark.sql.legacy.pathOptionBehavior.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, \"path\" option is overwritten if one path parameter is passed to DataFrameReader.load(), DataFrameWriter.save(), DataStreamReader.load(), or DataStreamWriter.start(). Also, \"path\" option is added to the overall paths if multiple path parameters are passed to DataFrameReader.load()"
}, {
    "caption" : "spark.sql.legacy.extraOptionsBehavior.enabled",
    "value" : "spark.sql.legacy.extraOptionsBehavior.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, the extra options will be ignored for DataFrameReader.table(). If set it to false, which is the default, Spark will check if the extra options have the same key, but the value is different with the table serde properties. If the check passes, the extra options will be merged with the serde properties as the scan options. Otherwise, an exception will be thrown."
}, {
    "caption" : "spark.sql.legacy.createHiveTableByDefault",
    "value" : "spark.sql.legacy.createHiveTableByDefault = true",
    "meta" : "default: true",
    "version" : "3.1.0",
    "docHTML" : "When set to true, CREATE TABLE syntax without USING or STORED AS will use Hive instead of the value of spark.sql.sources.default as the table provider."
}, {
    "caption" : "spark.sql.legacy.charVarcharAsString",
    "value" : "spark.sql.legacy.charVarcharAsString = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, Spark treats CHAR/VARCHAR type the same as STRING type, which is the behavior of Spark 3.0 and earlier. This means no length check for CHAR/VARCHAR type and no padding for CHAR type when writing data to the table."
}, {
    "caption" : "spark.sql.legacy.keepCommandOutputSchema",
    "value" : "spark.sql.legacy.keepCommandOutputSchema = false",
    "meta" : "default: false",
    "version" : "3.0.2",
    "docHTML" : "When true, Spark will keep the output schema of commands such as SHOW DATABASES unchanged."
}, {
    "caption" : "spark.sql.maxConcurrentOutputFileWriters",
    "value" : "spark.sql.maxConcurrentOutputFileWriters = 0",
    "meta" : "default: 0",
    "version" : "3.2.0",
    "docHTML" : "Maximum number of output file writers to use concurrently. If number of writers needed reaches this limit, task will sort rest of output then writing them."
}, {
    "caption" : "spark.sql.pyspark.inferNestedDictAsStruct.enabled",
    "value" : "spark.sql.pyspark.inferNestedDictAsStruct.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "PySpark's SparkSession.createDataFrame infers the nested dict as a map by default. When it set to true, it infers the nested dict as a struct."
}, {
    "caption" : "spark.sql.legacy.histogramNumericPropagateInputType",
    "value" : "spark.sql.legacy.histogramNumericPropagateInputType = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "The histogram_numeric function computes a histogram on numeric 'expr' using nb bins. The return value is an array of (x,y) pairs representing the centers of the histogram's bins. If this config is set to true, the output type of the 'x' field in the return value is propagated from the input value consumed in the aggregate function. Otherwise, 'x' always has double type."
}, {
    "caption" : "spark.sql.legacy.lpadRpadAlwaysReturnString",
    "value" : "spark.sql.legacy.lpadRpadAlwaysReturnString = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When set to false, when the first argument and the optional padding pattern is a byte sequence, the result is a BINARY value. The default padding pattern in this case is the zero byte. When set to true, it restores the legacy behavior of always returning string types even for binary inputs."
}, {
    "caption" : "spark.sql.files.ignoreCorruptFiles",
    "value" : "spark.sql.files.ignoreCorruptFiles = false",
    "meta" : "default: false",
    "version" : "2.1.1",
    "docHTML" : "Whether to ignore corrupt files. If true, the Spark jobs will continue to run when encountering corrupted files and the contents that have been read will still be returned. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC."
}, {
    "caption" : "spark.sql.files.ignoreMissingFiles",
    "value" : "spark.sql.files.ignoreMissingFiles = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "Whether to ignore missing files. If true, the Spark jobs will continue to run when encountering missing files and the contents that have been read will still be returned. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC."
}, {
    "caption" : "spark.sql.files.openCostInBytes",
    "value" : "spark.sql.files.openCostInBytes = 4MB",
    "meta" : "default: 4MB",
    "version" : "2.0.0",
    "docHTML" : "The estimated cost to open a file, measured by the number of bytes could be scanned in the same time. This is used when putting multiple files into a partition. It's better to over estimated, then the partitions with small files will be faster than partitions with bigger files (which is scheduled first). This configuration is effective only when using file-based sources such as Parquet, JSON and ORC."
}, {
    "caption" : "spark.sql.analyzer.maxIterations",
    "value" : "spark.sql.analyzer.maxIterations = 100",
    "meta" : "default: 100",
    "version" : "3.0.0",
    "docHTML" : "The max number of iterations the analyzer runs."
}, {
    "caption" : "spark.sql.optimizer.maxIterations",
    "value" : "spark.sql.optimizer.maxIterations = 100",
    "meta" : "default: 100",
    "version" : "2.0.0",
    "docHTML" : "The max number of iterations the optimizer runs."
}, {
    "caption" : "spark.sql.planChangeLog.level",
    "value" : "spark.sql.planChangeLog.level = trace",
    "meta" : "default: trace",
    "version" : "3.1.0",
    "docHTML" : "Configures the log level for logging the change from the original plan to the new plan after a rule or batch is applied. The value can be 'trace', 'debug', 'info', 'warn', or 'error'. The default log level is 'trace'."
}, {
    "caption" : "spark.sql.inMemoryColumnarStorage.compressed",
    "value" : "spark.sql.inMemoryColumnarStorage.compressed = true",
    "meta" : "default: true",
    "version" : "1.0.1",
    "docHTML" : "When set to true Spark SQL will automatically select a compression codec for each column based on statistics of the data."
}, {
    "caption" : "spark.sql.inMemoryColumnarStorage.batchSize",
    "value" : "spark.sql.inMemoryColumnarStorage.batchSize = 10000",
    "meta" : "default: 10000",
    "version" : "1.1.1",
    "docHTML" : "Controls the size of batches for columnar caching.  Larger batch sizes can improve memory utilization and compression, but risk OOMs when caching data."
}, {
    "caption" : "spark.sql.join.preferSortMergeJoin",
    "value" : "spark.sql.join.preferSortMergeJoin = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, prefer sort merge join over shuffled hash join. Sort merge join consumes less memory than shuffled hash join and it works efficiently when both join tables are large. On the other hand, shuffled hash join can improve performance (e.g., of full outer joins) when one of join tables is much smaller."
}, {
    "caption" : "spark.sql.sort.enableRadixSort",
    "value" : "spark.sql.sort.enableRadixSort = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, enable use of radix sort when possible. Radix sort is much faster but requires additional memory to be reserved up-front. The memory overhead may be significant when sorting very small rows (up to 50% more in this case)."
}, {
    "caption" : "spark.sql.shuffledHashJoinFactor",
    "value" : "spark.sql.shuffledHashJoinFactor = 3",
    "meta" : "default: 3",
    "version" : "3.3.0",
    "docHTML" : "The shuffle hash join can be selected if the data size of small side multiplied by this factor is still smaller than the large side."
}, {
    "caption" : "spark.sql.limit.scaleUpFactor",
    "value" : "spark.sql.limit.scaleUpFactor = 4",
    "meta" : "default: 4",
    "version" : "2.1.1",
    "docHTML" : "Minimal increase rate in number of partitions between attempts when executing a take on a query. Higher values lead to more partitions read. Lower values might lead to longer execution times as more jobs will be run"
}, {
    "caption" : "spark.sql.shuffle.partitions",
    "value" : "spark.sql.shuffle.partitions = 200",
    "meta" : "default: 200",
    "version" : "1.1.0",
    "docHTML" : "The default number of partitions to use when shuffling data for joins or aggregations. Note: For structured streaming, this configuration cannot be changed between query restarts from the same checkpoint location."
}, {
    "caption" : "spark.sql.adaptive.skewJoin.enabled",
    "value" : "spark.sql.adaptive.skewJoin.enabled = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true and 'spark.sql.adaptive.enabled' is true, Spark dynamically handles skew in shuffled join (sort-merge and shuffled hash) by splitting (and replicating if needed) skewed partitions."
}, {
    "caption" : "spark.sql.caseSensitive",
    "value" : "spark.sql.caseSensitive = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : "Whether the query analyzer should be case sensitive or not. Default to case insensitive. It is highly discouraged to turn on case sensitive mode."
}, {
    "caption" : "spark.sql.parser.escapedStringLiterals",
    "value" : "spark.sql.parser.escapedStringLiterals = false",
    "meta" : "default: false",
    "version" : "2.2.1",
    "docHTML" : "When true, string literals (including regex patterns) remain escaped in our SQL parser. The default is false since Spark 2.0. Setting it to true can restore the behavior prior to Spark 2.0."
}, {
    "caption" : "spark.sql.sources.fileCompressionFactor",
    "value" : "spark.sql.sources.fileCompressionFactor = 1.0",
    "meta" : "default: 1.0",
    "version" : "2.3.1",
    "docHTML" : "When estimating the output data size of a table scan, multiply the file size with this factor as the estimated data size, in case the data is compressed in the file and lead to a heavily underestimated result."
}, {
    "caption" : "spark.sql.parquet.binaryAsString",
    "value" : "spark.sql.parquet.binaryAsString = false",
    "meta" : "default: false",
    "version" : "1.1.1",
    "docHTML" : "Some other Parquet-producing systems, in particular Impala and older versions of Spark SQL, do not differentiate between binary data and strings when writing out the Parquet schema. This flag tells Spark SQL to interpret binary data as a string to provide compatibility with these systems."
}, {
    "caption" : "spark.sql.parquet.compression.codec",
    "value" : "spark.sql.parquet.compression.codec = snappy",
    "meta" : "default: snappy",
    "version" : "1.1.1",
    "docHTML" : "Sets the compression codec used when writing Parquet files. If either `compression` or `parquet.compression` is specified in the table-specific options/properties, the precedence would be `compression`, `parquet.compression`, `spark.sql.parquet.compression.codec`. Acceptable values include: none, uncompressed, snappy, gzip, lzo, brotli, lz4, zstd."
}, {
    "caption" : "spark.sql.orc.compression.codec",
    "value" : "spark.sql.orc.compression.codec = snappy",
    "meta" : "default: snappy",
    "version" : "2.3.0",
    "docHTML" : "Sets the compression codec used when writing ORC files. If either `compression` or `orc.compress` is specified in the table-specific options/properties, the precedence would be `compression`, `orc.compress`, `spark.sql.orc.compression.codec`.Acceptable values include: none, uncompressed, snappy, zlib, lzo, zstd, lz4."
}, {
    "caption" : "spark.sql.orc.impl",
    "value" : "spark.sql.orc.impl = native",
    "meta" : "default: native",
    "version" : "2.3.0",
    "docHTML" : "When native, use the native version of ORC support instead of the ORC library in Hive. It is 'hive' by default prior to Spark 2.4."
}, {
    "caption" : "spark.sql.optimizer.metadataOnly",
    "value" : "spark.sql.optimizer.metadataOnly = false",
    "meta" : "default: false",
    "version" : "2.1.1",
    "docHTML" : "When true, enable the metadata-only query optimization that use the table's metadata to produce the partition columns instead of table scans. It applies when all the columns scanned are partition columns and the query has an aggregate operator that satisfies distinct semantics. By default the optimization is disabled, and deprecated as of Spark 3.0 since it may return incorrect results when the files are empty, see also SPARK-26709.It will be removed in the future releases. If you must use, use 'SparkSessionExtensions' instead to inject it as a custom rule."
}, {
    "caption" : "spark.sql.broadcastTimeout",
    "value" : "spark.sql.broadcastTimeout = 300",
    "meta" : "default: 300",
    "version" : "1.3.0",
    "docHTML" : "Timeout in seconds for the broadcast wait time in broadcast joins."
}, {
    "caption" : "spark.sql.sources.default",
    "value" : "spark.sql.sources.default = parquet",
    "meta" : "default: parquet",
    "version" : "1.3.0",
    "docHTML" : "The default data source to use in input/output."
}, {
    "caption" : "spark.sql.hive.convertCTAS",
    "value" : "spark.sql.hive.convertCTAS = false",
    "meta" : "default: false",
    "version" : "2.0.0",
    "docHTML" : "When true, a table created by a Hive CTAS statement (no USING clause) without specifying any storage property will be converted to a data source table, using the data source set by spark.sql.sources.default."
}, {
    "caption" : "spark.sql.hive.gatherFastStats",
    "value" : "spark.sql.hive.gatherFastStats = true",
    "meta" : "default: true",
    "version" : "2.0.1",
    "docHTML" : "When true, fast stats (number of files and total size of all files) will be gathered in parallel while repairing table partitions to avoid the sequential listing in Hive metastore."
}, {
    "caption" : "spark.sql.sources.bucketing.enabled",
    "value" : "spark.sql.sources.bucketing.enabled = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When false, we will treat bucketed table as normal table"
}, {
    "caption" : "spark.sql.sources.v2.bucketing.enabled",
    "value" : "spark.sql.sources.v2.bucketing.enabled = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "Similar to spark.sql.sources.bucketing.enabled, this config is used to enable bucketing for V2 data sources. When turned on, Spark will recognize the specific distribution reported by a V2 data source through SupportsReportPartitioning, and will try to avoid shuffle if necessary."
}, {
    "caption" : "spark.sql.sources.bucketing.maxBuckets",
    "value" : "spark.sql.sources.bucketing.maxBuckets = 100000",
    "meta" : "default: 100000",
    "version" : "2.4.0",
    "docHTML" : "The maximum number of buckets allowed."
}, {
    "caption" : "spark.sql.crossJoin.enabled",
    "value" : "spark.sql.crossJoin.enabled = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When false, we will throw an error if a query contains a cartesian product without explicit CROSS JOIN syntax."
}, {
    "caption" : "spark.sql.orderByOrdinal",
    "value" : "spark.sql.orderByOrdinal = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, the ordinal numbers are treated as the position in the select list. When false, the ordinal numbers in order/sort by clause are ignored."
}, {
    "caption" : "spark.sql.groupByOrdinal",
    "value" : "spark.sql.groupByOrdinal = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, the ordinal numbers in group by clauses are treated as the position in the select list. When false, the ordinal numbers are ignored."
}, {
    "caption" : "spark.sql.groupByAliases",
    "value" : "spark.sql.groupByAliases = true",
    "meta" : "default: true",
    "version" : "2.2.0",
    "docHTML" : "When true, aliases in a select list can be used in group by clauses. When false, an analysis exception is thrown in the case."
}, {
    "caption" : "spark.sql.sources.ignoreDataLocality",
    "value" : "spark.sql.sources.ignoreDataLocality = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "If true, Spark will not fetch the block locations for each file on listing files. This speeds up file listing, but the scheduler cannot schedule tasks to take advantage of data locality. It can be particularly useful if data is read from a remote cluster so the scheduler could never take advantage of locality anyway."
}, {
    "caption" : "spark.sql.runSQLOnFiles",
    "value" : "spark.sql.runSQLOnFiles = true",
    "meta" : "default: true",
    "version" : "1.6.0",
    "docHTML" : "When true, we could use `datasource`.`path` as table in SQL query."
}, {
    "caption" : "spark.sql.codegen.factoryMode",
    "value" : "spark.sql.codegen.factoryMode = FALLBACK",
    "meta" : "default: FALLBACK",
    "version" : "2.4.0",
    "docHTML" : "This config determines the fallback behavior of several codegen generators during tests. `FALLBACK` means trying codegen first and then falling back to interpreted if any compile error happens. Disabling fallback if `CODEGEN_ONLY`. `NO_CODEGEN` skips codegen and goes interpreted path always. Note that this config works only for tests."
}, {
    "caption" : "spark.sql.codegen.fallback",
    "value" : "spark.sql.codegen.fallback = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, (whole stage) codegen could be temporary disabled for the part of query that fail to compile generated code"
}, {
    "caption" : "spark.sql.files.maxRecordsPerFile",
    "value" : "spark.sql.files.maxRecordsPerFile = 0",
    "meta" : "default: 0",
    "version" : "2.2.0",
    "docHTML" : "Maximum number of records to write out to a single file. If this value is zero or negative, there is no limit."
}, {
    "caption" : "spark.sql.exchange.reuse",
    "value" : "spark.sql.exchange.reuse = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When true, the planner will try to find out duplicated exchanges and re-use them."
}, {
    "caption" : "spark.sql.execution.reuseSubquery",
    "value" : "spark.sql.execution.reuseSubquery = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When true, the planner will try to find out duplicated subqueries and re-use them."
}, {
    "caption" : "spark.sql.streaming.minBatchesToRetain",
    "value" : "spark.sql.streaming.minBatchesToRetain = 100",
    "meta" : "default: 100",
    "version" : "2.1.1",
    "docHTML" : "The minimum number of batches that must be retained and made recoverable."
}, {
    "caption" : "spark.sql.codegen.aggregate.map.twolevel.enabled",
    "value" : "spark.sql.codegen.aggregate.map.twolevel.enabled = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "Enable two-level aggregate hash map. When enabled, records will first be inserted/looked-up at a 1st-level, small, fast map, and then fallback to a 2nd-level, larger, slower map when 1st level is full or keys cannot be found. When disabled, records go directly to the 2nd level."
}, {
    "caption" : "spark.sql.view.maxNestedViewDepth",
    "value" : "spark.sql.view.maxNestedViewDepth = 100",
    "meta" : "default: 100",
    "version" : "2.2.0",
    "docHTML" : "The maximum depth of a view reference in a nested view. A nested view may reference other nested views, the dependencies are organized in a directed acyclic graph (DAG). However the DAG depth may become too large and cause unexpected behavior. This configuration puts a limit on this: when the depth of a view exceeds this value during analysis, we terminate the resolution to avoid potential errors."
}, {
    "caption" : "spark.sql.execution.useObjectHashAggregateExec",
    "value" : "spark.sql.execution.useObjectHashAggregateExec = true",
    "meta" : "default: true",
    "version" : "2.2.0",
    "docHTML" : "Decides if we use ObjectHashAggregateExec"
}, {
    "caption" : "spark.sql.streaming.fileSink.log.deletion",
    "value" : "spark.sql.streaming.fileSink.log.deletion = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "Whether to delete the expired log files in file stream sink."
}, {
    "caption" : "spark.sql.streaming.fileSource.log.deletion",
    "value" : "spark.sql.streaming.fileSource.log.deletion = true",
    "meta" : "default: true",
    "version" : "2.0.1",
    "docHTML" : "Whether to delete the expired log files in file stream source."
}, {
    "caption" : "spark.sql.streaming.pollingDelay",
    "value" : "spark.sql.streaming.pollingDelay = 10ms",
    "meta" : "default: 10ms",
    "version" : "2.0.0",
    "docHTML" : "How long to delay polling new data when no data is available"
}, {
    "caption" : "spark.sql.streaming.stopTimeout",
    "value" : "spark.sql.streaming.stopTimeout = 0",
    "meta" : "default: 0",
    "version" : "3.0.0",
    "docHTML" : "How long to wait in milliseconds for the streaming execution thread to stop when calling the streaming query's stop() method. 0 or negative values wait indefinitely."
}, {
    "caption" : "spark.sql.defaultSizeInBytes",
    "value" : "spark.sql.defaultSizeInBytes = 9223372036854775807b",
    "meta" : "default: 9223372036854775807b",
    "version" : "1.1.0",
    "docHTML" : "The default table size used in query planning. By default, it is set to Long.MaxValue which is larger than `spark.sql.autoBroadcastJoinThreshold` to be more conservative. That is to say by default the optimizer will not choose to broadcast a table unless it knows for sure its size is small enough."
}, {
    "caption" : "spark.sql.statistics.ndv.maxError",
    "value" : "spark.sql.statistics.ndv.maxError = 0.05",
    "meta" : "default: 0.05",
    "version" : "2.1.1",
    "docHTML" : "The maximum relative standard deviation allowed in HyperLogLog++ algorithm when generating column level statistics."
}, {
    "caption" : "spark.sql.statistics.histogram.enabled",
    "value" : "spark.sql.statistics.histogram.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "Generates histograms when computing column statistics if enabled. Histograms can provide better estimation accuracy. Currently, Spark only supports equi-height histogram. Note that collecting histograms takes extra cost. For example, collecting column statistics usually takes only one table scan, but generating equi-height histogram will cause an extra table scan."
}, {
    "caption" : "spark.sql.statistics.histogram.numBins",
    "value" : "spark.sql.statistics.histogram.numBins = 254",
    "meta" : "default: 254",
    "version" : "2.3.0",
    "docHTML" : "The number of bins when generating histograms."
}, {
    "caption" : "spark.sql.statistics.percentile.accuracy",
    "value" : "spark.sql.statistics.percentile.accuracy = 10000",
    "meta" : "default: 10000",
    "version" : "2.3.0",
    "docHTML" : "Accuracy of percentile approximation when generating equi-height histograms. Larger value means better accuracy. The relative error can be deduced by 1.0 / PERCENTILE_ACCURACY."
}, {
    "caption" : "spark.sql.statistics.size.autoUpdate.enabled",
    "value" : "spark.sql.statistics.size.autoUpdate.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "Enables automatic update for table size once table's data is changed. Note that if the total number of files of the table is very large, this can be expensive and slow down data change commands."
}, {
    "caption" : "spark.sql.cbo.enabled",
    "value" : "spark.sql.cbo.enabled = false",
    "meta" : "default: false",
    "version" : "2.2.0",
    "docHTML" : "Enables CBO for estimation of plan statistics when set true."
}, {
    "caption" : "spark.sql.cbo.planStats.enabled",
    "value" : "spark.sql.cbo.planStats.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, the logical plan will fetch row counts and column statistics from catalog."
}, {
    "caption" : "spark.sql.cbo.joinReorder.enabled",
    "value" : "spark.sql.cbo.joinReorder.enabled = false",
    "meta" : "default: false",
    "version" : "2.2.0",
    "docHTML" : "Enables join reorder in CBO."
}, {
    "caption" : "spark.sql.cbo.joinReorder.card.weight",
    "value" : "spark.sql.cbo.joinReorder.card.weight = 0.7",
    "meta" : "default: 0.7",
    "version" : "2.2.0",
    "docHTML" : "The weight of the ratio of cardinalities (number of rows) in the cost comparison function. The ratio of sizes in bytes has weight 1 - this value. The weighted geometric mean of these ratios is used to decide which of the candidate plans will be chosen by the CBO."
}, {
    "caption" : "spark.sql.cbo.starSchemaDetection",
    "value" : "spark.sql.cbo.starSchemaDetection = false",
    "meta" : "default: false",
    "version" : "2.2.0",
    "docHTML" : "When true, it enables join reordering based on star schema detection. "
}, {
    "caption" : "spark.sql.session.timeZone",
    "value" : "spark.sql.session.timeZone = Asia/Shanghai",
    "meta" : "default: Asia/Shanghai",
    "version" : "2.2.0",
    "docHTML" : "The ID of session local timezone in the format of either region-based zone IDs or zone offsets. Region IDs must have the form 'area/city', such as 'America/Los_Angeles'. Zone offsets must be in the format '(+|-)HH', '(+|-)HH:mm' or '(+|-)HH:mm:ss', e.g '-08', '+01:00' or '-13:33:33'. Also 'UTC' and 'Z' are supported as aliases of '+00:00'. Other short names are not recommended to use because they can be ambiguous."
}, {
    "caption" : "spark.sql.execution.arrow.enabled",
    "value" : "spark.sql.execution.arrow.enabled = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "(Deprecated since Spark 3.0, please set 'spark.sql.execution.arrow.pyspark.enabled'.)"
}, {
    "caption" : "spark.sql.execution.arrow.fallback.enabled",
    "value" : "spark.sql.execution.arrow.fallback.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "(Deprecated since Spark 3.0, please set 'spark.sql.execution.arrow.pyspark.fallback.enabled'.)"
}, {
    "caption" : "spark.sql.execution.pandas.udf.buffer.size",
    "version" : "3.0.0",
    "docHTML" : "Same as `spark.buffer.size` but only applies to Pandas UDF executions. If it is not set, the fallback is `spark.buffer.size`. Note that Pandas execution requires more than 4 bytes. Lowering this value could make small Pandas UDF batch iterated and pipelined; however, it might degrade performance. See SPARK-27870."
}, {
    "caption" : "spark.sql.function.concatBinaryAsString",
    "value" : "spark.sql.function.concatBinaryAsString = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "When this option is set to false and all inputs are binary, `functions.concat` returns an output as binary. Otherwise, it returns as a string."
}, {
    "caption" : "spark.sql.function.eltOutputAsString",
    "value" : "spark.sql.function.eltOutputAsString = false",
    "meta" : "default: false",
    "version" : "2.3.0",
    "docHTML" : "When this option is set to false and all inputs are binary, `elt` returns an output as binary. Otherwise, it returns as a string."
}, {
    "caption" : "spark.sql.sources.useV1SourceList",
    "value" : "spark.sql.sources.useV1SourceList = avro,csv,json,kafka,orc,parquet,text",
    "meta" : "default: avro,csv,json,kafka,orc,parquet,text",
    "version" : "3.0.0",
    "docHTML" : "A comma-separated list of data source short names or fully qualified data source implementation class names for which Data Source V2 code path is disabled. These data sources will fallback to Data Source V1 code path."
}, {
    "caption" : "spark.sql.sources.partitionOverwriteMode",
    "value" : "spark.sql.sources.partitionOverwriteMode = STATIC",
    "meta" : "default: STATIC",
    "version" : "2.3.0",
    "docHTML" : "When INSERT OVERWRITE a partitioned data source table, we currently support 2 modes: static and dynamic. In static mode, Spark deletes all the partitions that match the partition specification(e.g. PARTITION(a=1,b)) in the INSERT statement, before overwriting. In dynamic mode, Spark doesn't delete partitions ahead, and only overwrite those partitions that have data written into it at runtime. By default we use static mode to keep the same behavior of Spark prior to 2.3. Note that this config doesn't affect Hive serde tables, as they are always overwritten with dynamic mode. This can also be set as an output option for a data source using key partitionOverwriteMode (which takes precedence over this setting), e.g. dataframe.write.option(\"partitionOverwriteMode\", \"dynamic\").save(path)."
}, {
    "caption" : "spark.sql.storeAssignmentPolicy",
    "value" : "spark.sql.storeAssignmentPolicy = ANSI",
    "meta" : "default: ANSI",
    "version" : "3.0.0",
    "docHTML" : "When inserting a value into a column with different data type, Spark will perform type coercion. Currently, we support 3 policies for the type coercion rules: ANSI, legacy and strict. With ANSI policy, Spark performs the type coercion as per ANSI SQL. In practice, the behavior is mostly the same as PostgreSQL. It disallows certain unreasonable type conversions such as converting `string` to `int` or `double` to `boolean`. With legacy policy, Spark allows the type coercion as long as it is a valid `Cast`, which is very loose. e.g. converting `string` to `int` or `double` to `boolean` is allowed. It is also the only behavior in Spark 2.x and it is compatible with Hive. With strict policy, Spark doesn't allow any possible precision loss or data truncation in type coercion, e.g. converting `double` to `int` or `decimal` to `double` is not allowed."
}, {
    "caption" : "spark.sql.ansi.enabled",
    "value" : "spark.sql.ansi.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, Spark SQL uses an ANSI compliant dialect instead of being Hive compliant. For example, Spark will throw an exception at runtime instead of returning null results when the inputs to a SQL operator/function are invalid.For full details of this dialect, you can find them in the section \"ANSI Compliance\" of Spark's documentation. Some ANSI dialect features may be not from the ANSI SQL standard directly, but their behaviors align with ANSI SQL's style"
}, {
    "caption" : "spark.sql.execution.sortBeforeRepartition",
    "value" : "spark.sql.execution.sortBeforeRepartition = true",
    "meta" : "default: true",
    "version" : "2.1.4",
    "docHTML" : "When perform a repartition following a shuffle, the output row ordering would be nondeterministic. If some downstream stages fail and some tasks of the repartition stage retry, these tasks may generate different data, and that can lead to correctness issues. Turn on this config to insert a local sort before actually doing repartition to generate consistent repartition results. The performance of repartition() may go down since we insert extra local sort before it."
}, {
    "caption" : "spark.sql.optimizer.disableHints",
    "value" : "spark.sql.optimizer.disableHints = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "When true, the optimizer will disable user-specified hints that are additional directives for better planning of a query."
}, {
    "caption" : "spark.sql.repl.eagerEval.enabled",
    "value" : "spark.sql.repl.eagerEval.enabled = false",
    "meta" : "default: false",
    "version" : "2.4.0",
    "docHTML" : "Enables eager evaluation or not. When true, the top K rows of Dataset will be displayed if and only if the REPL supports the eager evaluation. Currently, the eager evaluation is supported in PySpark and SparkR. In PySpark, for the notebooks like Jupyter, the HTML table (generated by _repr_html_) will be returned. For plain Python REPL, the returned outputs are formatted like dataframe.show(). In SparkR, the returned outputs are showed similar to R data.frame would."
}, {
    "caption" : "spark.sql.repl.eagerEval.truncate",
    "value" : "spark.sql.repl.eagerEval.truncate = 20",
    "meta" : "default: 20",
    "version" : "2.4.0",
    "docHTML" : "The max number of characters for each cell that is returned by eager evaluation. This only takes effect when spark.sql.repl.eagerEval.enabled is set to true."
}, {
    "caption" : "spark.sql.avro.compression.codec",
    "value" : "spark.sql.avro.compression.codec = snappy",
    "meta" : "default: snappy",
    "version" : "2.4.0",
    "docHTML" : "Compression codec used in writing of AVRO files. Supported codecs: uncompressed, deflate, snappy, bzip2, xz and zstandard. Default codec is snappy."
}, {
    "caption" : "spark.sql.avro.deflate.level",
    "value" : "spark.sql.avro.deflate.level = -1",
    "meta" : "default: -1",
    "version" : "2.4.0",
    "docHTML" : "Compression level for the deflate codec used in writing of AVRO files. Valid value must be in the range of from 1 to 9 inclusive or -1. The default value is -1 which corresponds to 6 level in the current implementation."
}, {
    "caption" : "spark.sql.legacy.sizeOfNull",
    "value" : "spark.sql.legacy.sizeOfNull = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "If it is set to false, or spark.sql.ansi.enabled is true, then size of null returns null. Otherwise, it returns -1, which was inherited from Hive."
}, {
    "caption" : "spark.sql.debug.maxToStringFields",
    "value" : "spark.sql.debug.maxToStringFields = 25",
    "meta" : "default: 25",
    "version" : "3.0.0",
    "docHTML" : "Maximum number of fields of sequence-like entries can be converted to strings in debug output. Any elements beyond the limit will be dropped and replaced by a \"... N more fields\" placeholder."
}, {
    "caption" : "spark.sql.maxPlanStringLength",
    "value" : "spark.sql.maxPlanStringLength = 2147483632",
    "meta" : "default: 2147483632",
    "version" : "3.0.0",
    "docHTML" : "Maximum number of characters to output for a plan string.  If the plan is longer, further output will be truncated.  The default setting always generates a full plan.  Set this to a lower value such as 8k if plan strings are taking up too much memory or are causing OutOfMemory errors in the driver or UI processes."
}, {
    "caption" : "spark.sql.timestampType",
    "value" : "spark.sql.timestampType = TIMESTAMP_LTZ",
    "meta" : "default: TIMESTAMP_LTZ",
    "version" : "3.4.0",
    "docHTML" : "Configures the default timestamp type of Spark SQL, including SQL DDL, Cast clause and type literal. Setting the configuration as TIMESTAMP_NTZ will use TIMESTAMP WITHOUT TIME ZONE as the default type while putting it as TIMESTAMP_LTZ will use TIMESTAMP WITH LOCAL TIME ZONE. Before the 3.4.0 release, Spark only supports the TIMESTAMP WITH LOCAL TIME ZONE type."
}, {
    "caption" : "spark.sql.ui.explainMode",
    "value" : "spark.sql.ui.explainMode = formatted",
    "meta" : "default: formatted",
    "version" : "3.1.0",
    "docHTML" : "Configures the query explain mode used in the Spark SQL UI. The value can be 'simple', 'extended', 'codegen', 'cost', or 'formatted'. The default value is 'formatted'."
}, {
    "caption" : "spark.sql.defaultCatalog",
    "value" : "spark.sql.defaultCatalog = spark_catalog",
    "meta" : "default: spark_catalog",
    "version" : "3.0.0",
    "docHTML" : "Name of the default catalog. This will be the current catalog if users have not explicitly set the current catalog yet."
}, {
    "caption" : "spark.sql.mapKeyDedupPolicy",
    "value" : "spark.sql.mapKeyDedupPolicy = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "The policy to deduplicate map keys in builtin function: CreateMap, MapFromArrays, MapFromEntries, StringToMap, MapConcat and TransformKeys. When EXCEPTION, the query fails if duplicated map keys are detected. When LAST_WIN, the map key that is inserted at last takes precedence."
}, {
    "caption" : "spark.sql.legacy.doLooseUpcast",
    "value" : "spark.sql.legacy.doLooseUpcast = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "When true, the upcast will be loose and allows string to atomic types."
}, {
    "caption" : "spark.sql.addPartitionInBatch.size",
    "value" : "spark.sql.addPartitionInBatch.size = 100",
    "meta" : "default: 100",
    "version" : "3.0.0",
    "docHTML" : "The number of partitions to be handled in one turn when use `AlterTableAddPartitionCommand` or `RepairTableCommand` to add partitions into table. The smaller batch size is, the less memory is required for the real handler, e.g. Hive Metastore."
}, {
    "caption" : "spark.sql.avro.datetimeRebaseModeInRead",
    "value" : "spark.sql.avro.datetimeRebaseModeInRead = EXCEPTION",
    "meta" : "default: EXCEPTION",
    "version" : "3.0.0",
    "docHTML" : "When LEGACY, Spark will rebase dates/timestamps from the legacy hybrid (Julian + Gregorian) calendar to Proleptic Gregorian calendar when reading Avro files. When CORRECTED, Spark will not do rebase and read the dates/timestamps as it is. When EXCEPTION, which is the default, Spark will fail the reading if it sees ancient dates/timestamps that are ambiguous between the two calendars. This config is only effective if the writer info (like Spark, Hive) of the Avro files is unknown."
}, {
    "caption" : "spark.sql.charAsVarchar",
    "value" : "spark.sql.charAsVarchar = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, Spark replaces CHAR type with VARCHAR type in CREATE/REPLACE/ALTER TABLE commands, so that newly created/updated tables will not have CHAR type columns/fields. Existing tables with CHAR type columns/fields are not affected by this config."
}, {
    "caption" : "spark.sql.cli.print.header",
    "value" : "spark.sql.cli.print.header = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When set to true, spark-sql CLI prints the names of the columns in query output."
}, {
    "caption" : "spark.sql.legacy.interval.enabled",
    "value" : "spark.sql.legacy.interval.enabled = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When set to true, Spark SQL uses the mixed legacy interval type `CalendarIntervalType` instead of the ANSI compliant interval types `YearMonthIntervalType` and `DayTimeIntervalType`. For instance, the date subtraction expression returns `CalendarIntervalType` when the SQL config is set to `true` otherwise an ANSI interval."
}, {
    "caption" : "spark.sql.legacy.useV1Command",
    "value" : "spark.sql.legacy.useV1Command = false",
    "meta" : "default: false",
    "version" : "3.3.0",
    "docHTML" : "When true, Spark will use legacy V1 SQL commands."
}, {
    "caption" : "spark.sql.hive.convertMetastoreParquet.mergeSchema",
    "value" : "spark.sql.hive.convertMetastoreParquet.mergeSchema = false",
    "meta" : "default: false",
    "version" : "1.3.1",
    "docHTML" : "When true, also tries to merge possibly different but compatible Parquet schemas in different Parquet data files. This configuration is only effective when \"spark.sql.hive.convertMetastoreParquet\" is true."
}, {
    "caption" : "spark.sql.hive.convertMetastoreParquet",
    "value" : "spark.sql.hive.convertMetastoreParquet = true",
    "meta" : "default: true",
    "version" : "1.1.1",
    "docHTML" : "When set to true, the built-in Parquet reader and writer are used to process parquet tables created by using the HiveQL syntax, instead of Hive serde."
}, {
    "caption" : "spark.sql.hive.convertInsertingPartitionedTable",
    "value" : "spark.sql.hive.convertInsertingPartitionedTable = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When set to true, and `spark.sql.hive.convertMetastoreParquet` or `spark.sql.hive.convertMetastoreOrc` is true, the built-in ORC/Parquet writer is usedto process inserting into partitioned ORC/Parquet tables created by using the HiveSQL syntax."
}, {
    "caption" : "spark.sql.hive.convertMetastoreInsertDir",
    "value" : "spark.sql.hive.convertMetastoreInsertDir = true",
    "meta" : "default: true",
    "version" : "3.3.0",
    "docHTML" : "When set to true,  Spark will try to use built-in data source writer instead of Hive serde in INSERT OVERWRITE DIRECTORY. This flag is effective only if `spark.sql.hive.convertMetastoreParquet` or `spark.sql.hive.convertMetastoreOrc` is enabled respectively for Parquet and ORC formats"
}, {
    "caption" : "spark.sql.hive.metastore.sharedPrefixes",
    "value" : "spark.sql.hive.metastore.sharedPrefixes = ",
    "version" : "1.4.0",
    "docHTML" : "A comma separated list of class prefixes that should be loaded using the classloader that is shared between Spark SQL and a specific version of Hive. An example of classes that should be shared is JDBC drivers that are needed to talk to the metastore. Other classes that need to be shared are those that interact with classes that are already shared. For example, custom appenders that are used by log4j."
}, {
    "caption" : "spark.sql.hive.metastore.barrierPrefixes",
    "value" : "spark.sql.hive.metastore.barrierPrefixes = ",
    "version" : "1.4.0",
    "docHTML" : "A comma separated list of class prefixes that should explicitly be reloaded for each version of Hive that Spark SQL is communicating with. For example, Hive UDFs that are declared in a prefix that typically would be shared (i.e. <code>org.apache.spark.*</code>)."
}, {
    "caption" : "spark.sql.hive.version",
    "value" : "spark.sql.hive.version = 2.3.9",
    "meta" : "default: 2.3.9",
    "version" : "1.1.1",
    "docHTML" : "The compiled, a.k.a, builtin Hive version of the Spark distribution bundled with. Note that, this a read-only conf and only used to report the built-in hive version. If you want a different metastore client for Spark to call, please refer to spark.sql.hive.metastore.version."
}, {
    "caption" : "spark.sql.hive.metastore.version",
    "value" : "spark.sql.hive.metastore.version = 2.3.9",
    "meta" : "default: 2.3.9",
    "version" : "1.4.0",
    "docHTML" : "Version of the Hive metastore. Available options are <code>0.12.0</code> through <code>2.3.9</code> and <code>3.0.0</code> through <code>3.1.2</code>."
}, {
    "caption" : "spark.sql.hive.metastore.jars",
    "value" : "spark.sql.hive.metastore.jars = builtin",
    "meta" : "default: builtin",
    "version" : "1.4.0",
    "docHTML" : "\n Location of the jars that should be used to instantiate the HiveMetastoreClient.\n This property can be one of four options:\n 1. \"builtin\"\n   Use Hive 2.3.9, which is bundled with the Spark assembly when\n   <code>-Phive</code> is enabled. When this option is chosen,\n   <code>spark.sql.hive.metastore.version</code> must be either\n   <code>2.3.9</code> or not defined.\n 2. \"maven\"\n   Use Hive jars of specified version downloaded from Maven repositories.\n 3. \"path\"\n   Use Hive jars configured by `spark.sql.hive.metastore.jars.path`\n   in comma separated format. Support both local or remote paths.The provided jars\n   should be the same version as `spark.sql.hive.metastore.version`.\n 4. A classpath in the standard format for both Hive and Hadoop. The provided jars\n   should be the same version as `spark.sql.hive.metastore.version`.\n      "
}, {
    "caption" : "spark.sql.hive.metastore.jars.path",
    "value" : "spark.sql.hive.metastore.jars.path = ",
    "version" : "3.1.0",
    "docHTML" : "\n Comma-separated paths of the jars that used to instantiate the HiveMetastoreClient.\n This configuration is useful only when `spark.sql.hive.metastore.jars` is set as `path`.\n The paths can be any of the following format:\n 1. file://path/to/jar/foo.jar\n 2. hdfs://nameservice/path/to/jar/foo.jar\n 3. /path/to/jar/ (path without URI scheme follow conf `fs.defaultFS`'s URI schema)\n 4. [http/https/ftp]://path/to/jar/foo.jar\n Note that 1, 2, and 3 support wildcard. For example:\n 1. file://path/to/jar/*,file://path2/to/jar/*/*.jar\n 2. hdfs://nameservice/path/to/jar/*,hdfs://nameservice2/path/to/jar/*/*.jar\n      "
}, {
    "caption" : "spark.sql.hive.convertMetastoreOrc",
    "value" : "spark.sql.hive.convertMetastoreOrc = true",
    "meta" : "default: true",
    "version" : "2.0.0",
    "docHTML" : "When set to true, the built-in ORC reader and writer are used to process ORC tables created by using the HiveQL syntax, instead of Hive serde."
}, {
    "caption" : "spark.sql.hive.convertMetastoreCtas",
    "value" : "spark.sql.hive.convertMetastoreCtas = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "When set to true,  Spark will try to use built-in data source writer instead of Hive serde in CTAS. This flag is effective only if `spark.sql.hive.convertMetastoreParquet` or `spark.sql.hive.convertMetastoreOrc` is enabled respectively for Parquet and ORC formats"
}, {
    "caption" : "spark.sql.hive.thriftServer.async",
    "value" : "spark.sql.hive.thriftServer.async = true",
    "meta" : "default: true",
    "version" : "1.5.0",
    "docHTML" : "When set to true, Hive Thrift server executes SQL queries in an asynchronous way."
}, {
    "caption" : "spark.history.custom.executor.log.url.applyIncompleteApplication",
    "value" : "spark.history.custom.executor.log.url.applyIncompleteApplication = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to apply custom executor log url, as specified by spark.history.custom.executor.log.url, to incomplete application as well. Even if this is true, this still only affects the behavior of the history server, not running spark applications."
}, {
    "caption" : "spark.history.fs.safemodeCheck.interval",
    "value" : "spark.history.fs.safemodeCheck.interval = 5s",
    "meta" : "default: 5s",
    "version" : "1.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.endEventReparseChunkSize",
    "value" : "spark.history.fs.endEventReparseChunkSize = 1m",
    "meta" : "default: 1m",
    "version" : "2.4.0",
    "docHTML" : "How many bytes to parse at the end of log files looking for the end event. This is used to speed up generation of application listings by skipping unnecessary parts of event log files. It can be disabled by setting this config to 0."
}, {
    "caption" : "spark.history.fs.eventLog.rolling.maxFilesToRetain",
    "value" : "spark.history.fs.eventLog.rolling.maxFilesToRetain = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.0.0",
    "docHTML" : "The maximum number of event log files which will be retained as non-compacted. By default, all event log files will be retained. Please set the configuration and spark.eventLog.rolling.maxFileSize accordingly if you want to control the overall size of event log files."
}, {
    "caption" : "spark.history.fs.eventLog.rolling.compaction.score.threshold",
    "value" : "spark.history.fs.eventLog.rolling.compaction.score.threshold = 0.7",
    "meta" : "default: 0.7",
    "version" : "3.0.0",
    "docHTML" : "The threshold score to determine whether it's good to do the compaction or not. The compaction score is calculated in analyzing, and being compared to this value. Compaction will proceed only when the score is higher than the threshold value."
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.enabled",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.interval",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.acls.enable",
    "value" : "spark.history.ui.acls.enable = false",
    "meta" : "default: false",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.admin.acls",
    "value" : "spark.history.ui.admin.acls = ",
    "version" : "2.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.admin.acls.groups",
    "value" : "spark.history.ui.admin.acls.groups = ",
    "version" : "2.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.store.hybridStore.maxMemoryUsage",
    "value" : "spark.history.store.hybridStore.maxMemoryUsage = 2g",
    "meta" : "default: 2g",
    "version" : "3.1.0",
    "docHTML" : "Maximum memory space that can be used to create HybridStore. The HybridStore co-uses the heap memory, so the heap memory should be increased through the memory option for SHS if the HybridStore is enabled."
}, {
    "caption" : "spark.history.store.hybridStore.diskBackend",
    "value" : "spark.history.store.hybridStore.diskBackend = LEVELDB",
    "meta" : "default: LEVELDB",
    "version" : "3.3.0",
    "docHTML" : "Specifies a disk-based store used in hybrid store; LEVELDB or ROCKSDB."
}, {
    "caption" : "spark.history.fs.logDirectory",
    "value" : "spark.history.fs.logDirectory = file:/tmp/spark-events",
    "meta" : "default: file:/tmp/spark-events",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.update.interval",
    "value" : "spark.history.fs.update.interval = 10s",
    "meta" : "default: 10s",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.enabled",
    "value" : "spark.history.fs.cleaner.enabled = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.interval",
    "value" : "spark.history.fs.cleaner.interval = 1d",
    "meta" : "default: 1d",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.maxAge",
    "value" : "spark.history.fs.cleaner.maxAge = 7d",
    "meta" : "default: 7d",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.maxNum",
    "value" : "spark.history.fs.cleaner.maxNum = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.0.0",
    "docHTML" : "The maximum number of log files in the event log directory."
}, {
    "caption" : "spark.history.store.maxDiskUsage",
    "value" : "spark.history.store.maxDiskUsage = 10g",
    "meta" : "default: 10g",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.port",
    "value" : "spark.history.ui.port = 18080",
    "meta" : "default: 18080",
    "version" : "1.0.0",
    "docHTML" : "Web UI port to bind Spark History Server"
}, {
    "caption" : "spark.history.fs.inProgressOptimization.enabled",
    "value" : "spark.history.fs.inProgressOptimization.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "Enable optimized handling of in-progress logs. This option may leave finished applications that fail to rename their event logs listed as in-progress."
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.maxAge",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.numReplayThreads",
    "value" : "spark.history.fs.numReplayThreads = 2",
    "meta" : "default: 2",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.retainedApplications",
    "value" : "spark.history.retainedApplications = 50",
    "meta" : "default: 50",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.kerberos.enabled",
    "value" : "spark.history.kerberos.enabled = false",
    "meta" : "default: false",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.store.hybridStore.enabled",
    "value" : "spark.history.store.hybridStore.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to use HybridStore as the store when parsing event logs. HybridStore will first write data to an in-memory store and having a background thread that dumps data to a disk store after the writing to in-memory store is completed."
}, {
    "caption" : "spark.kryo.registrationRequired",
    "value" : "spark.kryo.registrationRequired = false",
    "meta" : "default: false",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryoserializer.buffer",
    "value" : "spark.kryoserializer.buffer = 64k",
    "meta" : "default: 64k",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryoserializer.buffer.max",
    "value" : "spark.kryoserializer.buffer.max = 64m",
    "meta" : "default: 64m",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryo.classesToRegister",
    "value" : "spark.kryo.classesToRegister = ",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryo.registrator",
    "value" : "spark.kryo.registrator = ",
    "version" : "0.5.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryo.unsafe",
    "value" : "spark.kryo.unsafe = false",
    "meta" : "default: false",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryo.pool",
    "value" : "spark.kryo.pool = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.kryo.referenceTracking",
    "value" : "spark.kryo.referenceTracking = true",
    "meta" : "default: true",
    "version" : "0.8.0",
    "docHTML" : ""
}, {
    "caption" : "spark.python.authenticate.socketTimeout",
    "value" : "spark.python.authenticate.socketTimeout = 15s",
    "meta" : "default: 15s",
    "version" : "3.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.python.worker.faulthandler.enabled",
    "value" : "spark.python.worker.faulthandler.enabled = false",
    "meta" : "default: false",
    "version" : "3.2.0",
    "docHTML" : "When true, Python workers set up the faulthandler for the case when the Python worker exits unexpectedly (crashes), and shows the stack trace of the moment the Python worker crashes in the error message if captured successfully."
}, {
    "caption" : "spark.python.worker.reuse",
    "value" : "spark.python.worker.reuse = true",
    "meta" : "default: true",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.python.task.killTimeout",
    "value" : "spark.python.task.killTimeout = 2s",
    "meta" : "default: 2s",
    "version" : "2.2.2",
    "docHTML" : ""
}, {
    "caption" : "spark.python.use.daemon",
    "value" : "spark.python.use.daemon = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.consoleProgress.update.interval",
    "value" : "spark.ui.consoleProgress.update.interval = 200ms",
    "meta" : "default: 200ms",
    "version" : "2.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.xContentTypeOptions.enabled",
    "value" : "spark.ui.xContentTypeOptions.enabled = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : "Set to 'true' for setting X-Content-Type-Options HTTP response header to 'nosniff'"
}, {
    "caption" : "spark.ui.timeline.tasks.maximum",
    "value" : "spark.ui.timeline.tasks.maximum = 1000",
    "meta" : "default: 1000",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.timeline.stages.maximum",
    "value" : "spark.ui.timeline.stages.maximum = 500",
    "meta" : "default: 500",
    "version" : "3.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.timeline.executors.maximum",
    "value" : "spark.ui.timeline.executors.maximum = 250",
    "meta" : "default: 250",
    "version" : "3.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.master.ui.decommission.allow.mode",
    "value" : "spark.master.ui.decommission.allow.mode = LOCAL",
    "meta" : "default: LOCAL",
    "version" : "3.1.0",
    "docHTML" : "Specifies the behavior of the Master Web UI's /workers/kill endpoint. Possible choices are: `LOCAL` means allow this endpoint from IP's that are local to the machine running the Master, `DENY` means to completely disable this endpoint, `ALLOW` means to allow calling this endpoint from any IP."
}, {
    "caption" : "spark.ui.port",
    "value" : "spark.ui.port = 4040",
    "meta" : "default: 4040",
    "version" : "0.7.0",
    "docHTML" : "Port for your application's dashboard, which shows memory and workload data."
}, {
    "caption" : "spark.user.groups.mapping",
    "value" : "spark.user.groups.mapping = org.apache.spark.security.ShellBasedGroupsMappingProvider",
    "meta" : "default: org.apache.spark.security.ShellBasedGroupsMappingProvider",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.filters",
    "value" : "spark.ui.filters = ",
    "version" : "1.0.0",
    "docHTML" : "Comma separated list of filter class names to apply to the Spark Web UI."
}, {
    "caption" : "spark.ui.killEnabled",
    "value" : "spark.ui.killEnabled = true",
    "meta" : "default: true",
    "version" : "1.0.0",
    "docHTML" : "Allows jobs and stages to be killed from the web UI."
}, {
    "caption" : "spark.ui.threadDumpsEnabled",
    "value" : "spark.ui.threadDumpsEnabled = true",
    "meta" : "default: true",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.prometheus.enabled",
    "value" : "spark.ui.prometheus.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Expose executor metrics at /metrics/executors/prometheus. For master/worker/driver metrics, you need to configure `conf/metrics.properties`."
}, {
    "caption" : "spark.ui.xXssProtection",
    "value" : "spark.ui.xXssProtection = 1; mode=block",
    "meta" : "default: 1; mode=block",
    "version" : "2.3.0",
    "docHTML" : "Value for HTTP X-XSS-Protection response header"
}, {
    "caption" : "spark.ui.requestHeaderSize",
    "value" : "spark.ui.requestHeaderSize = 8k",
    "meta" : "default: 8k",
    "version" : "2.2.3",
    "docHTML" : "Value for HTTP request header size in bytes."
}, {
    "caption" : "spark.ui.timeline.jobs.maximum",
    "value" : "spark.ui.timeline.jobs.maximum = 500",
    "meta" : "default: 500",
    "version" : "3.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.acls.enable",
    "value" : "spark.acls.enable = false",
    "meta" : "default: false",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.view.acls",
    "value" : "spark.ui.view.acls = ",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.view.acls.groups",
    "value" : "spark.ui.view.acls.groups = ",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.admin.acls",
    "value" : "spark.admin.acls = ",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.admin.acls.groups",
    "value" : "spark.admin.acls.groups = ",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.modify.acls",
    "value" : "spark.modify.acls = ",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.modify.acls.groups",
    "value" : "spark.modify.acls.groups = ",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.showConsoleProgress",
    "value" : "spark.ui.showConsoleProgress = false",
    "meta" : "default: false",
    "version" : "1.2.1",
    "docHTML" : "When true, show the progress bar in the console."
}, {
    "caption" : "spark.ui.enabled",
    "value" : "spark.ui.enabled = true",
    "meta" : "default: true",
    "version" : "1.1.1",
    "docHTML" : "Whether to run the web UI for the Spark application."
}, {
    "caption" : "spark.ui.reverseProxy",
    "value" : "spark.ui.reverseProxy = false",
    "meta" : "default: false",
    "version" : "2.1.0",
    "docHTML" : "Enable running Spark Master as reverse proxy for worker and application UIs. In this mode, Spark master will reverse proxy the worker and application UIs to enable access without requiring direct access to their hosts. Use it with caution, as worker and application UI will not be accessible directly, you will only be able to access themthrough spark master/proxy public URL. This setting affects all the workers and application UIs running in the cluster and must be set on all the workers, drivers  and masters."
}, {
    "caption" : "spark.history.custom.executor.log.url.applyIncompleteApplication",
    "value" : "spark.history.custom.executor.log.url.applyIncompleteApplication = true",
    "meta" : "default: true",
    "version" : "3.0.0",
    "docHTML" : "Whether to apply custom executor log url, as specified by spark.history.custom.executor.log.url, to incomplete application as well. Even if this is true, this still only affects the behavior of the history server, not running spark applications."
}, {
    "caption" : "spark.history.fs.safemodeCheck.interval",
    "value" : "spark.history.fs.safemodeCheck.interval = 5s",
    "meta" : "default: 5s",
    "version" : "1.6.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.endEventReparseChunkSize",
    "value" : "spark.history.fs.endEventReparseChunkSize = 1m",
    "meta" : "default: 1m",
    "version" : "2.4.0",
    "docHTML" : "How many bytes to parse at the end of log files looking for the end event. This is used to speed up generation of application listings by skipping unnecessary parts of event log files. It can be disabled by setting this config to 0."
}, {
    "caption" : "spark.history.fs.eventLog.rolling.maxFilesToRetain",
    "value" : "spark.history.fs.eventLog.rolling.maxFilesToRetain = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.0.0",
    "docHTML" : "The maximum number of event log files which will be retained as non-compacted. By default, all event log files will be retained. Please set the configuration and spark.eventLog.rolling.maxFileSize accordingly if you want to control the overall size of event log files."
}, {
    "caption" : "spark.history.fs.eventLog.rolling.compaction.score.threshold",
    "value" : "spark.history.fs.eventLog.rolling.compaction.score.threshold = 0.7",
    "meta" : "default: 0.7",
    "version" : "3.0.0",
    "docHTML" : "The threshold score to determine whether it's good to do the compaction or not. The compaction score is calculated in analyzing, and being compared to this value. Compaction will proceed only when the score is higher than the threshold value."
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.enabled",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.interval",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.acls.enable",
    "value" : "spark.history.ui.acls.enable = false",
    "meta" : "default: false",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.admin.acls",
    "value" : "spark.history.ui.admin.acls = ",
    "version" : "2.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.admin.acls.groups",
    "value" : "spark.history.ui.admin.acls.groups = ",
    "version" : "2.1.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.store.hybridStore.maxMemoryUsage",
    "value" : "spark.history.store.hybridStore.maxMemoryUsage = 2g",
    "meta" : "default: 2g",
    "version" : "3.1.0",
    "docHTML" : "Maximum memory space that can be used to create HybridStore. The HybridStore co-uses the heap memory, so the heap memory should be increased through the memory option for SHS if the HybridStore is enabled."
}, {
    "caption" : "spark.history.store.hybridStore.diskBackend",
    "value" : "spark.history.store.hybridStore.diskBackend = LEVELDB",
    "meta" : "default: LEVELDB",
    "version" : "3.3.0",
    "docHTML" : "Specifies a disk-based store used in hybrid store; LEVELDB or ROCKSDB."
}, {
    "caption" : "spark.history.fs.logDirectory",
    "value" : "spark.history.fs.logDirectory = file:/tmp/spark-events",
    "meta" : "default: file:/tmp/spark-events",
    "version" : "1.1.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.update.interval",
    "value" : "spark.history.fs.update.interval = 10s",
    "meta" : "default: 10s",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.enabled",
    "value" : "spark.history.fs.cleaner.enabled = false",
    "meta" : "default: false",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.interval",
    "value" : "spark.history.fs.cleaner.interval = 1d",
    "meta" : "default: 1d",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.maxAge",
    "value" : "spark.history.fs.cleaner.maxAge = 7d",
    "meta" : "default: 7d",
    "version" : "1.4.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.cleaner.maxNum",
    "value" : "spark.history.fs.cleaner.maxNum = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "3.0.0",
    "docHTML" : "The maximum number of log files in the event log directory."
}, {
    "caption" : "spark.history.store.maxDiskUsage",
    "value" : "spark.history.store.maxDiskUsage = 10g",
    "meta" : "default: 10g",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.ui.port",
    "value" : "spark.history.ui.port = 18080",
    "meta" : "default: 18080",
    "version" : "1.0.0",
    "docHTML" : "Web UI port to bind Spark History Server"
}, {
    "caption" : "spark.history.fs.inProgressOptimization.enabled",
    "value" : "spark.history.fs.inProgressOptimization.enabled = true",
    "meta" : "default: true",
    "version" : "2.4.0",
    "docHTML" : "Enable optimized handling of in-progress logs. This option may leave finished applications that fail to rename their event logs listed as in-progress."
}, {
    "caption" : "spark.history.fs.driverlog.cleaner.maxAge",
    "version" : "3.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.fs.numReplayThreads",
    "value" : "spark.history.fs.numReplayThreads = 2",
    "meta" : "default: 2",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.retainedApplications",
    "value" : "spark.history.retainedApplications = 50",
    "meta" : "default: 50",
    "version" : "1.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.history.kerberos.enabled",
    "value" : "spark.history.kerberos.enabled = false",
    "meta" : "default: false",
    "version" : "1.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.history.store.hybridStore.enabled",
    "value" : "spark.history.store.hybridStore.enabled = false",
    "meta" : "default: false",
    "version" : "3.1.0",
    "docHTML" : "Whether to use HybridStore as the store when parsing event logs. HybridStore will first write data to an in-memory store and having a background thread that dumps data to a disk store after the writing to in-memory store is completed."
}, {
    "caption" : "spark.ui.liveUpdate.period",
    "value" : "spark.ui.liveUpdate.period = 100ms",
    "meta" : "default: 100ms",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.liveUpdate.minFlushPeriod",
    "value" : "spark.ui.liveUpdate.minFlushPeriod = 1s",
    "meta" : "default: 1s",
    "version" : "2.4.2",
    "docHTML" : "Minimum time elapsed before stale UI data is flushed. This avoids UI staleness when incoming task events are not fired frequently."
}, {
    "caption" : "spark.ui.retainedTasks",
    "value" : "spark.ui.retainedTasks = 100000",
    "meta" : "default: 100000",
    "version" : "2.0.1",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.retainedDeadExecutors",
    "value" : "spark.ui.retainedDeadExecutors = 100",
    "meta" : "default: 100",
    "version" : "2.0.0",
    "docHTML" : ""
}, {
    "caption" : "spark.metrics.appStatusSource.enabled",
    "value" : "spark.metrics.appStatusSource.enabled = false",
    "meta" : "default: false",
    "version" : "3.0.0",
    "docHTML" : "Whether Dropwizard/Codahale metrics will be reported for the status of the running spark app."
}, {
    "caption" : "spark.appStateStore.asyncTracking.enable",
    "value" : "spark.appStateStore.asyncTracking.enable = true",
    "meta" : "default: true",
    "version" : "2.3.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.retainedJobs",
    "value" : "spark.ui.retainedJobs = 1000",
    "meta" : "default: 1000",
    "version" : "1.2.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.retainedStages",
    "value" : "spark.ui.retainedStages = 1000",
    "meta" : "default: 1000",
    "version" : "0.9.0",
    "docHTML" : ""
}, {
    "caption" : "spark.ui.dagGraph.retainedRootRDDs",
    "value" : "spark.ui.dagGraph.retainedRootRDDs = 2147483647",
    "meta" : "default: 2147483647",
    "version" : "2.1.0",
    "docHTML" : ""
} ]
