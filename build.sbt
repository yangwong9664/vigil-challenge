ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "vigil-challenge",
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.16" % "test",
      "org.scalatest" %% "scalatest" % "3.2.16" % "test",
      "org.typelevel" %% "cats-effect" % "3.5.1",
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test,
      "com.disneystreaming" %% "weaver-cats" % "0.8.3" % Test,
      "co.fs2" %% "fs2-core" % "3.8.0",
      "co.fs2" %% "fs2-io" % "3.8.0",
      "co.fs2" %% "fs2-reactive-streams" % "3.8.0",
      "co.fs2" %% "fs2-scodec" % "3.8.0"
    )
  )
testFrameworks += new TestFramework("weaver.framework.CatsEffect")
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps"
)

