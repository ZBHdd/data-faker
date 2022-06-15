
package com.dunnhumby.datafaker.schema.table.columns

import com.dunnhumby.datafaker.YamlParser.YamlParserProtocol
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.DataType

import java.sql.{Date, Timestamp}

case class SchemaColumnFixed[T](override val name: String, value: T, cast: Option[DataType] = None) extends SchemaColumn {
  override def column(rowID: Option[Column] = None): Column =
    cast.map(lit(value).cast).getOrElse(lit(value))

}

object SchemaColumnFixedProtocol extends SchemaColumnFixedProtocol

trait SchemaColumnFixedProtocol extends YamlParserProtocol {

  import net.jcazevedo.moultingyaml._

  implicit object SchemaColumnFixedFormat extends YamlFormat[SchemaColumnFixed[_]] {

    override def read(yaml: YamlValue): SchemaColumnFixed[_] = {
      val fields = yaml.asYamlObject.fields
      val YamlString(name) = fields.getOrElse(YamlString("name"), deserializationError("name not set"))
      val YamlString(dataType) = fields.getOrElse(YamlString("data_type"), deserializationError(s"data_type not set for $name"))
      val value = fields.getOrElse(YamlString("value"), deserializationError(s"value not set for $name"))

      val maybeCast = fields.get(YamlString("cast")) match {
        case Some(YamlString(dType)) => Some(DataType.fromDDL(dType))
        case _ => None
      }

      dataType match {
        case SchemaColumnDataType.Int => SchemaColumnFixed(name, value.convertTo[Int], maybeCast)
        case SchemaColumnDataType.Long => SchemaColumnFixed(name, value.convertTo[Long], maybeCast)
        case SchemaColumnDataType.Float => SchemaColumnFixed(name, value.convertTo[Float], maybeCast)
        case SchemaColumnDataType.Double => SchemaColumnFixed(name, value.convertTo[Double], maybeCast)
        case SchemaColumnDataType.Date => SchemaColumnFixed(name, value.convertTo[Date], maybeCast)
        case SchemaColumnDataType.Timestamp => SchemaColumnFixed(name, value.convertTo[Timestamp], maybeCast)
        case SchemaColumnDataType.String => SchemaColumnFixed(name, value.convertTo[String], maybeCast)
        case SchemaColumnDataType.Boolean => SchemaColumnFixed(name, value.convertTo[Boolean], maybeCast)
        case _ => deserializationError(s"unsupported data_type: $dataType for ${SchemaColumnType.Fixed}")
      }

    }

    override def write(obj: SchemaColumnFixed[_]): YamlValue = ???

  }

}