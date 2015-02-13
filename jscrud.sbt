name := "play2-jscrud-parent"

version := Common.version

scalaVersion := Common.scalaVersion

lazy val core =  project.in( file("module-code") ).enablePlugins(PlayScala)

lazy val scalaDemo = project.in( file("sample") ).enablePlugins(PlayScala).dependsOn(core)

lazy val root = project.in( file(".") ).aggregate(core, scalaDemo) .settings(
     aggregate in update := false
   )
