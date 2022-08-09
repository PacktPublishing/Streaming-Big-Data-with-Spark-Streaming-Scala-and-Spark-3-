// Kinesis setup steps (and more) at http://spark.apache.org/docs/latest/streaming-kinesis-integration.html

package com.sundogsoftware.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel

import java.util.regex.Pattern
import java.util.regex.Matcher

import Utilities._

 import org.apache.spark.streaming.Duration
 import org.apache.spark.streaming.kinesis._
import org.apache.spark.streaming.kinesis.KinesisInitialPositions.Latest
 
/** Example of connecting to Amazon Kinesis Streaming and listening for log data. */
object KinesisExample {
  
  def main(args: Array[String]) {

    // Create the context with a 1 second batch size
    val ssc = new StreamingContext("local[*]", "KinesisExample", Seconds(1))
    
    setupLogging()
    
    // Construct a regular expression (regex) to extract fields from raw Apache log lines
    val pattern = apacheLogPattern()

    // Create a Kinesis stream. You must create an app name unique for this region, and specify
    // stream name, Kinesis endpoint, and region you want.

    val kinesisStream = KinesisInputDStream.builder
      .streamingContext(ssc)
      .endpointUrl("kinesis.us-east-1.amazonaws.com")
    .regionName("us-east-1")
    .streamName("Stream Name")
    .initialPosition(new Latest())
    .checkpointAppName("Unique App Name")
    .checkpointInterval(Duration(2000))
    .storageLevel(StorageLevel.MEMORY_AND_DISK_2)
      .build()

    // This gives you a byte array for each message. Let's assume these represent strings.
    val lines = kinesisStream.map(x => new String(x))
    
    // Extract the request field from each log line
    val requests = lines.map(x => {val matcher:Matcher = pattern.matcher(x); if (matcher.matches()) matcher.group(5)})
    
    // Extract the URL from the request
    val urls = requests.map(x => {val arr = x.toString().split(" "); if (arr.size == 3) arr(1) else "[error]"})
    
    // Reduce by URL over a 5-minute window sliding every second
    val urlCounts = urls.map(x => (x, 1)).reduceByKeyAndWindow(_ + _, _ - _, Seconds(300), Seconds(1))
    
    // Sort and print the results
    val sortedResults = urlCounts.transform(rdd => rdd.sortBy(x => x._2, false))
    sortedResults.print()
    
    // Kick it off
    ssc.checkpoint("C:/checkpoint/")
    ssc.start()
    ssc.awaitTermination()
  }
}

