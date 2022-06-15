
package com.dunnhumby.datafaker

import com.github.javafaker.Faker

import java.sql.{Date, Timestamp}
import org.scalatest.{MustMatchers, WordSpec}

class YamlParserTest extends WordSpec with MustMatchers {

  import com.dunnhumby.datafaker.YamlParser.YamlParserProtocol._
  import net.jcazevedo.moultingyaml._

  "YamlParser" must {
    "convert a YamlDate to java.sql.Date" in {
      val date = "1998-06-03"
      val string = s"""$date""".stripMargin
      string.parseYaml.convertTo[Date] mustBe Date.valueOf(date)
    }

    "convert a YamlDate to java.sql.Timestamp" in {
      val timestamp = "1998-06-03 01:23:45"
      val string = s"""$timestamp""".stripMargin
      string.parseYaml.convertTo[Timestamp] mustBe Timestamp.valueOf(timestamp)
    }

    "fake test" in {
      println(new Faker().expression("#{Address.city_name}"))
      println(new Faker().expression("#{Address.state}"))
      println(new Faker().expression("#{Address.country_code}"))
      println(new Faker().expression("#{Address.full_address}"))
      1 mustBe 1
    }
  }
}
