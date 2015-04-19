name := """sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "play-autosource" %% "reactivemongo" % "2.1-SNAPSHOT",
  "play-autosource" %% "slick" % "2.1-SNAPSHOT"
)
