
package com.dunnhumby.datafaker

import org.apache.spark.sql.SparkSession


trait SharedSparkSession {

  lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("Test")
      .getOrCreate()
  }

}
