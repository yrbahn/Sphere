name := "Sphere"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.0",
  "io.spray" % "spray-can" % "1.3.1",
  "io.spray" % "spray-routing" % "1.3.1",
  "io.spray" % "spray-io" % "1.3.1",
  "io.spray" % "spray-caching" % "1.3.1",
  "io.spray" %%  "spray-json" % "1.2.5",
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.2",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.2"
)


resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "rediscala" at "https://github.com/etaty/rediscala-mvn/raw/master/releases/"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

scalacOptions  := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
