package io.github.melin.spark.jobserver.api;

import java.io.Serializable;
import org.apache.spark.sql.SparkSession;

public interface SparkJob extends Serializable {

    void runJob(SparkSession sparkSession, String[] args) throws Exception;
}
