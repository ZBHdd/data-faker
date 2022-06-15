
package com.dunnhumby.datafaker

import com.dunnhumby.datafaker.Application.{parsedArgs, spark}
import com.dunnhumby.datafaker.schema.Schema
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

class DataGenerator(spark: SparkSession, parsedArgs: Map[String, String]) extends Serializable {

  private val logger = Logger.getLogger(getClass)
  logger.setLevel(Level.INFO)

  def generateAndWriteDataFromSchema(schema: Schema): Unit = {
    for (table <- schema.tables) {
      logger.info(s"generating and writing ${table.rows} rows for ${table.name}")

      val partitions = table.partitions.getOrElse(List.empty[String])
      val dataFrame = table.columns.foldLeft(spark.range(table.rows).toDF("rowID"))((a, b) => {
        a.withColumn(b.name, b.column())
      }).drop("rowID")

      parsedArgs.get("dataType") match {
        case None => logger.info("no dataType")
        case Some("csv") => dataFrame.write.mode(SaveMode.Overwrite).csv(s"${parsedArgs("outPath")}/${table.name}")
      }

      if (parsedArgs.contains("database")) {
        val database = parsedArgs("database")
        parsedArgs.get("store") match {
          case None => logger.info("no store")
          case Some("orc") =>
            dataFrame.write.mode(SaveMode.Overwrite).partitionBy(partitions: _*).orc(s"$database.${table.name}")
          case _ =>
            dataFrame.write.mode(SaveMode.Overwrite).partitionBy(partitions: _*).saveAsTable(s"$database.${table.name}")
        }
      }


      logger.info(s"${table.name} - complete")
    }
  }

}
