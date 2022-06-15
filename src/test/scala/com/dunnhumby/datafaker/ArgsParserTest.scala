
package com.dunnhumby.datafaker

import java.sql.{Date, Timestamp}
import org.scalatest.{MustMatchers, WordSpec}

class ArgsParserTest extends WordSpec with MustMatchers {

  import com.dunnhumby.datafaker.YamlParser.YamlParserProtocol._
  import net.jcazevedo.moultingyaml._

  "ArgsParser" must {
    "accepts --file arg" in {
      ArgsParser.parseArgs(List("--file", "test")) mustBe Map("file" -> "test")
    }

    "accepts --database arg" in {
      ArgsParser.parseArgs(List("--database", "test")) mustBe Map("database" -> "test")
    }

    "accepts --dataType arg" in {
      ArgsParser.parseArgs(List("--dataType", "test")) mustBe Map("dataType" -> "test")
    }

    "accepts --outPath arg" in {
      ArgsParser.parseArgs(List("--outPath", "test")) mustBe Map("outPath" -> "test")
    }

    "accepts --store arg" in {
      ArgsParser.parseArgs(List("--store", "test")) mustBe Map("store" -> "test")
    }

    "accepts all arg" in {
      ArgsParser.parseArgs(List("--store", "test","--file", "test","--outPath", "test")) mustBe Map("store" -> "test","file"->"test","outPath" -> "test")
    }
  }
}
