package com.github.melin.jobserver.extensions.util.zip

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.BytesWritable

/**
  * Created by zheng on 2019-01-15.
  */
object ProcessFile extends Serializable {
  def write(configuration: Configuration, fileName: String, records: BytesWritable, location: String): Unit = {
    val fs = FileSystem.get(configuration)
    if (records.getLength > 0) {
      val outFileStream = fs.create(new Path(location + "/" + fileName), true)
      outFileStream.write(records.getBytes)
      outFileStream.close()
    }
  }
}
