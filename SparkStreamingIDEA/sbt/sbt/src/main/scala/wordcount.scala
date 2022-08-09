package com.sundogsoftware.sparkstreaming

import org.apache.spark._

object wordcount {
  
  def main(args: Array[String]) {
      val conf = new SparkConf().setAppName("WordCount")
      //conf.setMaster("local[*]")
      val sc = new SparkContext(conf)

      //val input = sc.textFile("book.txt")
      val input = sc.textFile("s3n://sundog-spark/book.txt")
      val words = input.flatMap(line => line.split(' '))
      val lowerCaseWords = words.map(word => word.toLowerCase())
      val wordCounts = lowerCaseWords.countByValue()
      
      val sample = wordCounts.take(20)
      
      for ((word, count) <- sample) {
        println(word + " " + count)
      }
      
      sc.stop()
    }  
}