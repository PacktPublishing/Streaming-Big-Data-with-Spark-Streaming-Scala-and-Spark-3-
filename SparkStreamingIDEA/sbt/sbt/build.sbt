name := "wordcount"

version := "1.0"

organization := "com.sundogsoftware"

scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
"org.apache.spark" %% "spark-core" % "3.1.2" % "provided"
)
