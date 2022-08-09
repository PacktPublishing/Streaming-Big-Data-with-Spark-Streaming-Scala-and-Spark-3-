ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "SparkStreaming"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2",
  "org.apache.spark" %% "spark-mllib" % "3.1.2",
  "org.apache.spark" %% "spark-streaming" % "3.1.2",
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "org.twitter4j" % "twitter4j-stream" % "4.0.4",
  "com.twitter" % "jsr166e" % "1.1.0",
  "com.datastax.spark" % "spark-cassandra-connector_2.12" % "3.1.0",
  "org.apache.spark" %% "spark-streaming-flume" % "2.4.8",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.1.2",
  "org.apache.spark" %% "spark-streaming-kinesis-asl" % "3.1.2"
)
