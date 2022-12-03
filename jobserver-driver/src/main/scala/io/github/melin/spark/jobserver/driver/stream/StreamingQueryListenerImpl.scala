package io.github.melin.spark.jobserver.driver.stream

import com.typesafe.scalalogging.Logger
import io.github.melin.spark.jobserver.driver.util.LogUtils
import org.apache.spark.sql.streaming.StreamingQueryListener
import org.apache.spark.sql.streaming.StreamingQueryListener.{QueryProgressEvent, QueryStartedEvent, QueryTerminatedEvent}

/**
 * Created by libinsong on 2020/7/30 11:03 下午
 */
class StreamingQueryListenerImpl() extends StreamingQueryListener {
  private val logger = Logger(classOf[StreamingQueryListenerImpl])

  override def onQueryStarted(queryStarted: QueryStartedEvent): Unit = {
    logger.info("Query started: " + queryStarted.id)
  }
  override def onQueryTerminated(queryTerminated: QueryTerminatedEvent): Unit = {
    logger.info("Query terminated: " + queryTerminated.id)
  }
  override def onQueryProgress(queryProgress: QueryProgressEvent): Unit = {
    val progress = queryProgress.progress
    val msg = s"id: ${progress.id}, numInputRows: ${progress.numInputRows}, " +
      s"inputRowsPerSecond: ${progress.inputRowsPerSecond}, processedRowsPerSecond: ${progress.processedRowsPerSecond}";
    LogUtils.info(msg);
  }
}
