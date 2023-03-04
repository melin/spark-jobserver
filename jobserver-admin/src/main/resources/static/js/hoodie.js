let HUDI_CONFIG_OPTIONS = [ {
    "caption" : "hoodie.database.name",
    "value" : "hoodie.database.name = ",
    "docHTML" : "Database name that will be used for incremental query.If different databases have the same table name during incremental query, we can set it to limit the table name under a specific database"
}, {
    "caption" : "hoodie.table.name",
    "value" : "hoodie.table.name = ",
    "docHTML" : "Table name that will be used for registering with Hive. Needs to be same across runs."
}, {
    "caption" : "hoodie.table.type",
    "value" : "hoodie.table.type = COPY_ON_WRITE",
    "meta" : "default: COPY_ON_WRITE",
    "docHTML" : "The table type for the underlying data, for this write. This can’t change between writes."
}, {
    "caption" : "hoodie.table.version",
    "value" : "hoodie.table.version = ZERO",
    "meta" : "default: ZERO",
    "docHTML" : "Version of table, used for running upgrade/downgrade steps between releases with potentially breaking/backwards compatible changes."
}, {
    "caption" : "hoodie.table.precombine.field",
    "value" : "hoodie.table.precombine.field = ",
    "docHTML" : "Field used in preCombining before actual write. By default, when two records have the same key value, the largest value for the precombine field determined by Object.compareTo(..), is picked."
}, {
    "caption" : "hoodie.table.partition.fields",
    "value" : "hoodie.table.partition.fields = ",
    "docHTML" : "Fields used to partition the table. Concatenated values of these fields are used as the partition path, by invoking toString()"
}, {
    "caption" : "hoodie.table.recordkey.fields",
    "value" : "hoodie.table.recordkey.fields = ",
    "docHTML" : "Columns used to uniquely identify the table. Concatenated values of these fields are used as  the record key component of HoodieKey."
}, {
    "caption" : "hoodie.table.cdc.enabled",
    "value" : "hoodie.table.cdc.enabled = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "When enable, persist the change data if necessary, and can be queried as a CDC query mode."
}, {
    "caption" : "hoodie.table.cdc.supplemental.logging.mode",
    "value" : "hoodie.table.cdc.supplemental.logging.mode = data_before_after",
    "meta" : "default: data_before_after",
    "version" : "0.13.0",
    "docHTML" : "Setting 'op_key_only' persists the 'op' and the record key only, setting 'data_before' persists the additional 'before' image, and setting 'data_before_after' persists the additional 'before' and 'after' images."
}, {
    "caption" : "hoodie.table.create.schema",
    "value" : "hoodie.table.create.schema = ",
    "docHTML" : "Schema used when creating the table, for the first time."
}, {
    "caption" : "hoodie.table.base.file.format",
    "value" : "hoodie.table.base.file.format = PARQUET",
    "meta" : "default: PARQUET",
    "docHTML" : "Base file format to store all the base file data."
}, {
    "caption" : "hoodie.table.log.file.format",
    "value" : "hoodie.table.log.file.format = HOODIE_LOG",
    "meta" : "default: HOODIE_LOG",
    "docHTML" : "Log format used for the delta logs."
}, {
    "caption" : "hoodie.timeline.layout.version",
    "value" : "hoodie.timeline.layout.version = ",
    "docHTML" : "Version of timeline used, by the table."
}, {
    "caption" : "hoodie.compaction.payload.class",
    "value" : "hoodie.compaction.payload.class = org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "meta" : "default: org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "docHTML" : "Payload class to use for performing compactions, i.e merge delta logs with current base file and then  produce a new base file."
}, {
    "caption" : "hoodie.compaction.record.merger.strategy",
    "value" : "hoodie.compaction.record.merger.strategy = eeb8d96f-b1e4-49fd-bbf8-28ac514178e5",
    "meta" : "default: eeb8d96f-b1e4-49fd-bbf8-28ac514178e5",
    "version" : "0.13.0",
    "docHTML" : "Id of merger strategy. Hudi will pick HoodieRecordMerger implementations in hoodie.datasource.write.record.merger.impls which has the same merger strategy id"
}, {
    "caption" : "hoodie.archivelog.folder",
    "value" : "hoodie.archivelog.folder = archived",
    "meta" : "default: archived",
    "docHTML" : "path under the meta folder, to store archived timeline instants at."
}, {
    "caption" : "hoodie.bootstrap.index.enable",
    "value" : "hoodie.bootstrap.index.enable = true",
    "meta" : "default: true",
    "docHTML" : "Whether or not, this is a bootstrapped table, with bootstrap base data and an mapping index defined, default true."
}, {
    "caption" : "hoodie.bootstrap.index.class",
    "value" : "hoodie.bootstrap.index.class = org.apache.hudi.common.bootstrap.index.HFileBootstrapIndex",
    "meta" : "default: org.apache.hudi.common.bootstrap.index.HFileBootstrapIndex",
    "docHTML" : "Implementation to use, for mapping base files to bootstrap base file, that contain actual data."
}, {
    "caption" : "hoodie.bootstrap.base.path",
    "value" : "hoodie.bootstrap.base.path = ",
    "docHTML" : "Base path of the dataset that needs to be bootstrapped as a Hudi table"
}, {
    "caption" : "hoodie.populate.meta.fields",
    "value" : "hoodie.populate.meta.fields = true",
    "meta" : "default: true",
    "docHTML" : "When enabled, populates all meta fields. When disabled, no meta fields are populated and incremental queries will not be functional. This is only meant to be used for append only/immutable data for batch processing"
}, {
    "caption" : "hoodie.table.keygenerator.class",
    "value" : "hoodie.table.keygenerator.class = ",
    "docHTML" : "Key Generator class property for the hoodie table"
}, {
    "caption" : "hoodie.table.timeline.timezone",
    "value" : "hoodie.table.timeline.timezone = LOCAL",
    "meta" : "default: LOCAL",
    "docHTML" : "User can set hoodie commit timeline timezone, such as utc, local and so on. local is default"
}, {
    "caption" : "hoodie.partition.metafile.use.base.format",
    "value" : "hoodie.partition.metafile.use.base.format = false",
    "meta" : "default: false",
    "docHTML" : "If true, partition metafiles are saved in the same format as base-files for this dataset (e.g. Parquet / ORC). If false (default) partition metafiles are saved as properties files."
}, {
    "caption" : "hoodie.datasource.write.drop.partition.columns",
    "value" : "hoodie.datasource.write.drop.partition.columns = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, will not write the partition columns into hudi. By default, false."
}, {
    "caption" : "hoodie.datasource.write.partitionpath.urlencode",
    "value" : "hoodie.datasource.write.partitionpath.urlencode = false",
    "meta" : "default: false",
    "docHTML" : "Should we url encode the partition path value, before creating the folder structure."
}, {
    "caption" : "hoodie.datasource.write.hive_style_partitioning",
    "value" : "hoodie.datasource.write.hive_style_partitioning = false",
    "meta" : "default: false",
    "docHTML" : "Flag to indicate whether to use Hive style partitioning.\nIf set true, the names of partition folders follow <partition_column_name>=<partition_value> format.\nBy default false (the names of partition folders are only partition values)"
}, {
    "caption" : "hoodie.table.checksum",
    "value" : "hoodie.table.checksum = ",
    "version" : "0.11.0",
    "docHTML" : "Table checksum is used to guard against partial writes in HDFS. It is added as the last entry in hoodie.properties and then used to validate while reading table config."
}, {
    "caption" : "hoodie.table.metadata.partitions.inflight",
    "value" : "hoodie.table.metadata.partitions.inflight = ",
    "version" : "0.11.0",
    "docHTML" : "Comma-separated list of metadata partitions whose building is in progress. These partitions are not yet ready for use by the readers."
}, {
    "caption" : "hoodie.table.metadata.partitions",
    "value" : "hoodie.table.metadata.partitions = ",
    "version" : "0.11.0",
    "docHTML" : "Comma-separated list of metadata partitions that have been completely built and in-sync with data table. These partitions are ready for use by the readers"
}, {
    "caption" : "hoodie.table.secondary.indexes.metadata",
    "value" : "hoodie.table.secondary.indexes.metadata = ",
    "version" : "0.13.0",
    "docHTML" : "The metadata of secondary indexes"
}, {
    "caption" : "hoodie.table.name",
    "value" : "hoodie.table.name = ",
    "docHTML" : "Table name that will be used for registering with metastores like HMS. Needs to be same across runs."
}, {
    "caption" : "hoodie.datasource.write.precombine.field",
    "value" : "hoodie.datasource.write.precombine.field = ts",
    "meta" : "default: ts",
    "docHTML" : "Field used in preCombining before actual write. When two records have the same key value, we will pick the one with the largest value for the precombine field, determined by Object.compareTo(..)"
}, {
    "caption" : "hoodie.datasource.write.payload.class",
    "value" : "hoodie.datasource.write.payload.class = org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "meta" : "default: org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "docHTML" : "Payload class used. Override this, if you like to roll your own merge logic, when upserting/inserting. This will render any value set for PRECOMBINE_FIELD_OPT_VAL in-effective"
}, {
    "caption" : "hoodie.datasource.write.record.merger.impls",
    "value" : "hoodie.datasource.write.record.merger.impls = org.apache.hudi.common.model.HoodieAvroRecordMerger",
    "meta" : "default: org.apache.hudi.common.model.HoodieAvroRecordMerger",
    "version" : "0.13.0",
    "docHTML" : "List of HoodieMerger implementations constituting Hudi's merging strategy -- based on the engine used. These merger impls will filter by hoodie.datasource.write.record.merger.strategy Hudi will pick most efficient implementation to perform merging/combining of the records (during update, reading MOR table, etc)"
}, {
    "caption" : "hoodie.datasource.write.record.merger.strategy",
    "value" : "hoodie.datasource.write.record.merger.strategy = eeb8d96f-b1e4-49fd-bbf8-28ac514178e5",
    "meta" : "default: eeb8d96f-b1e4-49fd-bbf8-28ac514178e5",
    "version" : "0.13.0",
    "docHTML" : "Id of merger strategy. Hudi will pick HoodieRecordMerger implementations in hoodie.datasource.write.record.merger.impls which has the same merger strategy id"
}, {
    "caption" : "hoodie.datasource.write.keygenerator.class",
    "value" : "hoodie.datasource.write.keygenerator.class = ",
    "docHTML" : "Key generator class, that implements `org.apache.hudi.keygen.KeyGenerator` extract a key out of incoming records."
}, {
    "caption" : "hoodie.write.executor.type",
    "value" : "hoodie.write.executor.type = SIMPLE",
    "meta" : "default: SIMPLE",
    "version" : "0.13.0",
    "docHTML" : "Set executor which orchestrates concurrent producers and consumers communicating through a message queue.BOUNDED_IN_MEMORY(default): Use LinkedBlockingQueue as a bounded in-memory queue, this queue will use extra lock to balance producers and consumerDISRUPTOR: Use disruptor which a lock free message queue as inner message, this queue may gain better writing performance if lock was the bottleneck. SIMPLE: Executor with no inner message queue and no inner lock. Consuming and writing records from iterator directly. Compared with BIM and DISRUPTOR, this queue has no need for additional memory and cpu resources due to lock or multithreading, but also lost some benefits such as speed limit. Although DISRUPTOR_EXECUTOR and SIMPLE are still in experimental."
}, {
    "caption" : "hoodie.datasource.write.keygenerator.type",
    "value" : "hoodie.datasource.write.keygenerator.type = SIMPLE",
    "meta" : "default: SIMPLE",
    "docHTML" : "Easily configure one the built-in key generators, instead of specifying the key generator class.Currently supports SIMPLE, COMPLEX, TIMESTAMP, CUSTOM, NON_PARTITION, GLOBAL_DELETE. **Note** This is being actively worked on. Please use `hoodie.datasource.write.keygenerator.class` instead."
}, {
    "caption" : "hoodie.rollback.using.markers",
    "value" : "hoodie.rollback.using.markers = true",
    "meta" : "default: true",
    "docHTML" : "Enables a more efficient mechanism for rollbacks based on the marker files generated during the writes. Turned on by default."
}, {
    "caption" : "hoodie.timeline.layout.version",
    "value" : "hoodie.timeline.layout.version = 1",
    "meta" : "default: 1",
    "version" : "0.5.1",
    "docHTML" : "Controls the layout of the timeline. Version 0 relied on renames, Version 1 (default) models the timeline as an immutable log relying only on atomic writes for object storage."
}, {
    "caption" : "hoodie.table.base.file.format",
    "value" : "hoodie.table.base.file.format = PARQUET",
    "meta" : "default: PARQUET",
    "docHTML" : "Base file format to store all the base file data."
}, {
    "caption" : "hoodie.base.path",
    "value" : "hoodie.base.path = ",
    "docHTML" : "Base path on lake storage, under which all the table data is stored. Always prefix it explicitly with the storage scheme (e.g hdfs://, s3:// etc). Hudi stores all the main meta-data about commits, savepoints, cleaning audit logs etc in .hoodie directory under this base path directory."
}, {
    "caption" : "hoodie.avro.schema",
    "value" : "hoodie.avro.schema = ",
    "docHTML" : "Schema string representing the current write schema of the table. Hudi passes this to implementations of HoodieRecordPayload to convert incoming records to avro. This is also used as the write schema evolving records during an update."
}, {
    "caption" : "hoodie.internal.schema",
    "value" : "hoodie.internal.schema = ",
    "docHTML" : "Schema string representing the latest schema of the table. Hudi passes this to implementations of evolution of schema"
}, {
    "caption" : "hoodie.schema.cache.enable",
    "value" : "hoodie.schema.cache.enable = false",
    "meta" : "default: false",
    "docHTML" : "cache query internalSchemas in driver/executor side"
}, {
    "caption" : "hoodie.avro.schema.validate",
    "value" : "hoodie.avro.schema.validate = false",
    "meta" : "default: false",
    "docHTML" : "Validate the schema used for the write against the latest schema, for backwards compatibility."
}, {
    "caption" : "hoodie.datasource.write.schema.allow.auto.evolution.column.drop",
    "value" : "hoodie.datasource.write.schema.allow.auto.evolution.column.drop = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Controls whether table's schema is allowed to automatically evolve when incoming batch's schema can have any of the columns dropped. By default, Hudi will not allow this kind of (auto) schema evolution. Set this config to true to allow table's schema to be updated automatically when columns are dropped from the new incoming batch."
}, {
    "caption" : "hoodie.insert.shuffle.parallelism",
    "value" : "hoodie.insert.shuffle.parallelism = 0",
    "meta" : "default: 0",
    "docHTML" : "Parallelism for inserting records into the table. Inserts can shuffle data before writing to tune file sizes and optimize the storage layout."
}, {
    "caption" : "hoodie.bulkinsert.shuffle.parallelism",
    "value" : "hoodie.bulkinsert.shuffle.parallelism = 0",
    "meta" : "default: 0",
    "docHTML" : "For large initial imports using bulk_insert operation, controls the parallelism to use for sort modes or custom partitioning donebefore writing records to the table."
}, {
    "caption" : "hoodie.bulkinsert.user.defined.partitioner.sort.columns",
    "value" : "hoodie.bulkinsert.user.defined.partitioner.sort.columns = ",
    "docHTML" : "Columns to sort the data by when use org.apache.hudi.execution.bulkinsert.RDDCustomColumnsSortPartitioner as user defined partitioner during bulk_insert. For example 'column1,column2'"
}, {
    "caption" : "hoodie.bulkinsert.user.defined.partitioner.class",
    "value" : "hoodie.bulkinsert.user.defined.partitioner.class = ",
    "docHTML" : "If specified, this class will be used to re-partition records before they are bulk inserted. This can be used to sort, pack, cluster data optimally for common query patterns. For now we support a build-in user defined bulkinsert partitioner org.apache.hudi.execution.bulkinsert.RDDCustomColumnsSortPartitioner which can does sorting based on specified column values set by hoodie.bulkinsert.user.defined.partitioner.sort.columns"
}, {
    "caption" : "hoodie.upsert.shuffle.parallelism",
    "value" : "hoodie.upsert.shuffle.parallelism = 0",
    "meta" : "default: 0",
    "docHTML" : "Parallelism to use for upsert operation on the table. Upserts can shuffle data to perform index lookups, file sizing, bin packing records optimallyinto file groups."
}, {
    "caption" : "hoodie.delete.shuffle.parallelism",
    "value" : "hoodie.delete.shuffle.parallelism = 0",
    "meta" : "default: 0",
    "docHTML" : "Parallelism used for “delete” operation. Delete operations also performs shuffles, similar to upsert operation."
}, {
    "caption" : "hoodie.rollback.parallelism",
    "value" : "hoodie.rollback.parallelism = 100",
    "meta" : "default: 100",
    "docHTML" : "Parallelism for rollback of commits. Rollbacks perform delete of files or logging delete blocks to file groups on storage in parallel."
}, {
    "caption" : "hoodie.write.buffer.limit.bytes",
    "value" : "hoodie.write.buffer.limit.bytes = 4194304",
    "meta" : "default: 4194304",
    "docHTML" : "Size of in-memory buffer used for parallelizing network reads and lake storage writes."
}, {
    "caption" : "hoodie.write.executor.disruptor.buffer.limit.bytes",
    "value" : "hoodie.write.executor.disruptor.buffer.limit.bytes = 1024",
    "meta" : "default: 1024",
    "version" : "0.13.0",
    "docHTML" : "The size of the Disruptor Executor ring buffer, must be power of 2"
}, {
    "caption" : "hoodie.write.executor.disruptor.wait.strategy",
    "value" : "hoodie.write.executor.disruptor.wait.strategy = BLOCKING_WAIT",
    "meta" : "default: BLOCKING_WAIT",
    "version" : "0.13.0",
    "docHTML" : "Strategy employed for making Disruptor Executor wait on a cursor. Other options are SLEEPING_WAIT, it attempts to be conservative with CPU usage by using a simple busy wait loopYIELDING_WAIT, it is designed for cases where there is the option to burn CPU cycles with the goal of improving latencyBUSY_SPIN_WAIT, it can be used in low-latency systems, but puts the highest constraints on the deployment environment"
}, {
    "caption" : "hoodie.combine.before.insert",
    "value" : "hoodie.combine.before.insert = false",
    "meta" : "default: false",
    "docHTML" : "When inserted records share same key, controls whether they should be first combined (i.e de-duplicated) before writing to storage."
}, {
    "caption" : "hoodie.combine.before.upsert",
    "value" : "hoodie.combine.before.upsert = true",
    "meta" : "default: true",
    "docHTML" : "When upserted records share same key, controls whether they should be first combined (i.e de-duplicated) before writing to storage. This should be turned off only if you are absolutely certain that there are no duplicates incoming,  otherwise it can lead to duplicate keys and violate the uniqueness guarantees."
}, {
    "caption" : "hoodie.combine.before.delete",
    "value" : "hoodie.combine.before.delete = true",
    "meta" : "default: true",
    "docHTML" : "During delete operations, controls whether we should combine deletes (and potentially also upserts) before  writing to storage."
}, {
    "caption" : "hoodie.write.status.storage.level",
    "value" : "hoodie.write.status.storage.level = MEMORY_AND_DISK_SER",
    "meta" : "default: MEMORY_AND_DISK_SER",
    "docHTML" : "Write status objects hold metadata about a write (stats, errors), that is not yet committed to storage. This controls the how that information is cached for inspection by clients. We rarely expect this to be changed."
}, {
    "caption" : "hoodie.auto.commit",
    "value" : "hoodie.auto.commit = true",
    "meta" : "default: true",
    "docHTML" : "Controls whether a write operation should auto commit. This can be turned off to perform inspection of the uncommitted write before deciding to commit."
}, {
    "caption" : "hoodie.writestatus.class",
    "value" : "hoodie.writestatus.class = org.apache.hudi.client.WriteStatus",
    "meta" : "default: org.apache.hudi.client.WriteStatus",
    "docHTML" : "Subclass of org.apache.hudi.client.WriteStatus to be used to collect information about a write. Can be overridden to collection additional metrics/statistics about the data if needed."
}, {
    "caption" : "hoodie.finalize.write.parallelism",
    "value" : "hoodie.finalize.write.parallelism = 200",
    "meta" : "default: 200",
    "docHTML" : "Parallelism for the write finalization internal operation, which involves removing any partially written files from lake storage, before committing the write. Reduce this value, if the high number of tasks incur delays for smaller tables or low latency writes."
}, {
    "caption" : "hoodie.write.markers.type",
    "value" : "hoodie.write.markers.type = TIMELINE_SERVER_BASED",
    "meta" : "default: TIMELINE_SERVER_BASED",
    "version" : "0.9.0",
    "docHTML" : "Marker type to use.  Two modes are supported: - DIRECT: individual marker file corresponding to each data file is directly created by the writer. - TIMELINE_SERVER_BASED: marker operations are all handled at the timeline service which serves as a proxy.  New marker entries are batch processed and stored in a limited number of underlying files for efficiency.  If HDFS is used or timeline server is disabled, DIRECT markers are used as fallback even if this is configure.  For Spark structured streaming, this configuration does not take effect, i.e., DIRECT markers are always used for Spark structured streaming."
}, {
    "caption" : "hoodie.markers.timeline_server_based.batch.num_threads",
    "value" : "hoodie.markers.timeline_server_based.batch.num_threads = 20",
    "meta" : "default: 20",
    "version" : "0.9.0",
    "docHTML" : "Number of threads to use for batch processing marker creation requests at the timeline server"
}, {
    "caption" : "hoodie.markers.timeline_server_based.batch.interval_ms",
    "value" : "hoodie.markers.timeline_server_based.batch.interval_ms = 50",
    "meta" : "default: 50",
    "version" : "0.9.0",
    "docHTML" : "The batch interval in milliseconds for marker creation batch processing"
}, {
    "caption" : "hoodie.markers.delete.parallelism",
    "value" : "hoodie.markers.delete.parallelism = 100",
    "meta" : "default: 100",
    "docHTML" : "Determines the parallelism for deleting marker files, which are used to track all files (valid or invalid/partial) written during a write operation. Increase this value if delays are observed, with large batch writes."
}, {
    "caption" : "hoodie.bulkinsert.sort.mode",
    "value" : "hoodie.bulkinsert.sort.mode = NONE",
    "meta" : "default: NONE",
    "docHTML" : "Sorting modes to use for sorting records for bulk insert. This is use when user hoodie.bulkinsert.user.defined.partitioner.classis not configured. Available values are - GLOBAL_SORT: this ensures best file sizes, with lowest memory overhead at cost of sorting. PARTITION_SORT: Strikes a balance by only sorting within a partition, still keeping the memory overhead of writing lowest and best effort file sizing. PARTITION_PATH_REPARTITION: this ensures that the data for a single physical partition in the table is written by the same Spark executor, best for input data evenly distributed across different partition paths. This can cause imbalance among Spark executors if the input data is skewed, i.e., most records are intended for a handful of partition paths among all. PARTITION_PATH_REPARTITION_AND_SORT: this ensures that the data for a single physical partition in the table is written by the same Spark executor, best for input data evenly distributed across different partition paths. Compared to PARTITION_PATH_REPARTITION, this sort mode does an additional step of sorting the records based on the partition path within a single Spark partition, given that data for multiple physical partitions can be sent to the same Spark partition and executor. This can cause imbalance among Spark executors if the input data is skewed, i.e., most records are intended for a handful of partition paths among all. NONE: No sorting. Fastest and matches `spark.write.parquet()` in terms of number of files, overheads"
}, {
    "caption" : "hoodie.embed.timeline.server",
    "value" : "hoodie.embed.timeline.server = true",
    "meta" : "default: true",
    "docHTML" : "When true, spins up an instance of the timeline server (meta server that serves cached file listings, statistics),running on each writer's driver process, accepting requests during the write from executors."
}, {
    "caption" : "hoodie.embed.timeline.server.reuse.enabled",
    "value" : "hoodie.embed.timeline.server.reuse.enabled = false",
    "meta" : "default: false",
    "docHTML" : "Controls whether the timeline server instance should be cached and reused across the JVM (across task lifecycles)to avoid startup costs. This should rarely be changed."
}, {
    "caption" : "hoodie.embed.timeline.server.port",
    "value" : "hoodie.embed.timeline.server.port = 0",
    "meta" : "default: 0",
    "docHTML" : "Port at which the timeline server listens for requests. When running embedded in each writer, it picks a free port and communicates to all the executors. This should rarely be changed."
}, {
    "caption" : "hoodie.embed.timeline.server.threads",
    "value" : "hoodie.embed.timeline.server.threads = -1",
    "meta" : "default: -1",
    "docHTML" : "Number of threads to serve requests in the timeline server. By default, auto configured based on the number of underlying cores."
}, {
    "caption" : "hoodie.embed.timeline.server.gzip",
    "value" : "hoodie.embed.timeline.server.gzip = true",
    "meta" : "default: true",
    "docHTML" : "Controls whether gzip compression is used, for large responses from the timeline server, to improve latency."
}, {
    "caption" : "hoodie.embed.timeline.server.async",
    "value" : "hoodie.embed.timeline.server.async = false",
    "meta" : "default: false",
    "docHTML" : "Controls whether or not, the requests to the timeline server are processed in asynchronous fashion, potentially improving throughput."
}, {
    "caption" : "hoodie.fail.on.timeline.archiving",
    "value" : "hoodie.fail.on.timeline.archiving = true",
    "meta" : "default: true",
    "docHTML" : "Timeline archiving removes older instants from the timeline, after each write operation, to minimize metadata overhead. Controls whether or not, the write should be failed as well, if such archiving fails."
}, {
    "caption" : "hoodie.fail.writes.on.inline.table.service.exception",
    "value" : "hoodie.fail.writes.on.inline.table.service.exception = true",
    "meta" : "default: true",
    "version" : "0.13.0",
    "docHTML" : "Table services such as compaction and clustering can fail and prevent syncing to the metaclient. Set this to true to fail writes when table services fail"
}, {
    "caption" : "hoodie.consistency.check.initial_interval_ms",
    "value" : "hoodie.consistency.check.initial_interval_ms = 2000",
    "meta" : "default: 2000",
    "docHTML" : "Initial time between successive attempts to ensure written data's metadata is consistent on storage. Grows with exponential backoff after the initial value."
}, {
    "caption" : "hoodie.consistency.check.max_interval_ms",
    "value" : "hoodie.consistency.check.max_interval_ms = 300000",
    "meta" : "default: 300000",
    "docHTML" : "Max time to wait between successive attempts at performing consistency checks"
}, {
    "caption" : "hoodie.consistency.check.max_checks",
    "value" : "hoodie.consistency.check.max_checks = 7",
    "meta" : "default: 7",
    "docHTML" : "Maximum number of checks, for consistency of written data."
}, {
    "caption" : "hoodie.merge.data.validation.enabled",
    "value" : "hoodie.merge.data.validation.enabled = false",
    "meta" : "default: false",
    "docHTML" : "When enabled, data validation checks are performed during merges to ensure expected number of records after merge operation."
}, {
    "caption" : "hoodie.merge.allow.duplicate.on.inserts",
    "value" : "hoodie.merge.allow.duplicate.on.inserts = false",
    "meta" : "default: false",
    "docHTML" : "When enabled, we allow duplicate keys even if inserts are routed to merge with an existing file (for ensuring file sizing). This is only relevant for insert operation, since upsert, delete operations will ensure unique key constraints are maintained."
}, {
    "caption" : "hoodie.merge.small.file.group.candidates.limit",
    "value" : "hoodie.merge.small.file.group.candidates.limit = 1",
    "meta" : "default: 1",
    "docHTML" : "Limits number of file groups, whose base file satisfies small-file limit, to consider for appending records during upsert operation. Only applicable to MOR tables"
}, {
    "caption" : "hoodie.client.heartbeat.interval_in_ms",
    "value" : "hoodie.client.heartbeat.interval_in_ms = 60000",
    "meta" : "default: 60000",
    "docHTML" : "Writers perform heartbeats to indicate liveness. Controls how often (in ms), such heartbeats are registered to lake storage."
}, {
    "caption" : "hoodie.client.heartbeat.tolerable.misses",
    "value" : "hoodie.client.heartbeat.tolerable.misses = 2",
    "meta" : "default: 2",
    "docHTML" : "Number of heartbeat misses, before a writer is deemed not alive and all pending writes are aborted."
}, {
    "caption" : "hoodie.write.concurrency.mode",
    "value" : "hoodie.write.concurrency.mode = SINGLE_WRITER",
    "meta" : "default: SINGLE_WRITER",
    "docHTML" : "Enable different concurrency modes. Options are SINGLE_WRITER: Only one active writer to the table. Maximizes throughputOPTIMISTIC_CONCURRENCY_CONTROL: Multiple writers can operate on the table and exactly one of them succeed if a conflict (writes affect the same file group) is detected."
}, {
    "caption" : "hoodie.write.schema",
    "value" : "hoodie.write.schema = ",
    "docHTML" : "Config allowing to override writer's schema. This might be necessary in cases when writer's schema derived from the incoming dataset might actually be different from the schema we actually want to use when writing. This, for ex, could be the case for'partial-update' use-cases (like `MERGE INTO` Spark SQL statement for ex) where only a projection of the incoming dataset might be used to update the records in the existing table, prompting us to override the writer's schema"
}, {
    "caption" : "_.hoodie.allow.multi.write.on.same.instant",
    "value" : "_.hoodie.allow.multi.write.on.same.instant = false",
    "meta" : "default: false",
    "docHTML" : ""
}, {
    "caption" : "hoodie.avro.schema.external.transformation",
    "value" : "hoodie.avro.schema.external.transformation = false",
    "meta" : "default: false",
    "docHTML" : "When enabled, records in older schema are rewritten into newer schema during upsert,delete and background compaction,clustering operations."
}, {
    "caption" : "hoodie.allow.empty.commit",
    "value" : "hoodie.allow.empty.commit = true",
    "meta" : "default: true",
    "docHTML" : "Whether to allow generation of empty commits, even if no data was written in the commit. It's useful in cases where extra metadata needs to be published regardless e.g tracking source offsets when ingesting data"
}, {
    "caption" : "hoodie.allow.operation.metadata.field",
    "value" : "hoodie.allow.operation.metadata.field = false",
    "meta" : "default: false",
    "version" : "0.9.0",
    "docHTML" : "Whether to include '_hoodie_operation' in the metadata fields. Once enabled, all the changes of a record are persisted to the delta log directly without merge"
}, {
    "caption" : "hoodie.fileid.prefix.provider.class",
    "value" : "hoodie.fileid.prefix.provider.class = org.apache.hudi.table.RandomFileIdPrefixProvider",
    "meta" : "default: org.apache.hudi.table.RandomFileIdPrefixProvider",
    "version" : "0.10.0",
    "docHTML" : "File Id Prefix provider class, that implements `org.apache.hudi.fileid.FileIdPrefixProvider`"
}, {
    "caption" : "hoodie.table.services.enabled",
    "value" : "hoodie.table.services.enabled = true",
    "meta" : "default: true",
    "version" : "0.11.0",
    "docHTML" : "Master control to disable all table services including archive, clean, compact, cluster, etc."
}, {
    "caption" : "hoodie.release.resource.on.completion.enable",
    "value" : "hoodie.release.resource.on.completion.enable = true",
    "meta" : "default: true",
    "version" : "0.11.0",
    "docHTML" : "Control to enable release all persist rdds when the spark job finish."
}, {
    "caption" : "hoodie.auto.adjust.lock.configs",
    "value" : "hoodie.auto.adjust.lock.configs = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Auto adjust lock configurations when metadata table is enabled and for async table services."
}, {
    "caption" : "hoodie.skip.default.partition.validation",
    "value" : "hoodie.skip.default.partition.validation = false",
    "meta" : "default: false",
    "version" : "0.12.0",
    "docHTML" : "When table is upgraded from pre 0.12 to 0.12, we check for \"default\" partition and fail if found one. Users are expected to rewrite the data in those partitions. Enabling this config will bypass this validation"
}, {
    "caption" : "hoodie.write.concurrency.early.conflict.detection.strategy",
    "value" : "hoodie.write.concurrency.early.conflict.detection.strategy = ",
    "version" : "0.13.0",
    "docHTML" : "The class name of the early conflict detection strategy to use. This should be a subclass of `org.apache.hudi.common.conflict.detection.EarlyConflictDetectionStrategy`."
}, {
    "caption" : "hoodie.write.concurrency.early.conflict.detection.enable",
    "value" : "hoodie.write.concurrency.early.conflict.detection.enable = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Whether to enable early conflict detection based on markers. It eagerly detects writing conflict before create markers and fails fast if a conflict is detected, to release cluster compute resources as soon as possible."
}, {
    "caption" : "hoodie.write.concurrency.async.conflict.detector.initial_delay_ms",
    "value" : "hoodie.write.concurrency.async.conflict.detector.initial_delay_ms = 0",
    "meta" : "default: 0",
    "version" : "0.13.0",
    "docHTML" : "Used for timeline-server-based markers with `AsyncTimelineServerBasedDetectionStrategy`. The time in milliseconds to delay the first execution of async marker-based conflict detection."
}, {
    "caption" : "hoodie.write.concurrency.async.conflict.detector.period_ms",
    "value" : "hoodie.write.concurrency.async.conflict.detector.period_ms = 30000",
    "meta" : "default: 30000",
    "version" : "0.13.0",
    "docHTML" : "Used for timeline-server-based markers with `AsyncTimelineServerBasedDetectionStrategy`. The period in milliseconds between successive executions of async marker-based conflict detection."
}, {
    "caption" : "hoodie.write.concurrency.early.conflict.check.commit.conflict",
    "value" : "hoodie.write.concurrency.early.conflict.check.commit.conflict = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Whether to enable commit conflict checking or not during early conflict detection."
}, {
    "caption" : "hoodie.archive.automatic",
    "value" : "hoodie.archive.automatic = true",
    "meta" : "default: true",
    "docHTML" : "When enabled, the archival table service is invoked immediately after each commit, to archive commits if we cross a maximum value of commits. It's recommended to enable this, to ensure number of active commits is bounded."
}, {
    "caption" : "hoodie.archive.async",
    "value" : "hoodie.archive.async = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Only applies when hoodie.archive.automatic is turned on. When turned on runs archiver async with writing, which can speed up overall write performance."
}, {
    "caption" : "hoodie.keep.max.commits",
    "value" : "hoodie.keep.max.commits = 30",
    "meta" : "default: 30",
    "docHTML" : "Archiving service moves older entries from timeline into an archived log after each write, to keep the metadata overhead constant, even as the table size grows. This config controls the maximum number of instants to retain in the active timeline. "
}, {
    "caption" : "hoodie.archive.delete.parallelism",
    "value" : "hoodie.archive.delete.parallelism = 100",
    "meta" : "default: 100",
    "docHTML" : "Parallelism for deleting archived hoodie commits."
}, {
    "caption" : "hoodie.keep.min.commits",
    "value" : "hoodie.keep.min.commits = 20",
    "meta" : "default: 20",
    "docHTML" : "Similar to hoodie.keep.max.commits, but controls the minimum number of instants to retain in the active timeline."
}, {
    "caption" : "hoodie.commits.archival.batch",
    "value" : "hoodie.commits.archival.batch = 10",
    "meta" : "default: 10",
    "docHTML" : "Archiving of instants is batched in best-effort manner, to pack more instants into a single archive log. This config controls such archival batch size."
}, {
    "caption" : "hoodie.archive.merge.files.batch.size",
    "value" : "hoodie.archive.merge.files.batch.size = 10",
    "meta" : "default: 10",
    "docHTML" : "The number of small archive files to be merged at once."
}, {
    "caption" : "hoodie.archive.merge.small.file.limit.bytes",
    "value" : "hoodie.archive.merge.small.file.limit.bytes = 20971520",
    "meta" : "default: 20971520",
    "docHTML" : "This config sets the archive file size limit below which an archive file becomes a candidate to be selected as such a small file."
}, {
    "caption" : "hoodie.archive.merge.enable",
    "value" : "hoodie.archive.merge.enable = false",
    "meta" : "default: false",
    "docHTML" : "When enable, hoodie will auto merge several small archive files into larger one. It's useful when storage scheme doesn't support append operation."
}, {
    "caption" : "hoodie.archive.beyond.savepoint",
    "value" : "hoodie.archive.beyond.savepoint = false",
    "meta" : "default: false",
    "version" : "0.12.0",
    "docHTML" : "If enabled, archival will proceed beyond savepoint, skipping savepoint commits. If disabled, archival will stop at the earliest savepoint commit."
}, {
    "caption" : "hoodie.meta_sync.global.replicate.timestamp",
    "value" : "hoodie.meta_sync.global.replicate.timestamp = ",
    "docHTML" : ""
}, {
    "caption" : "hoodie.datasource.hive_sync.enable",
    "value" : "hoodie.datasource.hive_sync.enable = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, register/sync the table to Apache Hive metastore."
}, {
    "caption" : "hoodie.datasource.hive_sync.username",
    "value" : "hoodie.datasource.hive_sync.username = hive",
    "meta" : "default: hive",
    "docHTML" : "hive user name to use"
}, {
    "caption" : "hoodie.datasource.hive_sync.password",
    "value" : "hoodie.datasource.hive_sync.password = hive",
    "meta" : "default: hive",
    "docHTML" : "hive password to use"
}, {
    "caption" : "hoodie.datasource.hive_sync.jdbcurl",
    "value" : "hoodie.datasource.hive_sync.jdbcurl = jdbc:hive2://localhost:10000",
    "meta" : "default: jdbc:hive2://localhost:10000",
    "docHTML" : "Hive metastore url"
}, {
    "caption" : "hoodie.datasource.hive_sync.use_pre_apache_input_format",
    "value" : "hoodie.datasource.hive_sync.use_pre_apache_input_format = false",
    "meta" : "default: false",
    "docHTML" : "Flag to choose InputFormat under com.uber.hoodie package instead of org.apache.hudi package. Use this when you are in the process of migrating from com.uber.hoodie to org.apache.hudi. Stop using this after you migrated the table definition to org.apache.hudi input format"
}, {
    "caption" : "hoodie.datasource.hive_sync.use_jdbc",
    "value" : "hoodie.datasource.hive_sync.use_jdbc = true",
    "meta" : "default: true",
    "docHTML" : "Use JDBC when hive synchronization is enabled"
}, {
    "caption" : "hoodie.datasource.hive_sync.metastore.uris",
    "value" : "hoodie.datasource.hive_sync.metastore.uris = thrift://localhost:9083",
    "meta" : "default: thrift://localhost:9083",
    "docHTML" : "Hive metastore url"
}, {
    "caption" : "hoodie.datasource.hive_sync.auto_create_database",
    "value" : "hoodie.datasource.hive_sync.auto_create_database = true",
    "meta" : "default: true",
    "docHTML" : "Auto create hive database if does not exists"
}, {
    "caption" : "hoodie.datasource.hive_sync.ignore_exceptions",
    "value" : "hoodie.datasource.hive_sync.ignore_exceptions = false",
    "meta" : "default: false",
    "docHTML" : "Ignore exceptions when syncing with Hive."
}, {
    "caption" : "hoodie.datasource.hive_sync.skip_ro_suffix",
    "value" : "hoodie.datasource.hive_sync.skip_ro_suffix = false",
    "meta" : "default: false",
    "docHTML" : "Skip the _ro suffix for Read optimized table, when registering"
}, {
    "caption" : "hoodie.datasource.hive_sync.support_timestamp",
    "value" : "hoodie.datasource.hive_sync.support_timestamp = false",
    "meta" : "default: false",
    "docHTML" : "‘INT64’ with original type TIMESTAMP_MICROS is converted to hive ‘timestamp’ type. Disabled by default for backward compatibility."
}, {
    "caption" : "hoodie.datasource.hive_sync.table_properties",
    "value" : "hoodie.datasource.hive_sync.table_properties = ",
    "docHTML" : "Additional properties to store with table."
}, {
    "caption" : "hoodie.datasource.hive_sync.serde_properties",
    "value" : "hoodie.datasource.hive_sync.serde_properties = ",
    "docHTML" : "Serde properties to hive table."
}, {
    "caption" : "hoodie.datasource.hive_sync.sync_as_datasource",
    "value" : "hoodie.datasource.hive_sync.sync_as_datasource = true",
    "meta" : "default: true",
    "docHTML" : ""
}, {
    "caption" : "hoodie.datasource.hive_sync.schema_string_length_thresh",
    "value" : "hoodie.datasource.hive_sync.schema_string_length_thresh = 4000",
    "meta" : "default: 4000",
    "docHTML" : ""
}, {
    "caption" : "hoodie.datasource.hive_sync.create_managed_table",
    "value" : "hoodie.datasource.hive_sync.create_managed_table = false",
    "meta" : "default: false",
    "docHTML" : "Whether to sync the table as managed table."
}, {
    "caption" : "hoodie.datasource.hive_sync.omit_metadata_fields",
    "value" : "hoodie.datasource.hive_sync.omit_metadata_fields = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Whether to omit the hoodie metadata fields in the target table."
}, {
    "caption" : "hoodie.datasource.hive_sync.batch_num",
    "value" : "hoodie.datasource.hive_sync.batch_num = 1000",
    "meta" : "default: 1000",
    "docHTML" : "The number of partitions one batch when synchronous partitions to hive."
}, {
    "caption" : "hoodie.datasource.hive_sync.mode",
    "value" : "hoodie.datasource.hive_sync.mode = ",
    "docHTML" : "Mode to choose for Hive ops. Valid values are hms, jdbc and hiveql."
}, {
    "caption" : "hoodie.datasource.hive_sync.bucket_sync",
    "value" : "hoodie.datasource.hive_sync.bucket_sync = false",
    "meta" : "default: false",
    "docHTML" : "Whether sync hive metastore bucket specification when using bucket index.The specification is 'CLUSTERED BY (trace_id) SORTED BY (trace_id ASC) INTO 65536 BUCKETS'"
}, {
    "caption" : "hoodie.datasource.hive_sync.bucket_sync_spec",
    "value" : "hoodie.datasource.hive_sync.bucket_sync_spec = ",
    "meta" : "default: ",
    "docHTML" : "The hive metastore bucket specification when using bucket index.The specification is 'CLUSTERED BY (trace_id) SORTED BY (trace_id ASC) INTO 65536 BUCKETS'"
}, {
    "caption" : "hoodie.datasource.hive_sync.sync_comment",
    "value" : "hoodie.datasource.hive_sync.sync_comment = false",
    "meta" : "default: false",
    "docHTML" : "Whether to sync the table column comments while syncing the table."
}, {
    "caption" : "hoodie.datasource.hive_sync.table.strategy",
    "value" : "hoodie.datasource.hive_sync.table.strategy = ALL",
    "meta" : "default: ALL",
    "version" : "0.13.0",
    "docHTML" : "Hive table synchronization strategy. Available option: ONLY_RO, ONLY_RT, ALL."
}, {
    "caption" : "hoodie.datasource.hive_sync.filter_pushdown_enabled",
    "value" : "hoodie.datasource.hive_sync.filter_pushdown_enabled = false",
    "meta" : "default: false",
    "docHTML" : "Whether to enable push down partitions by filter"
}, {
    "caption" : "hoodie.datasource.hive_sync.filter_pushdown_max_size",
    "value" : "hoodie.datasource.hive_sync.filter_pushdown_max_size = 1000",
    "meta" : "default: 1000",
    "docHTML" : "Max size limit to push down partition filters, if the estimate push down filters exceed this size, will directly try to fetch all partitions"
}, {
    "caption" : "hoodie.bootstrap.base.path",
    "value" : "hoodie.bootstrap.base.path = ",
    "version" : "0.6.0",
    "docHTML" : "Base path of the dataset that needs to be bootstrapped as a Hudi table"
}, {
    "caption" : "hoodie.bootstrap.mode.selector.regex.mode",
    "value" : "hoodie.bootstrap.mode.selector.regex.mode = METADATA_ONLY",
    "meta" : "default: METADATA_ONLY",
    "version" : "0.6.0",
    "docHTML" : "Bootstrap mode to apply for partition paths, that match regex above. METADATA_ONLY will generate just skeleton base files with keys/footers, avoiding full cost of rewriting the dataset. FULL_RECORD will perform a full copy/rewrite of the data as a Hudi table."
}, {
    "caption" : "hoodie.bootstrap.mode.selector",
    "value" : "hoodie.bootstrap.mode.selector = org.apache.hudi.client.bootstrap.selector.MetadataOnlyBootstrapModeSelector",
    "meta" : "default: org.apache.hudi.client.bootstrap.selector.MetadataOnlyBootstrapModeSelector",
    "version" : "0.6.0",
    "docHTML" : "Selects the mode in which each file/partition in the bootstrapped dataset gets bootstrapped"
}, {
    "caption" : "hoodie.bootstrap.full.input.provider",
    "value" : "hoodie.bootstrap.full.input.provider = org.apache.hudi.bootstrap.SparkParquetBootstrapDataProvider",
    "meta" : "default: org.apache.hudi.bootstrap.SparkParquetBootstrapDataProvider",
    "version" : "0.6.0",
    "docHTML" : "Class to use for reading the bootstrap dataset partitions/files, for Bootstrap mode FULL_RECORD"
}, {
    "caption" : "hoodie.bootstrap.keygen.class",
    "value" : "hoodie.bootstrap.keygen.class = ",
    "version" : "0.6.0",
    "docHTML" : "Key generator implementation to be used for generating keys from the bootstrapped dataset"
}, {
    "caption" : "hoodie.bootstrap.keygen.type",
    "value" : "hoodie.bootstrap.keygen.type = SIMPLE",
    "meta" : "default: SIMPLE",
    "version" : "0.9.0",
    "docHTML" : "Type of build-in key generator, currently support SIMPLE, COMPLEX, TIMESTAMP, CUSTOM, NON_PARTITION, GLOBAL_DELETE"
}, {
    "caption" : "hoodie.bootstrap.partitionpath.translator.class",
    "value" : "hoodie.bootstrap.partitionpath.translator.class = org.apache.hudi.client.bootstrap.translator.IdentityBootstrapPartitionPathTranslator",
    "meta" : "default: org.apache.hudi.client.bootstrap.translator.IdentityBootstrapPartitionPathTranslator",
    "version" : "0.6.0",
    "docHTML" : "Translates the partition paths from the bootstrapped data into how is laid out as a Hudi table."
}, {
    "caption" : "hoodie.bootstrap.parallelism",
    "value" : "hoodie.bootstrap.parallelism = 1500",
    "meta" : "default: 1500",
    "version" : "0.6.0",
    "docHTML" : "Parallelism value to be used to bootstrap data into hudi"
}, {
    "caption" : "hoodie.bootstrap.mode.selector.regex",
    "value" : "hoodie.bootstrap.mode.selector.regex = .*",
    "meta" : "default: .*",
    "version" : "0.6.0",
    "docHTML" : "Matches each bootstrap dataset partition against this regex and applies the mode below to it."
}, {
    "caption" : "hoodie.bootstrap.index.class",
    "value" : "hoodie.bootstrap.index.class = org.apache.hudi.common.bootstrap.index.HFileBootstrapIndex",
    "meta" : "default: org.apache.hudi.common.bootstrap.index.HFileBootstrapIndex",
    "version" : "0.6.0",
    "docHTML" : "Implementation to use, for mapping a skeleton base file to a boostrap base file."
}, {
    "caption" : "hoodie.clean.automatic",
    "value" : "hoodie.clean.automatic = true",
    "meta" : "default: true",
    "docHTML" : "When enabled, the cleaner table service is invoked immediately after each commit, to delete older file slices. It's recommended to enable this, to ensure metadata and data storage growth is bounded."
}, {
    "caption" : "hoodie.clean.async",
    "value" : "hoodie.clean.async = false",
    "meta" : "default: false",
    "docHTML" : "Only applies when hoodie.clean.automatic is turned on. When turned on runs cleaner async with writing, which can speed up overall write performance."
}, {
    "caption" : "hoodie.cleaner.commits.retained",
    "value" : "hoodie.cleaner.commits.retained = 10",
    "meta" : "default: 10",
    "docHTML" : "Number of commits to retain, without cleaning. This will be retained for num_of_commits * time_between_commits (scheduled). This also directly translates into how much data retention the table supports for incremental queries."
}, {
    "caption" : "hoodie.cleaner.hours.retained",
    "value" : "hoodie.cleaner.hours.retained = 24",
    "meta" : "default: 24",
    "docHTML" : "Number of hours for which commits need to be retained. This config provides a more flexible option ascompared to number of commits retained for cleaning service. Setting this property ensures all the files, but the latest in a file group, corresponding to commits with commit times older than the configured number of hours to be retained are cleaned."
}, {
    "caption" : "hoodie.cleaner.policy",
    "value" : "hoodie.cleaner.policy = KEEP_LATEST_COMMITS",
    "meta" : "default: KEEP_LATEST_COMMITS",
    "docHTML" : "Cleaning policy to be used. The cleaner service deletes older file slices files to re-claim space. By default, cleaner spares the file slices written by the last N commits, determined by  hoodie.cleaner.commits.retained Long running query plans may often refer to older file slices and will break if those are cleaned, before the query has had   a chance to run. So, it is good to make sure that the data is retained for more than the maximum query execution time"
}, {
    "caption" : "hoodie.clean.trigger.strategy",
    "value" : "hoodie.clean.trigger.strategy = NUM_COMMITS",
    "meta" : "default: NUM_COMMITS",
    "docHTML" : "Controls how cleaning is scheduled. Valid options: NUM_COMMITS"
}, {
    "caption" : "hoodie.clean.max.commits",
    "value" : "hoodie.clean.max.commits = 1",
    "meta" : "default: 1",
    "docHTML" : "Number of commits after the last clean operation, before scheduling of a new clean is attempted."
}, {
    "caption" : "hoodie.cleaner.fileversions.retained",
    "value" : "hoodie.cleaner.fileversions.retained = 3",
    "meta" : "default: 3",
    "docHTML" : "When KEEP_LATEST_FILE_VERSIONS cleaning policy is used,  the minimum number of file slices to retain in each file group, during cleaning."
}, {
    "caption" : "hoodie.cleaner.incremental.mode",
    "value" : "hoodie.cleaner.incremental.mode = true",
    "meta" : "default: true",
    "docHTML" : "When enabled, the plans for each cleaner service run is computed incrementally off the events  in the timeline, since the last cleaner run. This is much more efficient than obtaining listings for the full table for each planning (even with a metadata table)."
}, {
    "caption" : "hoodie.cleaner.policy.failed.writes",
    "value" : "hoodie.cleaner.policy.failed.writes = EAGER",
    "meta" : "default: EAGER",
    "docHTML" : "Cleaning policy for failed writes to be used. Hudi will delete any files written by failed writes to re-claim space. Choose to perform this rollback of failed writes eagerly before every writer starts (only supported for single writer) or lazily by the cleaner (required for multi-writers)"
}, {
    "caption" : "hoodie.cleaner.parallelism",
    "value" : "hoodie.cleaner.parallelism = 200",
    "meta" : "default: 200",
    "docHTML" : "Parallelism for the cleaning operation. Increase this if cleaning becomes slow."
}, {
    "caption" : "hoodie.clean.allow.multiple",
    "value" : "hoodie.clean.allow.multiple = true",
    "meta" : "default: true",
    "version" : "0.11.0",
    "docHTML" : "Allows scheduling/executing multiple cleans by enabling this config. If users prefer to strictly ensure clean requests should be mutually exclusive, .i.e. a 2nd clean will not be scheduled if another clean is not yet completed to avoid repeat cleaning of same files, they might want to disable this config."
}, {
    "caption" : "hoodie.cleaner.delete.bootstrap.base.file",
    "value" : "hoodie.cleaner.delete.bootstrap.base.file = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, cleaner also deletes the bootstrap base file when it's skeleton base file is  cleaned. Turn this to true, if you want to ensure the bootstrap dataset storage is reclaimed over time, as the table receives updates/deletes. Another reason to turn this on, would be to ensure data residing in bootstrap  base files are also physically deleted, to comply with data privacy enforcement processes."
}, {
    "caption" : "hoodie.clustering.plan.strategy.daybased.lookback.partitions",
    "value" : "hoodie.clustering.plan.strategy.daybased.lookback.partitions = 2",
    "meta" : "default: 2",
    "version" : "0.7.0",
    "docHTML" : "Number of partitions to list to create ClusteringPlan"
}, {
    "caption" : "hoodie.clustering.plan.strategy.cluster.begin.partition",
    "value" : "hoodie.clustering.plan.strategy.cluster.begin.partition = ",
    "version" : "0.11.0",
    "docHTML" : "Begin partition used to filter partition (inclusive), only effective when the filter mode 'hoodie.clustering.plan.partition.filter.mode' is SELECTED_PARTITIONS"
}, {
    "caption" : "hoodie.clustering.plan.strategy.cluster.end.partition",
    "value" : "hoodie.clustering.plan.strategy.cluster.end.partition = ",
    "version" : "0.11.0",
    "docHTML" : "End partition used to filter partition (inclusive), only effective when the filter mode 'hoodie.clustering.plan.partition.filter.mode' is SELECTED_PARTITIONS"
}, {
    "caption" : "hoodie.clustering.plan.strategy.small.file.limit",
    "value" : "hoodie.clustering.plan.strategy.small.file.limit = 314572800",
    "meta" : "default: 314572800",
    "version" : "0.7.0",
    "docHTML" : "Files smaller than the size in bytes specified here are candidates for clustering"
}, {
    "caption" : "hoodie.clustering.plan.strategy.partition.regex.pattern",
    "value" : "hoodie.clustering.plan.strategy.partition.regex.pattern = ",
    "version" : "0.11.0",
    "docHTML" : "Filter clustering partitions that matched regex pattern"
}, {
    "caption" : "hoodie.clustering.plan.strategy.partition.selected",
    "value" : "hoodie.clustering.plan.strategy.partition.selected = ",
    "version" : "0.11.0",
    "docHTML" : "Partitions to run clustering"
}, {
    "caption" : "hoodie.clustering.plan.strategy.class",
    "value" : "hoodie.clustering.plan.strategy.class = org.apache.hudi.client.clustering.plan.strategy.SparkSizeBasedClusteringPlanStrategy",
    "meta" : "default: org.apache.hudi.client.clustering.plan.strategy.SparkSizeBasedClusteringPlanStrategy",
    "version" : "0.7.0",
    "docHTML" : "Config to provide a strategy class (subclass of ClusteringPlanStrategy) to create clustering plan i.e select what file groups are being clustered. Default strategy, looks at the clustering small file size limit (determined by hoodie.clustering.plan.strategy.small.file.limit) to pick the small file slices within partitions for clustering."
}, {
    "caption" : "hoodie.clustering.execution.strategy.class",
    "value" : "hoodie.clustering.execution.strategy.class = org.apache.hudi.client.clustering.run.strategy.SparkSortAndSizeExecutionStrategy",
    "meta" : "default: org.apache.hudi.client.clustering.run.strategy.SparkSortAndSizeExecutionStrategy",
    "version" : "0.7.0",
    "docHTML" : "Config to provide a strategy class (subclass of RunClusteringStrategy) to define how the  clustering plan is executed. By default, we sort the file groups in th plan by the specified columns, while  meeting the configured target file sizes."
}, {
    "caption" : "hoodie.clustering.inline",
    "value" : "hoodie.clustering.inline = false",
    "meta" : "default: false",
    "version" : "0.7.0",
    "docHTML" : "Turn on inline clustering - clustering will be run after each write operation is complete"
}, {
    "caption" : "hoodie.clustering.inline.max.commits",
    "value" : "hoodie.clustering.inline.max.commits = 4",
    "meta" : "default: 4",
    "version" : "0.7.0",
    "docHTML" : "Config to control frequency of clustering planning"
}, {
    "caption" : "hoodie.clustering.async.max.commits",
    "value" : "hoodie.clustering.async.max.commits = 4",
    "meta" : "default: 4",
    "version" : "0.9.0",
    "docHTML" : "Config to control frequency of async clustering"
}, {
    "caption" : "hoodie.clustering.plan.strategy.daybased.skipfromlatest.partitions",
    "value" : "hoodie.clustering.plan.strategy.daybased.skipfromlatest.partitions = 0",
    "meta" : "default: 0",
    "version" : "0.9.0",
    "docHTML" : "Number of partitions to skip from latest when choosing partitions to create ClusteringPlan"
}, {
    "caption" : "hoodie.clustering.plan.partition.filter.mode",
    "value" : "hoodie.clustering.plan.partition.filter.mode = NONE",
    "meta" : "default: NONE",
    "version" : "0.11.0",
    "docHTML" : "Partition filter mode used in the creation of clustering plan. Available values are - NONE: do not filter table partition and thus the clustering plan will include all partitions that have clustering candidate.RECENT_DAYS: keep a continuous range of partitions, worked together with configs 'hoodie.clustering.plan.strategy.daybased.lookback.partitions' and 'hoodie.clustering.plan.strategy.daybased.skipfromlatest.partitions.SELECTED_PARTITIONS: keep partitions that are in the specified range ['hoodie.clustering.plan.strategy.cluster.begin.partition', 'hoodie.clustering.plan.strategy.cluster.end.partition'].DAY_ROLLING: clustering partitions on a rolling basis by the hour to avoid clustering all partitions each time, which strategy sorts the partitions asc and chooses the partition of which index is divided by 24 and the remainder is equal to the current hour."
}, {
    "caption" : "hoodie.clustering.plan.strategy.max.bytes.per.group",
    "value" : "hoodie.clustering.plan.strategy.max.bytes.per.group = 2147483648",
    "meta" : "default: 2147483648",
    "version" : "0.7.0",
    "docHTML" : "Each clustering operation can create multiple output file groups. Total amount of data processed by clustering operation is defined by below two properties (CLUSTERING_MAX_BYTES_PER_GROUP * CLUSTERING_MAX_NUM_GROUPS). Max amount of data to be included in one group"
}, {
    "caption" : "hoodie.clustering.plan.strategy.max.num.groups",
    "value" : "hoodie.clustering.plan.strategy.max.num.groups = 30",
    "meta" : "default: 30",
    "version" : "0.7.0",
    "docHTML" : "Maximum number of groups to create as part of ClusteringPlan. Increasing groups will increase parallelism"
}, {
    "caption" : "hoodie.clustering.plan.strategy.target.file.max.bytes",
    "value" : "hoodie.clustering.plan.strategy.target.file.max.bytes = 1073741824",
    "meta" : "default: 1073741824",
    "version" : "0.7.0",
    "docHTML" : "Each group can produce 'N' (CLUSTERING_MAX_GROUP_SIZE/CLUSTERING_TARGET_FILE_SIZE) output file groups"
}, {
    "caption" : "hoodie.clustering.plan.strategy.sort.columns",
    "value" : "hoodie.clustering.plan.strategy.sort.columns = ",
    "version" : "0.7.0",
    "docHTML" : "Columns to sort the data by when clustering"
}, {
    "caption" : "hoodie.clustering.updates.strategy",
    "value" : "hoodie.clustering.updates.strategy = org.apache.hudi.client.clustering.update.strategy.SparkRejectUpdateStrategy",
    "meta" : "default: org.apache.hudi.client.clustering.update.strategy.SparkRejectUpdateStrategy",
    "version" : "0.7.0",
    "docHTML" : "Determines how to handle updates, deletes to file groups that are under clustering. Default strategy just rejects the update"
}, {
    "caption" : "hoodie.clustering.schedule.inline",
    "value" : "hoodie.clustering.schedule.inline = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, clustering service will be attempted for inline scheduling after each write. Users have to ensure they have a separate job to run async clustering(execution) for the one scheduled by this writer. Users can choose to set both `hoodie.clustering.inline` and `hoodie.clustering.schedule.inline` to false and have both scheduling and execution triggered by any async process, on which case `hoodie.clustering.async.enabled` is expected to be set to true. But if `hoodie.clustering.inline` is set to false, and `hoodie.clustering.schedule.inline` is set to true, regular writers will schedule clustering inline, but users are expected to trigger async job for execution. If `hoodie.clustering.inline` is set to true, regular writers will do both scheduling and execution inline for clustering"
}, {
    "caption" : "hoodie.clustering.async.enabled",
    "value" : "hoodie.clustering.async.enabled = false",
    "meta" : "default: false",
    "version" : "0.7.0",
    "docHTML" : "Enable running of clustering service, asynchronously as inserts happen on the table."
}, {
    "caption" : "hoodie.clustering.preserve.commit.metadata",
    "value" : "hoodie.clustering.preserve.commit.metadata = true",
    "meta" : "default: true",
    "version" : "0.9.0",
    "docHTML" : "When rewriting data, preserves existing hoodie_commit_time"
}, {
    "caption" : "hoodie.layout.optimize.enable",
    "value" : "hoodie.layout.optimize.enable = false",
    "meta" : "default: false",
    "version" : "0.10.0",
    "docHTML" : "This setting has no effect. Please refer to clustering configuration, as well as LAYOUT_OPTIMIZE_STRATEGY config to enable advanced record layout optimization strategies"
}, {
    "caption" : "hoodie.layout.optimize.strategy",
    "value" : "hoodie.layout.optimize.strategy = linear",
    "meta" : "default: linear",
    "version" : "0.10.0",
    "docHTML" : "Determines ordering strategy used in records layout optimization. Currently supported strategies are \"linear\", \"z-order\" and \"hilbert\" values are supported."
}, {
    "caption" : "hoodie.layout.optimize.curve.build.method",
    "value" : "hoodie.layout.optimize.curve.build.method = direct",
    "meta" : "default: direct",
    "version" : "0.10.0",
    "docHTML" : "Controls how data is sampled to build the space-filling curves. Two methods: \"direct\", \"sample\". The direct method is faster than the sampling, however sample method would produce a better data layout."
}, {
    "caption" : "hoodie.layout.optimize.build.curve.sample.size",
    "value" : "hoodie.layout.optimize.build.curve.sample.size = 200000",
    "meta" : "default: 200000",
    "version" : "0.10.0",
    "docHTML" : "Determines target sample size used by the Boundary-based Interleaved Index method of building space-filling curve. Larger sample size entails better layout optimization outcomes, at the expense of higher memory footprint."
}, {
    "caption" : "hoodie.layout.optimize.data.skipping.enable",
    "value" : "hoodie.layout.optimize.data.skipping.enable = true",
    "meta" : "default: true",
    "version" : "0.10.0",
    "docHTML" : "Enable data skipping by collecting statistics once layout optimization is complete."
}, {
    "caption" : "hoodie.clustering.rollback.pending.replacecommit.on.conflict",
    "value" : "hoodie.clustering.rollback.pending.replacecommit.on.conflict = false",
    "meta" : "default: false",
    "version" : "0.10.0",
    "docHTML" : "If updates are allowed to file groups pending clustering, then set this config to rollback failed or pending clustering instants. Pending clustering will be rolled back ONLY IF there is conflict between incoming upsert and filegroup to be clustered. Please exercise caution while setting this config, especially when clustering is done very frequently. This could lead to race condition in rare scenarios, for example, when the clustering completes after instants are fetched but before rollback completed."
}, {
    "caption" : "hoodie.schema.on.read.enable",
    "value" : "hoodie.schema.on.read.enable = false",
    "meta" : "default: false",
    "docHTML" : "Enables support for Schema Evolution feature"
}, {
    "caption" : "as.of.instant",
    "value" : "as.of.instant = ",
    "docHTML" : "The query instant for time travel. Without specified this option, we query the latest snapshot."
}, {
    "caption" : "hoodie.datasource.write.reconcile.schema",
    "value" : "hoodie.datasource.write.reconcile.schema = false",
    "meta" : "default: false",
    "docHTML" : "This config controls how writer's schema will be selected based on the incoming batch's schema as well as existing table's one. When schema reconciliation is DISABLED, incoming batch's schema will be picked as a writer-schema (therefore updating table's schema). When schema reconciliation is ENABLED, writer-schema will be picked such that table's schema (after txn) is either kept the same or extended, meaning that we'll always prefer the schema that either adds new columns or stays the same. This enables us, to always extend the table's schema during evolution and never lose the data (when, for ex, existing column is being dropped in a new batch)"
}, {
    "caption" : "hoodie.common.spillable.diskmap.type",
    "value" : "hoodie.common.spillable.diskmap.type = BITCASK",
    "meta" : "default: BITCASK",
    "docHTML" : "When handling input data that cannot be held in memory, to merge with a file on storage, a spillable diskmap is employed.  By default, we use a persistent hashmap based loosely on bitcask, that offers O(1) inserts, lookups. Change this to `ROCKS_DB` to prefer using rocksDB, for handling the spill."
}, {
    "caption" : "hoodie.common.diskmap.compression.enabled",
    "value" : "hoodie.common.diskmap.compression.enabled = true",
    "meta" : "default: true",
    "docHTML" : "Turn on compression for BITCASK disk map used by the External Spillable Map"
}, {
    "caption" : "hoodie.compact.inline",
    "value" : "hoodie.compact.inline = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, compaction service is triggered after each write. While being  simpler operationally, this adds extra latency on the write path."
}, {
    "caption" : "hoodie.compact.schedule.inline",
    "value" : "hoodie.compact.schedule.inline = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, compaction service will be attempted for inline scheduling after each write. Users have to ensure they have a separate job to run async compaction(execution) for the one scheduled by this writer. Users can choose to set both `hoodie.compact.inline` and `hoodie.compact.schedule.inline` to false and have both scheduling and execution triggered by any async process. But if `hoodie.compact.inline` is set to false, and `hoodie.compact.schedule.inline` is set to true, regular writers will schedule compaction inline, but users are expected to trigger async job for execution. If `hoodie.compact.inline` is set to true, regular writers will do both scheduling and execution inline for compaction"
}, {
    "caption" : "hoodie.log.compaction.inline",
    "value" : "hoodie.log.compaction.inline = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "When set to true, logcompaction service is triggered after each write. While being  simpler operationally, this adds extra latency on the write path."
}, {
    "caption" : "hoodie.compact.inline.max.delta.commits",
    "value" : "hoodie.compact.inline.max.delta.commits = 5",
    "meta" : "default: 5",
    "docHTML" : "Number of delta commits after the last compaction, before scheduling of a new compaction is attempted. This config takes effect only for the compaction triggering strategy based on the number of commits, i.e., NUM_COMMITS, NUM_COMMITS_AFTER_LAST_REQUEST, NUM_AND_TIME, and NUM_OR_TIME."
}, {
    "caption" : "hoodie.compact.inline.max.delta.seconds",
    "value" : "hoodie.compact.inline.max.delta.seconds = 3600",
    "meta" : "default: 3600",
    "docHTML" : "Number of elapsed seconds after the last compaction, before scheduling a new one. This config takes effect only for the compaction triggering strategy based on the elapsed time, i.e., TIME_ELAPSED, NUM_AND_TIME, and NUM_OR_TIME."
}, {
    "caption" : "hoodie.compact.inline.trigger.strategy",
    "value" : "hoodie.compact.inline.trigger.strategy = NUM_COMMITS",
    "meta" : "default: NUM_COMMITS",
    "docHTML" : "Controls how compaction scheduling is triggered, by time or num delta commits or combination of both. Valid options: NUM_COMMITS,NUM_COMMITS_AFTER_LAST_REQUEST,TIME_ELAPSED,NUM_AND_TIME,NUM_OR_TIME"
}, {
    "caption" : "hoodie.parquet.small.file.limit",
    "value" : "hoodie.parquet.small.file.limit = 104857600",
    "meta" : "default: 104857600",
    "docHTML" : "During upsert operation, we opportunistically expand existing small files on storage, instead of writing new files, to keep number of files to an optimum. This config sets the file size limit below which a file on storage  becomes a candidate to be selected as such a `small file`. By default, treat any file <= 100MB as a small file. Also note that if this set <= 0, will not try to get small files and directly write new files"
}, {
    "caption" : "hoodie.record.size.estimation.threshold",
    "value" : "hoodie.record.size.estimation.threshold = 1.0",
    "meta" : "default: 1.0",
    "docHTML" : "We use the previous commits' metadata to calculate the estimated record size and use it  to bin pack records into partitions. If the previous commit is too small to make an accurate estimation,  Hudi will search commits in the reverse order, until we find a commit that has totalBytesWritten  larger than (PARQUET_SMALL_FILE_LIMIT_BYTES * this_threshold)"
}, {
    "caption" : "hoodie.compaction.target.io",
    "value" : "hoodie.compaction.target.io = 512000",
    "meta" : "default: 512000",
    "docHTML" : "Amount of MBs to spend during compaction run for the LogFileSizeBasedCompactionStrategy. This value helps bound ingestion latency while compaction is run inline mode."
}, {
    "caption" : "hoodie.compaction.logfile.size.threshold",
    "value" : "hoodie.compaction.logfile.size.threshold = 0",
    "meta" : "default: 0",
    "docHTML" : "Only if the log file size is greater than the threshold in bytes, the file group will be compacted."
}, {
    "caption" : "hoodie.compaction.logfile.num.threshold",
    "value" : "hoodie.compaction.logfile.num.threshold = 0",
    "meta" : "default: 0",
    "version" : "0.13.0",
    "docHTML" : "Only if the log file num is greater than the threshold, the file group will be compacted."
}, {
    "caption" : "hoodie.compaction.strategy",
    "value" : "hoodie.compaction.strategy = org.apache.hudi.table.action.compact.strategy.LogFileSizeBasedCompactionStrategy",
    "meta" : "default: org.apache.hudi.table.action.compact.strategy.LogFileSizeBasedCompactionStrategy",
    "docHTML" : "Compaction strategy decides which file groups are picked up for compaction during each compaction run. By default. Hudi picks the log file with most accumulated unmerged data"
}, {
    "caption" : "hoodie.compaction.lazy.block.read",
    "value" : "hoodie.compaction.lazy.block.read = true",
    "meta" : "default: true",
    "docHTML" : "When merging the delta log files, this config helps to choose whether the log blocks should be read lazily or not. Choose true to use lazy block reading (low memory usage, but incurs seeks to each block header) or false for immediate block read (higher memory usage)"
}, {
    "caption" : "hoodie.compaction.reverse.log.read",
    "value" : "hoodie.compaction.reverse.log.read = false",
    "meta" : "default: false",
    "docHTML" : "HoodieLogFormatReader reads a logfile in the forward direction starting from pos=0 to pos=file_length. If this config is set to true, the reader reads the logfile in reverse direction, from pos=file_length to pos=0"
}, {
    "caption" : "hoodie.compaction.daybased.target.partitions",
    "value" : "hoodie.compaction.daybased.target.partitions = 10",
    "meta" : "default: 10",
    "docHTML" : "Used by org.apache.hudi.io.compact.strategy.DayBasedCompactionStrategy to denote the number of latest partitions to compact during a compaction run."
}, {
    "caption" : "hoodie.compaction.preserve.commit.metadata",
    "value" : "hoodie.compaction.preserve.commit.metadata = true",
    "meta" : "default: true",
    "version" : "0.11.0",
    "docHTML" : "When rewriting data, preserves existing hoodie_commit_time"
}, {
    "caption" : "hoodie.copyonwrite.insert.split.size",
    "value" : "hoodie.copyonwrite.insert.split.size = 500000",
    "meta" : "default: 500000",
    "docHTML" : "Number of inserts assigned for each partition/bucket for writing. We based the default on writing out 100MB files, with at least 1kb records (100K records per file), and   over provision to 500K. As long as auto-tuning of splits is turned on, this only affects the first   write, where there is no history to learn record sizes from."
}, {
    "caption" : "hoodie.copyonwrite.insert.auto.split",
    "value" : "hoodie.copyonwrite.insert.auto.split = true",
    "meta" : "default: true",
    "docHTML" : "Config to control whether we control insert split sizes automatically based on average record sizes. It's recommended to keep this turned on, since hand tuning is otherwise extremely cumbersome."
}, {
    "caption" : "hoodie.copyonwrite.record.size.estimate",
    "value" : "hoodie.copyonwrite.record.size.estimate = 1024",
    "meta" : "default: 1024",
    "docHTML" : "The average record size. If not explicitly specified, hudi will compute the record size estimate compute dynamically based on commit metadata.  This is critical in computing the insert parallelism and bin-packing inserts into small files."
}, {
    "caption" : "hoodie.log.compaction.blocks.threshold",
    "value" : "hoodie.log.compaction.blocks.threshold = 5",
    "meta" : "default: 5",
    "version" : "0.13.0",
    "docHTML" : "Log compaction can be scheduled if the no. of log blocks crosses this threshold value. This is effective only when log compaction is enabled via hoodie.log.compaction.inline"
}, {
    "caption" : "hoodie.optimized.log.blocks.scan.enable",
    "value" : "hoodie.optimized.log.blocks.scan.enable = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "New optimized scan for log blocks that handles all multi-writer use-cases while appending to log files. It also differentiates original blocks written by ingestion writers and compacted blocks written log compaction."
}, {
    "caption" : "hoodie.index.hbase.zkquorum",
    "value" : "hoodie.index.hbase.zkquorum = ",
    "docHTML" : "Only applies if index type is HBASE. HBase ZK Quorum url to connect to"
}, {
    "caption" : "hoodie.index.hbase.zkport",
    "value" : "hoodie.index.hbase.zkport = ",
    "docHTML" : "Only applies if index type is HBASE. HBase ZK Quorum port to connect to"
}, {
    "caption" : "hoodie.index.hbase.table",
    "value" : "hoodie.index.hbase.table = ",
    "docHTML" : "Only applies if index type is HBASE. HBase Table name to use as the index. Hudi stores the row_key and [partition_path, fileID, commitTime] mapping in the table"
}, {
    "caption" : "hoodie.index.hbase.get.batch.size",
    "value" : "hoodie.index.hbase.get.batch.size = 100",
    "meta" : "default: 100",
    "docHTML" : "Controls the batch size for performing gets against HBase. Batching improves throughput, by saving round trips."
}, {
    "caption" : "hoodie.index.hbase.zknode.path",
    "value" : "hoodie.index.hbase.zknode.path = ",
    "docHTML" : "Only applies if index type is HBASE. This is the root znode that will contain all the znodes created/used by HBase"
}, {
    "caption" : "hoodie.index.hbase.put.batch.size",
    "value" : "hoodie.index.hbase.put.batch.size = 100",
    "meta" : "default: 100",
    "docHTML" : "Controls the batch size for performing puts against HBase. Batching improves throughput, by saving round trips."
}, {
    "caption" : "hoodie.index.hbase.qps.allocator.class",
    "value" : "hoodie.index.hbase.qps.allocator.class = org.apache.hudi.index.hbase.DefaultHBaseQPSResourceAllocator",
    "meta" : "default: org.apache.hudi.index.hbase.DefaultHBaseQPSResourceAllocator",
    "docHTML" : "Property to set which implementation of HBase QPS resource allocator to be used, whichcontrols the batching rate dynamically."
}, {
    "caption" : "hoodie.index.hbase.put.batch.size.autocompute",
    "value" : "hoodie.index.hbase.put.batch.size.autocompute = false",
    "meta" : "default: false",
    "docHTML" : "Property to set to enable auto computation of put batch size"
}, {
    "caption" : "hoodie.index.hbase.qps.fraction",
    "value" : "hoodie.index.hbase.qps.fraction = 0.5",
    "meta" : "default: 0.5",
    "docHTML" : "Property to set the fraction of the global share of QPS that should be allocated to this job. Let's say there are 3 jobs which have input size in terms of number of rows required for HbaseIndexing as x, 2x, 3x respectively. Then this fraction for the jobs would be (0.17) 1/6, 0.33 (2/6) and 0.5 (3/6) respectively. Default is 50%, which means a total of 2 jobs can run using HbaseIndex without overwhelming Region Servers."
}, {
    "caption" : "hoodie.index.hbase.max.qps.per.region.server",
    "value" : "hoodie.index.hbase.max.qps.per.region.server = 1000",
    "meta" : "default: 1000",
    "docHTML" : "Property to set maximum QPS allowed per Region Server. This should be same across various jobs. This is intended to\n limit the aggregate QPS generated across various jobs to an Hbase Region Server. It is recommended to set this\n value based on global indexing throughput needs and most importantly, how much the HBase installation in use is\n able to tolerate without Region Servers going down."
}, {
    "caption" : "hoodie.index.hbase.dynamic_qps",
    "value" : "hoodie.index.hbase.dynamic_qps = false",
    "meta" : "default: false",
    "docHTML" : "Property to decide if HBASE_QPS_FRACTION_PROP is dynamically calculated based on write volume."
}, {
    "caption" : "hoodie.index.hbase.min.qps.fraction",
    "value" : "hoodie.index.hbase.min.qps.fraction = ",
    "docHTML" : "Minimum for HBASE_QPS_FRACTION_PROP to stabilize skewed write workloads"
}, {
    "caption" : "hoodie.index.hbase.max.qps.fraction",
    "value" : "hoodie.index.hbase.max.qps.fraction = ",
    "docHTML" : "Maximum for HBASE_QPS_FRACTION_PROP to stabilize skewed write workloads"
}, {
    "caption" : "hoodie.index.hbase.desired_puts_time_in_secs",
    "value" : "hoodie.index.hbase.desired_puts_time_in_secs = 600",
    "meta" : "default: 600",
    "docHTML" : ""
}, {
    "caption" : "hoodie.index.hbase.sleep.ms.for.put.batch",
    "value" : "hoodie.index.hbase.sleep.ms.for.put.batch = ",
    "docHTML" : ""
}, {
    "caption" : "hoodie.index.hbase.sleep.ms.for.get.batch",
    "value" : "hoodie.index.hbase.sleep.ms.for.get.batch = ",
    "docHTML" : ""
}, {
    "caption" : "hoodie.index.hbase.zk.session_timeout_ms",
    "value" : "hoodie.index.hbase.zk.session_timeout_ms = 60000",
    "meta" : "default: 60000",
    "docHTML" : "Session timeout value to use for Zookeeper failure detection, for the HBase client.Lower this value, if you want to fail faster."
}, {
    "caption" : "hoodie.index.hbase.zk.connection_timeout_ms",
    "value" : "hoodie.index.hbase.zk.connection_timeout_ms = 15000",
    "meta" : "default: 15000",
    "docHTML" : "Timeout to use for establishing connection with zookeeper, from HBase client."
}, {
    "caption" : "hoodie.index.hbase.zkpath.qps_root",
    "value" : "hoodie.index.hbase.zkpath.qps_root = /QPS_ROOT",
    "meta" : "default: /QPS_ROOT",
    "docHTML" : "chroot in zookeeper, to use for all qps allocation co-ordination."
}, {
    "caption" : "hoodie.hbase.index.update.partition.path",
    "value" : "hoodie.hbase.index.update.partition.path = false",
    "meta" : "default: false",
    "docHTML" : "Only applies if index type is HBASE. When an already existing record is upserted to a new partition compared to whats in storage, this config when set, will delete old record in old partition and will insert it as new record in new partition."
}, {
    "caption" : "hoodie.index.hbase.rollback.sync",
    "value" : "hoodie.index.hbase.rollback.sync = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, the rollback method will delete the last failed task index. The default value is false. Because deleting the index will add extra load on the Hbase cluster for each rollback"
}, {
    "caption" : "hoodie.index.hbase.security.authentication",
    "value" : "hoodie.index.hbase.security.authentication = simple",
    "meta" : "default: simple",
    "docHTML" : "Property to decide if the hbase cluster secure authentication is enabled or not. Possible values are 'simple' (no authentication), and 'kerberos'."
}, {
    "caption" : "hoodie.index.hbase.kerberos.user.keytab",
    "value" : "hoodie.index.hbase.kerberos.user.keytab = ",
    "docHTML" : "File name of the kerberos keytab file for connecting to the hbase cluster."
}, {
    "caption" : "hoodie.index.hbase.kerberos.user.principal",
    "value" : "hoodie.index.hbase.kerberos.user.principal = ",
    "docHTML" : "The kerberos principal name for connecting to the hbase cluster."
}, {
    "caption" : "hoodie.index.hbase.regionserver.kerberos.principal",
    "value" : "hoodie.index.hbase.regionserver.kerberos.principal = ",
    "docHTML" : "The value of hbase.regionserver.kerberos.principal in hbase cluster."
}, {
    "caption" : "hoodie.index.hbase.master.kerberos.principal",
    "value" : "hoodie.index.hbase.master.kerberos.principal = ",
    "docHTML" : "The value of hbase.master.kerberos.principal in hbase cluster."
}, {
    "caption" : "hoodie.index.hbase.bucket.number",
    "value" : "hoodie.index.hbase.bucket.number = 8",
    "meta" : "default: 8",
    "docHTML" : "Only applicable when using RebalancedSparkHoodieHBaseIndex, same as hbase regions count can get the best performance"
}, {
    "caption" : "hoodie.index.type",
    "value" : "hoodie.index.type = ",
    "docHTML" : "Type of index to use. Default is SIMPLE on Spark engine, and INMEMORY on Flink and Java engines. Possible options are [BLOOM | GLOBAL_BLOOM |SIMPLE | GLOBAL_SIMPLE | INMEMORY | HBASE | BUCKET]. Bloom filters removes the dependency on a external system and is stored in the footer of the Parquet Data Files"
}, {
    "caption" : "hoodie.index.class",
    "value" : "hoodie.index.class = ",
    "meta" : "default: ",
    "docHTML" : "Full path of user-defined index class and must be a subclass of HoodieIndex class. It will take precedence over the hoodie.index.type configuration if specified"
}, {
    "caption" : "hoodie.index.bloom.num_entries",
    "value" : "hoodie.index.bloom.num_entries = 60000",
    "meta" : "default: 60000",
    "docHTML" : "Only applies if index type is BLOOM. This is the number of entries to be stored in the bloom filter. The rationale for the default: Assume the maxParquetFileSize is 128MB and averageRecordSize is 1kb and hence we approx a total of 130K records in a file. The default (60000) is roughly half of this approximation. Warning: Setting this very low, will generate a lot of false positives and index lookup will have to scan a lot more files than it has to and setting this to a very high number will increase the size every base file linearly (roughly 4KB for every 50000 entries). This config is also used with DYNAMIC bloom filter which determines the initial size for the bloom."
}, {
    "caption" : "hoodie.index.bloom.fpp",
    "value" : "hoodie.index.bloom.fpp = 0.000000001",
    "meta" : "default: 0.000000001",
    "docHTML" : "Only applies if index type is BLOOM. Error rate allowed given the number of entries. This is used to calculate how many bits should be assigned for the bloom filter and the number of hash functions. This is usually set very low (default: 0.000000001), we like to tradeoff disk space for lower false positives. If the number of entries added to bloom filter exceeds the configured value (hoodie.index.bloom.num_entries), then this fpp may not be honored."
}, {
    "caption" : "hoodie.bloom.index.parallelism",
    "value" : "hoodie.bloom.index.parallelism = 0",
    "meta" : "default: 0",
    "docHTML" : "Only applies if index type is BLOOM. This is the amount of parallelism for index lookup, which involves a shuffle. By default, this is auto computed based on input workload characteristics."
}, {
    "caption" : "hoodie.bloom.index.prune.by.ranges",
    "value" : "hoodie.bloom.index.prune.by.ranges = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is BLOOM. When true, range information from files to leveraged speed up index lookups. Particularly helpful, if the key has a monotonously increasing prefix, such as timestamp. If the record key is completely random, it is better to turn this off, since range pruning will only  add extra overhead to the index lookup."
}, {
    "caption" : "hoodie.bloom.index.use.caching",
    "value" : "hoodie.bloom.index.use.caching = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is BLOOM.When true, the input RDD will cached to speed up index lookup by reducing IO for computing parallelism or affected partitions"
}, {
    "caption" : "hoodie.bloom.index.use.metadata",
    "value" : "hoodie.bloom.index.use.metadata = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Only applies if index type is BLOOM.When true, the index lookup uses bloom filters and column stats from metadata table when available to speed up the process."
}, {
    "caption" : "hoodie.bloom.index.use.treebased.filter",
    "value" : "hoodie.bloom.index.use.treebased.filter = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is BLOOM. When true, interval tree based file pruning optimization is enabled. This mode speeds-up file-pruning based on key ranges when compared with the brute-force mode"
}, {
    "caption" : "hoodie.bloom.index.bucketized.checking",
    "value" : "hoodie.bloom.index.bucketized.checking = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is BLOOM. When true, bucketized bloom filtering is enabled. This reduces skew seen in sort based bloom index lookup"
}, {
    "caption" : "hoodie.bloom.index.filter.type",
    "value" : "hoodie.bloom.index.filter.type = DYNAMIC_V0",
    "meta" : "default: DYNAMIC_V0",
    "docHTML" : "Filter type used. Default is BloomFilterTypeCode.DYNAMIC_V0. Available values are [BloomFilterTypeCode.SIMPLE , BloomFilterTypeCode.DYNAMIC_V0]. Dynamic bloom filters auto size themselves based on number of keys."
}, {
    "caption" : "hoodie.bloom.index.filter.dynamic.max.entries",
    "value" : "hoodie.bloom.index.filter.dynamic.max.entries = 100000",
    "meta" : "default: 100000",
    "docHTML" : "The threshold for the maximum number of keys to record in a dynamic Bloom filter row. Only applies if filter type is BloomFilterTypeCode.DYNAMIC_V0."
}, {
    "caption" : "hoodie.simple.index.use.caching",
    "value" : "hoodie.simple.index.use.caching = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is SIMPLE. When true, the incoming writes will cached to speed up index lookup by reducing IO for computing parallelism or affected partitions"
}, {
    "caption" : "hoodie.simple.index.parallelism",
    "value" : "hoodie.simple.index.parallelism = 100",
    "meta" : "default: 100",
    "docHTML" : "Only applies if index type is SIMPLE. This is the amount of parallelism for index lookup, which involves a Spark Shuffle"
}, {
    "caption" : "hoodie.global.simple.index.parallelism",
    "value" : "hoodie.global.simple.index.parallelism = 100",
    "meta" : "default: 100",
    "docHTML" : "Only applies if index type is GLOBAL_SIMPLE. This is the amount of parallelism for index lookup, which involves a Spark Shuffle"
}, {
    "caption" : "hoodie.bloom.index.keys.per.bucket",
    "value" : "hoodie.bloom.index.keys.per.bucket = 10000000",
    "meta" : "default: 10000000",
    "docHTML" : "Only applies if bloomIndexBucketizedChecking is enabled and index type is bloom. This configuration controls the “bucket” size which tracks the number of record-key checks made against a single file and is the unit of work allocated to each partition performing bloom filter lookup. A higher value would amortize the fixed cost of reading a bloom filter to memory."
}, {
    "caption" : "hoodie.bloom.index.input.storage.level",
    "value" : "hoodie.bloom.index.input.storage.level = MEMORY_AND_DISK_SER",
    "meta" : "default: MEMORY_AND_DISK_SER",
    "docHTML" : "Only applies when #bloomIndexUseCaching is set. Determine what level of persistence is used to cache input RDDs. Refer to org.apache.spark.storage.StorageLevel for different values"
}, {
    "caption" : "hoodie.simple.index.input.storage.level",
    "value" : "hoodie.simple.index.input.storage.level = MEMORY_AND_DISK_SER",
    "meta" : "default: MEMORY_AND_DISK_SER",
    "docHTML" : "Only applies when #simpleIndexUseCaching is set. Determine what level of persistence is used to cache input RDDs. Refer to org.apache.spark.storage.StorageLevel for different values"
}, {
    "caption" : "hoodie.bloom.index.update.partition.path",
    "value" : "hoodie.bloom.index.update.partition.path = true",
    "meta" : "default: true",
    "docHTML" : "Only applies if index type is GLOBAL_BLOOM. When set to true, an update including the partition path of a record that already exists will result in inserting the incoming record into the new partition and deleting the original record in the old partition. When set to false, the original record will only be updated in the old partition"
}, {
    "caption" : "hoodie.simple.index.update.partition.path",
    "value" : "hoodie.simple.index.update.partition.path = true",
    "meta" : "default: true",
    "docHTML" : "Similar to Key: 'hoodie.bloom.index.update.partition.path' , default: true description: Only applies if index type is GLOBAL_BLOOM. When set to true, an update including the partition path of a record that already exists will result in inserting the incoming record into the new partition and deleting the original record in the old partition. When set to false, the original record will only be updated in the old partition since version: version is not defined deprecated after: version is not defined), but for simple index."
}, {
    "caption" : "hoodie.index.bucket.engine",
    "value" : "hoodie.index.bucket.engine = SIMPLE",
    "meta" : "default: SIMPLE",
    "version" : "0.11.0",
    "docHTML" : "Type of bucket index engine to use. Default is SIMPLE bucket index, with fixed number of bucket.Possible options are [SIMPLE | CONSISTENT_HASHING].Consistent hashing supports dynamic resizing of the number of bucket, solving potential data skew and file size issues of the SIMPLE hashing engine. Consistent hashing only works with MOR tables, only use simple hashing on COW tables."
}, {
    "caption" : "hoodie.bucket.index.num.buckets",
    "value" : "hoodie.bucket.index.num.buckets = 256",
    "meta" : "default: 256",
    "docHTML" : "Only applies if index type is BUCKET. Determine the number of buckets in the hudi table, and each partition is divided to N buckets."
}, {
    "caption" : "hoodie.bucket.index.max.num.buckets",
    "value" : "hoodie.bucket.index.max.num.buckets = ",
    "version" : "0.13.0",
    "docHTML" : "Only applies if bucket index engine is consistent hashing. Determine the upper bound of the number of buckets in the hudi table. Bucket resizing cannot be done higher than this max limit."
}, {
    "caption" : "hoodie.bucket.index.min.num.buckets",
    "value" : "hoodie.bucket.index.min.num.buckets = ",
    "version" : "0.13.0",
    "docHTML" : "Only applies if bucket index engine is consistent hashing. Determine the lower bound of the number of buckets in the hudi table. Bucket resizing cannot be done lower than this min limit."
}, {
    "caption" : "hoodie.bucket.index.hash.field",
    "value" : "hoodie.bucket.index.hash.field = ",
    "docHTML" : "Index key. It is used to index the record and find its file group. If not set, use record key field as default"
}, {
    "caption" : "hoodie.bucket.index.split.threshold",
    "value" : "hoodie.bucket.index.split.threshold = 2.0",
    "meta" : "default: 2.0",
    "version" : "0.13.0",
    "docHTML" : "Control if the bucket should be split when using consistent hashing bucket index.Specifically, if a file slice size reaches `hoodie.xxxx.max.file.size` * threshold, then split will be carried out."
}, {
    "caption" : "hoodie.bucket.index.merge.threshold",
    "value" : "hoodie.bucket.index.merge.threshold = 0.2",
    "meta" : "default: 0.2",
    "version" : "0.13.0",
    "docHTML" : "Control if buckets should be merged when using consistent hashing bucket indexSpecifically, if a file slice size is smaller than `hoodie.xxxx.max.file.size` * threshold, then it will be consideredas a merge candidate."
}, {
    "caption" : "hoodie.bulkinsert.schema.ddl",
    "value" : "hoodie.bulkinsert.schema.ddl = ",
    "docHTML" : "Schema set for row writer/bulk insert."
}, {
    "caption" : "hoodie.storage.layout.type",
    "value" : "hoodie.storage.layout.type = DEFAULT",
    "meta" : "default: DEFAULT",
    "docHTML" : "Type of storage layout. Possible options are [DEFAULT | BUCKET]"
}, {
    "caption" : "hoodie.storage.layout.partitioner.class",
    "value" : "hoodie.storage.layout.partitioner.class = ",
    "docHTML" : "Partitioner class, it is used to distribute data in a specific way."
}, {
    "caption" : "hoodie.write.lock.wait_time_ms_between_retry",
    "value" : "hoodie.write.lock.wait_time_ms_between_retry = 1000",
    "meta" : "default: 1000",
    "version" : "0.8.0",
    "docHTML" : "Initial amount of time to wait between retries to acquire locks,  subsequent retries will exponentially backoff."
}, {
    "caption" : "hoodie.write.lock.max_wait_time_ms_between_retry",
    "value" : "hoodie.write.lock.max_wait_time_ms_between_retry = 5000",
    "meta" : "default: 5000",
    "version" : "0.8.0",
    "docHTML" : "Maximum amount of time to wait between retries by lock provider client. This bounds the maximum delay from the exponential backoff. Currently used by ZK based lock provider only."
}, {
    "caption" : "hoodie.write.lock.client.wait_time_ms_between_retry",
    "value" : "hoodie.write.lock.client.wait_time_ms_between_retry = 5000",
    "meta" : "default: 5000",
    "version" : "0.8.0",
    "docHTML" : "Amount of time to wait between retries on the lock provider by the lock manager"
}, {
    "caption" : "hoodie.write.lock.num_retries",
    "value" : "hoodie.write.lock.num_retries = 15",
    "meta" : "default: 15",
    "version" : "0.8.0",
    "docHTML" : "Maximum number of times to retry lock acquire, at each lock provider"
}, {
    "caption" : "hoodie.write.lock.client.num_retries",
    "value" : "hoodie.write.lock.client.num_retries = 50",
    "meta" : "default: 50",
    "version" : "0.8.0",
    "docHTML" : "Maximum number of times to retry to acquire lock additionally from the lock manager."
}, {
    "caption" : "hoodie.write.lock.wait_time_ms",
    "value" : "hoodie.write.lock.wait_time_ms = 60000",
    "meta" : "default: 60000",
    "version" : "0.8.0",
    "docHTML" : "Timeout in ms, to wait on an individual lock acquire() call, at the lock provider."
}, {
    "caption" : "hoodie.write.lock.filesystem.path",
    "value" : "hoodie.write.lock.filesystem.path = ",
    "version" : "0.8.0",
    "docHTML" : "For DFS based lock providers, path to store the locks under. use Table's meta path as default"
}, {
    "caption" : "hoodie.write.lock.filesystem.expire",
    "value" : "hoodie.write.lock.filesystem.expire = 0",
    "meta" : "default: 0",
    "version" : "0.12.0",
    "docHTML" : "For DFS based lock providers, expire time in minutes, must be a nonnegative number, default means no expire"
}, {
    "caption" : "hoodie.write.lock.hivemetastore.database",
    "value" : "hoodie.write.lock.hivemetastore.database = ",
    "version" : "0.8.0",
    "docHTML" : "For Hive based lock provider, the Hive database to acquire lock against"
}, {
    "caption" : "hoodie.write.lock.hivemetastore.table",
    "value" : "hoodie.write.lock.hivemetastore.table = ",
    "version" : "0.8.0",
    "docHTML" : "For Hive based lock provider, the Hive table to acquire lock against"
}, {
    "caption" : "hoodie.write.lock.hivemetastore.uris",
    "value" : "hoodie.write.lock.hivemetastore.uris = ",
    "version" : "0.8.0",
    "docHTML" : "For Hive based lock provider, the Hive metastore URI to acquire locks against."
}, {
    "caption" : "hoodie.write.lock.zookeeper.base_path",
    "value" : "hoodie.write.lock.zookeeper.base_path = ",
    "version" : "0.8.0",
    "docHTML" : "The base path on Zookeeper under which to create lock related ZNodes. This should be same for all concurrent writers to the same table"
}, {
    "caption" : "hoodie.write.lock.zookeeper.session_timeout_ms",
    "value" : "hoodie.write.lock.zookeeper.session_timeout_ms = 60000",
    "meta" : "default: 60000",
    "version" : "0.8.0",
    "docHTML" : "Timeout in ms, to wait after losing connection to ZooKeeper, before the session is expired"
}, {
    "caption" : "hoodie.write.lock.zookeeper.connection_timeout_ms",
    "value" : "hoodie.write.lock.zookeeper.connection_timeout_ms = 15000",
    "meta" : "default: 15000",
    "version" : "0.8.0",
    "docHTML" : "Timeout in ms, to wait for establishing connection with Zookeeper."
}, {
    "caption" : "hoodie.write.lock.zookeeper.url",
    "value" : "hoodie.write.lock.zookeeper.url = ",
    "version" : "0.8.0",
    "docHTML" : "Zookeeper URL to connect to."
}, {
    "caption" : "hoodie.write.lock.zookeeper.port",
    "value" : "hoodie.write.lock.zookeeper.port = ",
    "version" : "0.8.0",
    "docHTML" : "Zookeeper port to connect to."
}, {
    "caption" : "hoodie.write.lock.zookeeper.lock_key",
    "value" : "hoodie.write.lock.zookeeper.lock_key = ",
    "version" : "0.8.0",
    "docHTML" : "Key name under base_path at which to create a ZNode and acquire lock. Final path on zk will look like base_path/lock_key. If this parameter is not set, we would set it as the table name"
}, {
    "caption" : "hoodie.write.lock.provider",
    "value" : "hoodie.write.lock.provider = org.apache.hudi.client.transaction.lock.ZookeeperBasedLockProvider",
    "meta" : "default: org.apache.hudi.client.transaction.lock.ZookeeperBasedLockProvider",
    "version" : "0.8.0",
    "docHTML" : "Lock provider class name, user can provide their own implementation of LockProvider which should be subclass of org.apache.hudi.common.lock.LockProvider"
}, {
    "caption" : "hoodie.write.lock.conflict.resolution.strategy",
    "value" : "hoodie.write.lock.conflict.resolution.strategy = org.apache.hudi.client.transaction.SimpleConcurrentFileWritesConflictResolutionStrategy",
    "meta" : "default: org.apache.hudi.client.transaction.SimpleConcurrentFileWritesConflictResolutionStrategy",
    "version" : "0.8.0",
    "docHTML" : "Lock provider class name, this should be subclass of org.apache.hudi.client.transaction.ConflictResolutionStrategy"
}, {
    "caption" : "hoodie.memory.merge.fraction",
    "value" : "hoodie.memory.merge.fraction = 0.6",
    "meta" : "default: 0.6",
    "docHTML" : "This fraction is multiplied with the user memory fraction (1 - spark.memory.fraction) to get a final fraction of heap space to use during merge"
}, {
    "caption" : "hoodie.memory.compaction.fraction",
    "value" : "hoodie.memory.compaction.fraction = 0.6",
    "meta" : "default: 0.6",
    "docHTML" : "HoodieCompactedLogScanner reads logblocks, converts records to HoodieRecords and then merges these log blocks and records. At any point, the number of entries in a log block can be less than or equal to the number of entries in the corresponding parquet file. This can lead to OOM in the Scanner. Hence, a spillable map helps alleviate the memory pressure. Use this config to set the max allowable inMemory footprint of the spillable map"
}, {
    "caption" : "hoodie.memory.merge.max.size",
    "value" : "hoodie.memory.merge.max.size = 1073741824",
    "meta" : "default: 1073741824",
    "docHTML" : "Maximum amount of memory used  in bytes for merge operations, before spilling to local storage."
}, {
    "caption" : "hoodie.memory.compaction.max.size",
    "value" : "hoodie.memory.compaction.max.size = ",
    "docHTML" : "Maximum amount of memory used  in bytes for compaction operations in bytes , before spilling to local storage."
}, {
    "caption" : "hoodie.memory.dfs.buffer.max.size",
    "value" : "hoodie.memory.dfs.buffer.max.size = 16777216",
    "meta" : "default: 16777216",
    "docHTML" : "Property to control the max memory in bytes for dfs input stream buffer size"
}, {
    "caption" : "hoodie.memory.spillable.map.path",
    "value" : "hoodie.memory.spillable.map.path = /tmp/",
    "meta" : "default: /tmp/",
    "docHTML" : "Default file path for spillable map"
}, {
    "caption" : "hoodie.memory.writestatus.failure.fraction",
    "value" : "hoodie.memory.writestatus.failure.fraction = 0.1",
    "meta" : "default: 0.1",
    "docHTML" : "Property to control how what fraction of the failed record, exceptions we report back to driver. Default is 10%. If set to 100%, with lot of failures, this can cause memory pressure, cause OOMs and mask actual data errors."
}, {
    "caption" : "hoodie.metadata.enable",
    "value" : "hoodie.metadata.enable = true",
    "meta" : "default: true",
    "version" : "0.7.0",
    "docHTML" : "Enable the internal metadata table which serves table metadata like level file listings"
}, {
    "caption" : "hoodie.metadata.metrics.enable",
    "value" : "hoodie.metadata.metrics.enable = false",
    "meta" : "default: false",
    "version" : "0.7.0",
    "docHTML" : "Enable publishing of metrics around metadata table."
}, {
    "caption" : "hoodie.metadata.insert.parallelism",
    "value" : "hoodie.metadata.insert.parallelism = 1",
    "meta" : "default: 1",
    "version" : "0.7.0",
    "docHTML" : "Parallelism to use when inserting to the metadata table"
}, {
    "caption" : "hoodie.metadata.clean.async",
    "value" : "hoodie.metadata.clean.async = false",
    "meta" : "default: false",
    "version" : "0.7.0",
    "docHTML" : "Enable asynchronous cleaning for metadata table. This is an internal config and setting this will not overwrite the value actually used."
}, {
    "caption" : "hoodie.metadata.index.async",
    "value" : "hoodie.metadata.index.async = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Enable asynchronous indexing of metadata table."
}, {
    "caption" : "hoodie.metadata.compact.max.delta.commits",
    "value" : "hoodie.metadata.compact.max.delta.commits = 10",
    "meta" : "default: 10",
    "version" : "0.7.0",
    "docHTML" : "Controls how often the metadata table is compacted."
}, {
    "caption" : "hoodie.metadata.keep.min.commits",
    "value" : "hoodie.metadata.keep.min.commits = 20",
    "meta" : "default: 20",
    "version" : "0.7.0",
    "docHTML" : "Archiving service moves older entries from metadata table’s timeline into an archived log after each write, to keep the overhead constant, even as the metadata table size grows.  This config controls the minimum number of instants to retain in the active timeline."
}, {
    "caption" : "hoodie.metadata.keep.max.commits",
    "value" : "hoodie.metadata.keep.max.commits = 30",
    "meta" : "default: 30",
    "version" : "0.7.0",
    "docHTML" : "Similar to hoodie.metadata.keep.min.commits, this config controls the maximum number of instants to retain in the active timeline."
}, {
    "caption" : "hoodie.metadata.cleaner.commits.retained",
    "value" : "hoodie.metadata.cleaner.commits.retained = 3",
    "meta" : "default: 3",
    "version" : "0.7.0",
    "docHTML" : "Number of commits to retain, without cleaning, on metadata table. This is an internal config and setting this will not overwrite the actual value used."
}, {
    "caption" : "hoodie.metadata.dir.filter.regex",
    "value" : "hoodie.metadata.dir.filter.regex = ",
    "meta" : "default: ",
    "version" : "0.7.0",
    "docHTML" : "Directories matching this regex, will be filtered out when initializing metadata table from lake storage for the first time."
}, {
    "caption" : "hoodie.assume.date.partitioning",
    "value" : "hoodie.assume.date.partitioning = false",
    "meta" : "default: false",
    "version" : "0.3.0",
    "docHTML" : "Should HoodieWriteClient assume the data is partitioned by dates, i.e three levels from base path. This is a stop-gap to support tables created by versions < 0.3.1. Will be removed eventually"
}, {
    "caption" : "hoodie.file.listing.parallelism",
    "value" : "hoodie.file.listing.parallelism = 200",
    "meta" : "default: 200",
    "version" : "0.7.0",
    "docHTML" : "Parallelism to use, when listing the table on lake storage."
}, {
    "caption" : "hoodie.metadata.enable.full.scan.log.files",
    "value" : "hoodie.metadata.enable.full.scan.log.files = true",
    "meta" : "default: true",
    "version" : "0.10.0",
    "docHTML" : "Enable full scanning of log files while reading log records. If disabled, Hudi does look up of only interested entries. This is an internal config and setting this will not overwrite the actual value used."
}, {
    "caption" : "hoodie.metadata.index.bloom.filter.enable",
    "value" : "hoodie.metadata.index.bloom.filter.enable = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Enable indexing bloom filters of user data files under metadata table. When enabled, metadata table will have a partition to store the bloom filter index and will be used during the index lookups."
}, {
    "caption" : "hoodie.metadata.index.bloom.filter.file.group.count",
    "value" : "hoodie.metadata.index.bloom.filter.file.group.count = 4",
    "meta" : "default: 4",
    "version" : "0.11.0",
    "docHTML" : "Metadata bloom filter index partition file group count. This controls the size of the base and log files and read parallelism in the bloom filter index partition. The recommendation is to size the file group count such that the base files are under 1GB."
}, {
    "caption" : "hoodie.metadata.index.bloom.filter.parallelism",
    "value" : "hoodie.metadata.index.bloom.filter.parallelism = 200",
    "meta" : "default: 200",
    "version" : "0.11.0",
    "docHTML" : "Parallelism to use for generating bloom filter index in metadata table."
}, {
    "caption" : "hoodie.metadata.index.column.stats.enable",
    "value" : "hoodie.metadata.index.column.stats.enable = false",
    "meta" : "default: false",
    "version" : "0.11.0",
    "docHTML" : "Enable indexing column ranges of user data files under metadata table key lookups. When enabled, metadata table will have a partition to store the column ranges and will be used for pruning files during the index lookups."
}, {
    "caption" : "hoodie.metadata.index.column.stats.file.group.count",
    "value" : "hoodie.metadata.index.column.stats.file.group.count = 2",
    "meta" : "default: 2",
    "version" : "0.11.0",
    "docHTML" : "Metadata column stats partition file group count. This controls the size of the base and log files and read parallelism in the column stats index partition. The recommendation is to size the file group count such that the base files are under 1GB."
}, {
    "caption" : "hoodie.metadata.index.column.stats.parallelism",
    "value" : "hoodie.metadata.index.column.stats.parallelism = 10",
    "meta" : "default: 10",
    "version" : "0.11.0",
    "docHTML" : "Parallelism to use, when generating column stats index."
}, {
    "caption" : "hoodie.metadata.index.column.stats.column.list",
    "value" : "hoodie.metadata.index.column.stats.column.list = ",
    "version" : "0.11.0",
    "docHTML" : "Comma-separated list of columns for which column stats index will be built. If not set, all columns will be indexed"
}, {
    "caption" : "hoodie.metadata.index.column.stats.processing.mode.override",
    "value" : "hoodie.metadata.index.column.stats.processing.mode.override = ",
    "version" : "0.12.0",
    "docHTML" : "By default Column Stats Index is automatically determining whether it should be read and processed either'in-memory' (w/in executing process) or using Spark (on a cluster), based on some factors like the size of the Index and how many columns are read. This config allows to override this behavior."
}, {
    "caption" : "hoodie.metadata.index.column.stats.inMemory.projection.threshold",
    "value" : "hoodie.metadata.index.column.stats.inMemory.projection.threshold = 100000",
    "meta" : "default: 100000",
    "version" : "0.12.0",
    "docHTML" : "When reading Column Stats Index, if the size of the expected resulting projection is below the in-memory threshold (counted by the # of rows), it will be attempted to be loaded \"in-memory\" (ie not using the execution engine like Spark, Flink, etc). If the value is above the threshold execution engine will be used to compose the projection."
}, {
    "caption" : "hoodie.metadata.index.bloom.filter.column.list",
    "value" : "hoodie.metadata.index.bloom.filter.column.list = ",
    "version" : "0.11.0",
    "docHTML" : "Comma-separated list of columns for which bloom filter index will be built. If not set, only record key will be indexed."
}, {
    "caption" : "hoodie.metadata.index.check.timeout.seconds",
    "value" : "hoodie.metadata.index.check.timeout.seconds = 900",
    "meta" : "default: 900",
    "version" : "0.11.0",
    "docHTML" : "After the async indexer has finished indexing upto the base instant, it will ensure that all inflight writers reliably write index updates as well. If this timeout expires, then the indexer will abort itself safely."
}, {
    "caption" : "hoodie.metadata.populate.meta.fields",
    "value" : "hoodie.metadata.populate.meta.fields = false",
    "meta" : "default: false",
    "version" : "0.10.0",
    "docHTML" : "When enabled, populates all meta fields. When disabled, no meta fields are populated. This is an internal config and setting this will not overwrite the actual value used."
}, {
    "caption" : "_hoodie.metadata.ignore.spurious.deletes",
    "value" : "_hoodie.metadata.ignore.spurious.deletes = true",
    "meta" : "default: true",
    "version" : "0.10.0",
    "docHTML" : "There are cases when extra files are requested to be deleted from metadata table which are never added before. This config determines how to handle such spurious deletes"
}, {
    "caption" : "hoodie.metadata.optimized.log.blocks.scan.enable",
    "value" : "hoodie.metadata.optimized.log.blocks.scan.enable = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Optimized log blocks scanner that addresses all the multiwriter use-cases while appending to log files. It also differentiates original blocks written by ingestion writers and compacted blocks written by log compaction."
}, {
    "caption" : "hoodie.metaserver.enabled",
    "value" : "hoodie.metaserver.enabled = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Enable Hudi metaserver for storing Hudi tables' metadata."
}, {
    "caption" : "hoodie.database.name",
    "value" : "hoodie.database.name = ",
    "version" : "0.13.0",
    "docHTML" : "Database name that will be used for incremental query.If different databases have the same table name during incremental query, we can set it to limit the table name under a specific database"
}, {
    "caption" : "hoodie.table.name",
    "value" : "hoodie.table.name = ",
    "version" : "0.13.0",
    "docHTML" : "Table name that will be used for registering with Hive. Needs to be same across runs."
}, {
    "caption" : "hoodie.metaserver.uris",
    "value" : "hoodie.metaserver.uris = thrift://localhost:9090",
    "meta" : "default: thrift://localhost:9090",
    "version" : "0.13.0",
    "docHTML" : "Metastore server uris"
}, {
    "caption" : "hoodie.metaserver.connect.retries",
    "value" : "hoodie.metaserver.connect.retries = 3",
    "meta" : "default: 3",
    "version" : "0.13.0",
    "docHTML" : "Number of retries while opening a connection to metastore"
}, {
    "caption" : "hoodie.metaserver.connect.retry.delay",
    "value" : "hoodie.metaserver.connect.retry.delay = 1",
    "meta" : "default: 1",
    "version" : "0.13.0",
    "docHTML" : "Number of seconds for the client to wait between consecutive connection attempts"
}, {
    "caption" : "hoodie.metrics.on",
    "value" : "hoodie.metrics.on = false",
    "meta" : "default: false",
    "version" : "0.5.0",
    "docHTML" : "Turn on/off metrics reporting. off by default."
}, {
    "caption" : "hoodie.metrics.reporter.type",
    "value" : "hoodie.metrics.reporter.type = GRAPHITE",
    "meta" : "default: GRAPHITE",
    "version" : "0.5.0",
    "docHTML" : "Type of metrics reporter."
}, {
    "caption" : "hoodie.metrics.reporter.class",
    "value" : "hoodie.metrics.reporter.class = ",
    "meta" : "default: ",
    "version" : "0.6.0",
    "docHTML" : ""
}, {
    "caption" : "hoodie.metrics.reporter.metricsname.prefix",
    "value" : "hoodie.metrics.reporter.metricsname.prefix = ",
    "meta" : "default: ",
    "version" : "0.11.0",
    "docHTML" : "The prefix given to the metrics names."
}, {
    "caption" : "hoodie.metrics.executor.enable",
    "value" : "hoodie.metrics.executor.enable = ",
    "version" : "0.7.0",
    "docHTML" : ""
}, {
    "caption" : "hoodie.metrics.lock.enable",
    "value" : "hoodie.metrics.lock.enable = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "Enable metrics for locking infra. Useful when operating in multiwriter mode"
}, {
    "caption" : "hoodie.metrics.pushgateway.host",
    "value" : "hoodie.metrics.pushgateway.host = localhost",
    "meta" : "default: localhost",
    "version" : "0.6.0",
    "docHTML" : "Hostname of the prometheus push gateway."
}, {
    "caption" : "hoodie.metrics.pushgateway.port",
    "value" : "hoodie.metrics.pushgateway.port = 9091",
    "meta" : "default: 9091",
    "version" : "0.6.0",
    "docHTML" : "Port for the push gateway."
}, {
    "caption" : "hoodie.metrics.pushgateway.report.period.seconds",
    "value" : "hoodie.metrics.pushgateway.report.period.seconds = 30",
    "meta" : "default: 30",
    "version" : "0.6.0",
    "docHTML" : "Reporting interval in seconds."
}, {
    "caption" : "hoodie.metrics.pushgateway.delete.on.shutdown",
    "value" : "hoodie.metrics.pushgateway.delete.on.shutdown = true",
    "meta" : "default: true",
    "version" : "0.6.0",
    "docHTML" : "Delete the pushgateway info or not when job shutdown, true by default."
}, {
    "caption" : "hoodie.metrics.pushgateway.job.name",
    "value" : "hoodie.metrics.pushgateway.job.name = ",
    "meta" : "default: ",
    "version" : "0.6.0",
    "docHTML" : "Name of the push gateway job."
}, {
    "caption" : "hoodie.metrics.pushgateway.random.job.name.suffix",
    "value" : "hoodie.metrics.pushgateway.random.job.name.suffix = true",
    "meta" : "default: true",
    "version" : "0.6.0",
    "docHTML" : "Whether the pushgateway name need a random suffix , default true."
}, {
    "caption" : "hoodie.metrics.prometheus.port",
    "value" : "hoodie.metrics.prometheus.port = 9090",
    "meta" : "default: 9090",
    "version" : "0.6.0",
    "docHTML" : "Port for prometheus server."
}, {
    "caption" : "hoodie.payload.ordering.field",
    "value" : "hoodie.payload.ordering.field = ts",
    "meta" : "default: ts",
    "docHTML" : "Table column/field name to order records that have the same key, before merging and writing to storage."
}, {
    "caption" : "hoodie.payload.event.time.field",
    "value" : "hoodie.payload.event.time.field = ts",
    "meta" : "default: ts",
    "docHTML" : "Table column/field name to derive timestamp associated with the records. This canbe useful for e.g, determining the freshness of the table."
}, {
    "caption" : "hoodie.compaction.payload.class",
    "value" : "hoodie.compaction.payload.class = org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "meta" : "default: org.apache.hudi.common.model.OverwriteWithLatestAvroPayload",
    "docHTML" : "This needs to be same as class used during insert/upserts. Just like writing, compaction also uses the record payload class to merge records in the log against each other, merge again with the base file and produce the final record to be written after compaction."
}, {
    "caption" : "hoodie.precommit.validators",
    "value" : "hoodie.precommit.validators = ",
    "meta" : "default: ",
    "docHTML" : "Comma separated list of class names that can be invoked to validate commit"
}, {
    "caption" : "hoodie.precommit.validators.equality.sql.queries",
    "value" : "hoodie.precommit.validators.equality.sql.queries = ",
    "meta" : "default: ",
    "docHTML" : "Spark SQL queries to run on table before committing new data to validate state before and after commit. Multiple queries separated by ';' delimiter are supported. Example: \"select count(*) from \\<TABLE_NAME\\> Note \\<TABLE_NAME\\> is replaced by table state before and after commit."
}, {
    "caption" : "hoodie.precommit.validators.single.value.sql.queries",
    "value" : "hoodie.precommit.validators.single.value.sql.queries = ",
    "meta" : "default: ",
    "docHTML" : "Spark SQL queries to run on table before committing new data to validate state after commit.Multiple queries separated by ';' delimiter are supported.Expected result is included as part of query separated by '#'. Example query: 'query1#result1:query2#result2'Note \\<TABLE_NAME\\> variable is expected to be present in query."
}, {
    "caption" : "hoodie.precommit.validators.inequality.sql.queries",
    "value" : "hoodie.precommit.validators.inequality.sql.queries = ",
    "meta" : "default: ",
    "docHTML" : "Spark SQL queries to run on table before committing new data to validate state before and after commit.Multiple queries separated by ';' delimiter are supported.Example query: 'select count(*) from \\<TABLE_NAME\\> where col=null'Note \\<TABLE_NAME\\> variable is expected to be present in query."
}, {
    "caption" : "hoodie.parquet.max.file.size",
    "value" : "hoodie.parquet.max.file.size = 125829120",
    "meta" : "default: 125829120",
    "docHTML" : "Target size in bytes for parquet files produced by Hudi write phases. For DFS, this needs to be aligned with the underlying filesystem block size for optimal performance."
}, {
    "caption" : "hoodie.parquet.block.size",
    "value" : "hoodie.parquet.block.size = 125829120",
    "meta" : "default: 125829120",
    "docHTML" : "Parquet RowGroup size in bytes. It's recommended to make this large enough that scan costs can be amortized by packing enough column values into a single row group."
}, {
    "caption" : "hoodie.parquet.page.size",
    "value" : "hoodie.parquet.page.size = 1048576",
    "meta" : "default: 1048576",
    "docHTML" : "Parquet page size in bytes. Page is the unit of read within a parquet file. Within a block, pages are compressed separately."
}, {
    "caption" : "hoodie.orc.max.file.size",
    "value" : "hoodie.orc.max.file.size = 125829120",
    "meta" : "default: 125829120",
    "docHTML" : "Target file size in bytes for ORC base files."
}, {
    "caption" : "hoodie.orc.stripe.size",
    "value" : "hoodie.orc.stripe.size = 67108864",
    "meta" : "default: 67108864",
    "docHTML" : "Size of the memory buffer in bytes for writing"
}, {
    "caption" : "hoodie.orc.block.size",
    "value" : "hoodie.orc.block.size = 125829120",
    "meta" : "default: 125829120",
    "docHTML" : "ORC block size, recommended to be aligned with the target file size."
}, {
    "caption" : "hoodie.hfile.max.file.size",
    "value" : "hoodie.hfile.max.file.size = 125829120",
    "meta" : "default: 125829120",
    "docHTML" : "Target file size in bytes for HFile base files."
}, {
    "caption" : "hoodie.hfile.block.size",
    "value" : "hoodie.hfile.block.size = 1048576",
    "meta" : "default: 1048576",
    "docHTML" : "Lower values increase the size in bytes of metadata tracked within HFile, but can offer potentially faster lookup times."
}, {
    "caption" : "hoodie.logfile.data.block.format",
    "value" : "hoodie.logfile.data.block.format = ",
    "docHTML" : "Format of the data block within delta logs. Following formats are currently supported \"avro\", \"hfile\", \"parquet\""
}, {
    "caption" : "hoodie.logfile.max.size",
    "value" : "hoodie.logfile.max.size = 1073741824",
    "meta" : "default: 1073741824",
    "docHTML" : "LogFile max size in bytes. This is the maximum size allowed for a log file before it is rolled over to the next version."
}, {
    "caption" : "hoodie.logfile.data.block.max.size",
    "value" : "hoodie.logfile.data.block.max.size = 268435456",
    "meta" : "default: 268435456",
    "docHTML" : "LogFile Data block max size in bytes. This is the maximum size allowed for a single data block to be appended to a log file. This helps to make sure the data appended to the log file is broken up into sizable blocks to prevent from OOM errors. This size should be greater than the JVM memory."
}, {
    "caption" : "hoodie.parquet.compression.ratio",
    "value" : "hoodie.parquet.compression.ratio = 0.1",
    "meta" : "default: 0.1",
    "docHTML" : "Expected compression of parquet data used by Hudi, when it tries to size new parquet files. Increase this value, if bulk_insert is producing smaller than expected sized files"
}, {
    "caption" : "hoodie.parquet.compression.codec",
    "value" : "hoodie.parquet.compression.codec = gzip",
    "meta" : "default: gzip",
    "docHTML" : "Compression Codec for parquet files"
}, {
    "caption" : "hoodie.parquet.dictionary.enabled",
    "value" : "hoodie.parquet.dictionary.enabled = true",
    "meta" : "default: true",
    "docHTML" : "Whether to use dictionary encoding"
}, {
    "caption" : "hoodie.parquet.writelegacyformat.enabled",
    "value" : "hoodie.parquet.writelegacyformat.enabled = false",
    "meta" : "default: false",
    "docHTML" : "Sets spark.sql.parquet.writeLegacyFormat. If true, data will be written in a way of Spark 1.4 and earlier. For example, decimal values will be written in Parquet's fixed-length byte array format which other systems such as Apache Hive and Apache Impala use. If false, the newer format in Parquet will be used. For example, decimals will be written in int-based format."
}, {
    "caption" : "hoodie.parquet.outputtimestamptype",
    "value" : "hoodie.parquet.outputtimestamptype = TIMESTAMP_MICROS",
    "meta" : "default: TIMESTAMP_MICROS",
    "docHTML" : "Sets spark.sql.parquet.outputTimestampType. Parquet timestamp type to use when Spark writes data to Parquet files."
}, {
    "caption" : "hoodie.parquet.field_id.write.enabled",
    "value" : "hoodie.parquet.field_id.write.enabled = true",
    "meta" : "default: true",
    "version" : "0.12.0",
    "docHTML" : "Would only be effective with Spark 3.3+. Sets spark.sql.parquet.fieldId.write.enabled. If enabled, Spark will write out parquet native field ids that are stored inside StructField's metadata as parquet.field.id to parquet files."
}, {
    "caption" : "hoodie.hfile.compression.algorithm",
    "value" : "hoodie.hfile.compression.algorithm = GZ",
    "meta" : "default: GZ",
    "docHTML" : "Compression codec to use for hfile base files."
}, {
    "caption" : "hoodie.orc.compression.codec",
    "value" : "hoodie.orc.compression.codec = ZLIB",
    "meta" : "default: ZLIB",
    "docHTML" : "Compression codec to use for ORC base files."
}, {
    "caption" : "hoodie.logfile.to.parquet.compression.ratio",
    "value" : "hoodie.logfile.to.parquet.compression.ratio = 0.35",
    "meta" : "default: 0.35",
    "docHTML" : "Expected additional compression as records move from log files to parquet. Used for merge_on_read table to send inserts into log files & control the size of compacted parquet file."
}, {
    "caption" : "hoodie.table.service.manager.enabled",
    "value" : "hoodie.table.service.manager.enabled = false",
    "meta" : "default: false",
    "version" : "0.13.0",
    "docHTML" : "If true, use table service manager to execute table service"
}, {
    "caption" : "hoodie.table.service.manager.uris",
    "value" : "hoodie.table.service.manager.uris = http://localhost:9091",
    "meta" : "default: http://localhost:9091",
    "version" : "0.13.0",
    "docHTML" : "Table service manager URIs (comma-delimited)."
}, {
    "caption" : "hoodie.table.service.manager.actions",
    "value" : "hoodie.table.service.manager.actions = ",
    "version" : "0.13.0",
    "docHTML" : "The actions deployed on table service manager, such as compaction or clean."
}, {
    "caption" : "hoodie.table.service.manager.deploy.username",
    "value" : "hoodie.table.service.manager.deploy.username = default",
    "meta" : "default: default",
    "version" : "0.13.0",
    "docHTML" : "The user name for this table to deploy table services."
}, {
    "caption" : "hoodie.table.service.manager.deploy.queue",
    "value" : "hoodie.table.service.manager.deploy.queue = default",
    "meta" : "default: default",
    "version" : "0.13.0",
    "docHTML" : "The queue for this table to deploy table services."
}, {
    "caption" : "hoodie.table.service.manager.deploy.resources",
    "value" : "hoodie.table.service.manager.deploy.resources = spark:4g,4g",
    "meta" : "default: spark:4g,4g",
    "version" : "0.13.0",
    "docHTML" : "The resources for this table to use for deploying table services."
}, {
    "caption" : "hoodie.table.service.manager.deploy.parallelism",
    "value" : "hoodie.table.service.manager.deploy.parallelism = 100",
    "meta" : "default: 100",
    "version" : "0.13.0",
    "docHTML" : "The parallelism for this table to deploy table services."
}, {
    "caption" : "hoodie.table.service.manager.execution.engine",
    "value" : "hoodie.table.service.manager.execution.engine = spark",
    "meta" : "default: spark",
    "version" : "0.13.0",
    "docHTML" : "The execution engine to deploy for table service of this table, default spark"
}, {
    "caption" : "hoodie.table.service.manager.deploy.extra.params",
    "value" : "hoodie.table.service.manager.deploy.extra.params = ",
    "version" : "0.13.0",
    "docHTML" : "The extra params to deploy for table service of this table, split by ';'"
}, {
    "caption" : "hoodie.table.service.manager.connection.timeout.sec",
    "value" : "hoodie.table.service.manager.connection.timeout.sec = 300",
    "meta" : "default: 300",
    "version" : "0.13.0",
    "docHTML" : "Timeout in seconds for connections to table service manager."
}, {
    "caption" : "hoodie.table.service.manager.connection.retries",
    "value" : "hoodie.table.service.manager.connection.retries = 3",
    "meta" : "default: 3",
    "version" : "0.13.0",
    "docHTML" : "Number of retries while opening a connection to table service manager"
}, {
    "caption" : "hoodie.table.service.manager.connection.retry.delay.sec",
    "value" : "hoodie.table.service.manager.connection.retry.delay.sec = 1",
    "meta" : "default: 1",
    "version" : "0.13.0",
    "docHTML" : "Number of seconds for the client to wait between consecutive connection attempts"
}, {
    "caption" : "hoodie.write.commit.callback.on",
    "value" : "hoodie.write.commit.callback.on = false",
    "meta" : "default: false",
    "version" : "0.6.0",
    "docHTML" : "Turn commit callback on/off. off by default."
}, {
    "caption" : "hoodie.write.commit.callback.class",
    "value" : "hoodie.write.commit.callback.class = org.apache.hudi.callback.impl.HoodieWriteCommitHttpCallback",
    "meta" : "default: org.apache.hudi.callback.impl.HoodieWriteCommitHttpCallback",
    "version" : "0.6.0",
    "docHTML" : "Full path of callback class and must be a subclass of HoodieWriteCommitCallback class, org.apache.hudi.callback.impl.HoodieWriteCommitHttpCallback by default"
}, {
    "caption" : "hoodie.write.commit.callback.http.url",
    "value" : "hoodie.write.commit.callback.http.url = ",
    "version" : "0.6.0",
    "docHTML" : "Callback host to be sent along with callback messages"
}, {
    "caption" : "hoodie.write.commit.callback.http.api.key",
    "value" : "hoodie.write.commit.callback.http.api.key = hudi_write_commit_http_callback",
    "meta" : "default: hudi_write_commit_http_callback",
    "version" : "0.6.0",
    "docHTML" : "Http callback API key. hudi_write_commit_http_callback by default"
}, {
    "caption" : "hoodie.write.commit.callback.http.timeout.seconds",
    "value" : "hoodie.write.commit.callback.http.timeout.seconds = 3",
    "meta" : "default: 3",
    "version" : "0.6.0",
    "docHTML" : "Callback timeout in seconds. 3 by default"
}, {
    "caption" : "hoodie.datasource.write.partitionpath.urlencode",
    "value" : "hoodie.datasource.write.partitionpath.urlencode = false",
    "meta" : "default: false",
    "docHTML" : "Should we url encode the partition path value, before creating the folder structure."
}, {
    "caption" : "hoodie.datasource.write.hive_style_partitioning",
    "value" : "hoodie.datasource.write.hive_style_partitioning = false",
    "meta" : "default: false",
    "docHTML" : "Flag to indicate whether to use Hive style partitioning.\nIf set true, the names of partition folders follow <partition_column_name>=<partition_value> format.\nBy default false (the names of partition folders are only partition values)"
}, {
    "caption" : "hoodie.datasource.write.recordkey.field",
    "value" : "hoodie.datasource.write.recordkey.field = ",
    "docHTML" : "Record key field. Value to be used as the `recordKey` component of `HoodieKey`.\nActual value will be obtained by invoking .toString() on the field value. Nested fields can be specified using\nthe dot notation eg: `a.b.c`"
}, {
    "caption" : "hoodie.datasource.write.partitionpath.field",
    "value" : "hoodie.datasource.write.partitionpath.field = ",
    "docHTML" : "Partition path field. Value to be used at the partitionPath component of HoodieKey. Actual value ontained by invoking .toString()"
}, {
    "caption" : "hoodie.datasource.write.keygenerator.consistent.logical.timestamp.enabled",
    "value" : "hoodie.datasource.write.keygenerator.consistent.logical.timestamp.enabled = false",
    "meta" : "default: false",
    "docHTML" : "When set to true, consistent value will be generated for a logical timestamp type column, like timestamp-millis and timestamp-micros, irrespective of whether row-writer is enabled. Disabled by default so as not to break the pipeline that deploy either fully row-writer path or non row-writer path. For example, if it is kept disabled then record key of timestamp type with value `2016-12-29 09:54:00` will be written as timestamp `2016-12-29 09:54:00.0` in row-writer path, while it will be written as long value `1483023240000000` in non row-writer path. If enabled, then the timestamp value will be written in both the cases."
} ]