package io.github.melin.spark.jobserver.driver.support;

import io.github.melin.spark.jobserver.api.LogUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.hadoop.util.HiddenFileFilter;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MergeParquetSmallFile {

    private static final Logger LOG = LoggerFactory.getLogger(MergeParquetSmallFile.class);

    private static final Long MAX_RECORDS_PER_FILE = 1_000_000L;

    private static final Long MiB = 1048576L;

    public void mergeFile(SparkSession sparkSession, String location) throws IOException {
        Configuration conf = sparkSession.sparkContext().hadoopConfiguration();
        FileSystem fs = FileSystem.get(conf);
        if (!fs.exists(new Path(location))) {
            throw new IllegalStateException("merge failed, " + location + " does not exist");
        }

        FileStatus[] fileStatusList = fs.listStatus(new Path(location), HiddenFileFilter.INSTANCE);
        if (fileStatusList.length == 0) {
            LogUtils.warn("{} 没有parquet 数据文件", location);
            return;
        }

        List<FileStatus> files = Arrays.stream(fileStatusList).filter(FileStatus::isFile).collect(Collectors.toList());
        if (files.size() <= 1) {
            return;
        }

        long recordCount = statParquetRowCount(conf, files);
        long fileSize = files.stream().mapToLong(FileStatus::getLen).sum();
        String[] mergePaths = files.stream().map(fileStatus -> fileStatus.getPath().toString()).toArray(String[]::new);
        int mergeCount = genOutputFileCount(recordCount, fileSize);

        String mergeTempDir = location + "/.mergeTemp";
        long startTime = System.currentTimeMillis();
        LogUtils.info("开始合并小文件: {}, 文件数量: {}", location, files.size());

        sparkSession.read().parquet(mergePaths).coalesce(mergeCount).write().parquet(mergeTempDir);

        FileStatus[] mergeFiles = fs.listStatus(new Path(mergeTempDir), HiddenFileFilter.INSTANCE);
        for (FileStatus mergeFile : mergeFiles) {
            String newLocation = location + "/" + mergeFile.getPath().getName();
            fs.rename(mergeFile.getPath(), new Path(newLocation));
        }

        LOG.info("开始删除源文件: " + files.size());
        for (FileStatus fileStatus : files) {
            fs.delete(fileStatus.getPath(), true);
        }
        fs.delete(new Path(mergeTempDir), true);
        LOG.info("开始删除源文件结束");

        long execTimes = TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis() - startTime) + 1;
        String msg = "合并小文件完成，原文件数量: {}, 合并后文件数量: {}, 数据量: {}, 合并耗时: {}s";
        LogUtils.info(msg, files.size(), mergeCount, recordCount, execTimes);
    }

    private Long statParquetRowCount(Configuration conf, List<FileStatus> files) {
        AtomicLong fileRowCount = new AtomicLong(0);
        files.forEach(fileStatus -> {
            try {
                HadoopInputFile inputFile = HadoopInputFile.fromStatus(fileStatus, conf);
                ParquetFileReader parquetFileReader = ParquetFileReader.open(inputFile);

                List<BlockMetaData> blockMetaDataList = parquetFileReader.getFooter().getBlocks();
                for (BlockMetaData metaData : blockMetaDataList) {
                    fileRowCount.addAndGet(metaData.getRowCount());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return fileRowCount.get();
    }

    // 计算输出文件(分区)数量
    private int genOutputFileCount(long recordCount, long fileSize) {
        int num1 = (int) Math.ceil(recordCount * 1.0d / MAX_RECORDS_PER_FILE);
        int num2 = (int) Math.ceil(fileSize * 1.0D / (256 * MiB));

        return Math.max(num1, num2);
    }
}
