name := "VOTD"

version := "1.0"

lazy val `votd` = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers ++= Seq ("scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/" )

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test,
    "org.postgresql" % "postgresql" % "9.4-1205-jdbc42",
    "com.typesafe.play" %% "play-slick" % "1.1.1",
    "com.adrianhurt" %% "play-bootstrap" % "1.0-P24-B3-SNAPSHOT",
    "org.webjars" % "font-awesome" % "4.5.0"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )