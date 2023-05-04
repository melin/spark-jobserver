package com.github.melin.jobserver.extensions

import com.github.melin.jobserver.extensions.parser.SuperiorSparkSqlParser
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSessionExtensions

class SparkJobserverExtensions extends (SparkSessionExtensions => Unit) with Logging {

  override def apply(extensions: SparkSessionExtensions): Unit = {
    extensions.injectParser { (session, parser) =>
      new SuperiorSparkSqlParser(session, parser)
    }
  }
}
