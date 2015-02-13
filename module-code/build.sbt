name := """module-code"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

lazy val njRepo = Seq(
    "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/",
    "Mandubian bintray repository releases" at "http://dl.bintray.com/mandubian/maven",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "sonarepo release" at "https://oss.sonatype.org/content/repositories/releases/")

resolvers in ThisBuild ++= njRepo

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "play-autosource" %% "reactivemongo" % "2.1-SNAPSHOT"
)

libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"

libraryDependencies += "org.coursera" %% "autoschema" % "0.1"