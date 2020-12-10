name := "BeatTheMarket"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += ("spray" at "http://repo.spray.io/").withAllowInsecureProtocol(true)

libraryDependencies ++= Seq("org.apache.httpcomponents" % "httpclient" % "4.5.2",
                            "org.json4s" % "json4s-jackson_2.13" % "3.7.0-M7")

