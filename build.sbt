ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "vigil-challenge",
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.16" % "test",
      "org.scalatest" %% "scalatest" % "3.2.16" % "test"
    )
  )
