name := "VOTD"

version := "1.0"

lazy val `votd` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test,
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4" ,
    "com.typesafe.play" %% "play-slick" % "1.1.1" )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  