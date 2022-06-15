
package com.dunnhumby.datafaker

object ArgsParser {
  val keys = List("--database", "--file", "--dataType","--store","--outPath")

  def parseArgs(list: List[String]): Map[String, String] = {
    var parsedArgs = Map[String, String]()
    for (i <- list.indices) {
      if (keys.contains(list(i))) {
        parsedArgs = parsedArgs + (list(i).replaceFirst("--","") -> list(i+1))
      }
    }
    parsedArgs
  }

  def validateArgs(args: Map[String, String]): Map[String, String] = {
    args
  }
}