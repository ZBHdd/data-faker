
package com.dunnhumby.datafaker

import com.github.javafaker.Faker
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Application extends App {

  private val logger = Logger.getLogger("org.apache.spark.SparkContext")
  logger.setLevel(Level.WARN)
  logger.info(s"receive parameters ${args.toList}")
  val parsedArgs = ArgsParser.validateArgs(ArgsParser.parseArgs(args.toList))
  val conf = new SparkConf()
    .set("spark.ui.showConsoleProgress", "true")
    .setAppName("data-faker")
  val spark: SparkSession = SparkSession
    .builder()
    .config(conf)
    .enableHiveSupport()
    .getOrCreate()

  spark.sparkContext.setLogLevel("OFF")

  //  val sharedFaker = SharedVariable(new Faker())
  spark.udf.register("fake", (e: String) => {
    Singletons.faker.expression(e)
  })

  if (parsedArgs.contains("database")) {
    logger.info(s"ready create database if not exists ${parsedArgs("database")}")
    spark.sql(s"create database if not exists ${parsedArgs("database")}")
  }

  val schema = YamlParser.parseSchemaFromFile(parsedArgs("file"))
  val dataGenerator = new DataGenerator(spark, parsedArgs)

  dataGenerator.generateAndWriteDataFromSchema(schema)
}
