package com.github.melin.jobserver.extensions.util

import java.io.{BufferedReader, InputStreamReader}

import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * https://gist.github.com/robertchong/11071949#file-dataconvertionutil-java-L23
 *
 * Created by libinsong on 2019/12/15 11:19 上午
 */
object DataConvertionUtil {

  /**
   * Comma separated characters
   */
  private val CVS_SEPERATOR_CHAR = ","

  /**
   * Convert CSV file to Excel file
   *
   * @param inputPath
   * @param outputPath
   * @throws Exception
   */
  @throws[Exception]
  def csvToEXCEL(fileSystem: FileSystem, inputPath: Path, outputPath: Path): Unit = {
    val reader = new BufferedReader(new InputStreamReader(fileSystem.open(inputPath)));
    val writer = fileSystem.create(outputPath, false)

    val wb = new XSSFWorkbook
    val myWorkBook = new SXSSFWorkbook(wb)

    val mySheet = myWorkBook.createSheet()
    var rowNo = 0
    var line = reader.readLine
    while (StringUtils.isNotBlank(line)) {
      val columns = line.split(CVS_SEPERATOR_CHAR)
      val myRow = mySheet.createRow(rowNo)
      for (i <- 0 until columns.length) {
        val myCell = myRow.createCell(i)
        val value = columns(i)
        if ("\"\"" != value && StringUtils.isNotBlank(value)) {
          myCell.setCellValue(value)
        }
      }
      rowNo += 1

      line = reader.readLine();
    }
    myWorkBook.write(writer)
    wb.close()
    myWorkBook.dispose()
    writer.close()
  }
}
