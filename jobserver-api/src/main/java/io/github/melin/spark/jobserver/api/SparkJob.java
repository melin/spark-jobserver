package io.github.melin.spark.jobserver.api;

import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

/**
 * Created by admin on 2017/7/25.
 */
public interface SparkJob extends Serializable{

    void runJob(SparkSession sparkSession, String[] args) throws Exception;
}
