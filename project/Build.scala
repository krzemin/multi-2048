import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.typesafe.sbt.packager.universal.UniversalKeys
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

object ApplicationBuild extends Build with UniversalKeys {

  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

  override def rootProject = Some(scalajvm)

  val sharedSrcDir = "scala"

  lazy val scalajvm = Project(
    id = "scalajvm",
    base = file("scalajvm")
  ) enablePlugins (play.PlayScala) settings (scalajvmSettings: _*) aggregate (scalajs)

  lazy val scalajs = Project(
    id   = "scalajs",
    base = file("scalajs")
  ) settings (scalajsSettings: _*)

  lazy val sharedScala = Project(
    id = "scala",
    base = file(sharedSrcDir)
  ) settings (sharedScalaSettings: _*)

  lazy val scalajvmSettings =
    Seq(
      name := "multi-2048",
      version := Versions.app,
      scalaVersion := Versions.scala,
      scalajsOutputDir := (crossTarget in Compile).value / "classes" / "public" / "javascripts",
      compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (scalajs, Compile)),
      dist <<= dist dependsOn (fullOptJS in (scalajs, Compile)),
      addSharedSrcSetting,
      libraryDependencies ++= Dependencies.scalajvm,
      resolvers += Resolver.url("scala-js-releases",
        url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
          Resolver.ivyStylePatterns),
      EclipseKeys.skipParents in ThisBuild := false
    ) ++ (
      // ask scalajs project to put its outputs in scalajsOutputDir
      Seq(packageExternalDepsJS, packageInternalDepsJS, packageExportedProductsJS, packageLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
        crossTarget in (scalajs, Compile, packageJSKey) := scalajsOutputDir.value
      }
    )

  lazy val scalajsSettings =
    scalaJSSettings ++ Seq(
      name := "multi-2048-scalajs",
      version := Versions.app,
      scalaVersion := Versions.scala,
      persistLauncher := true,
      persistLauncher in Test := false,
      libraryDependencies ++= Dependencies.scalajs,
      resolvers += Resolver.url("scala-js-releases",
        url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
          Resolver.ivyStylePatterns),
      addSharedSrcSetting
    )

  lazy val sharedScalaSettings =
    Seq(
      name := "multi-2048-shared",
      scalaSource in Compile := baseDirectory.value,
      EclipseKeys.skipProject := true
    )

  lazy val addSharedSrcSetting = unmanagedSourceDirectories in Compile += new File((baseDirectory.value / ".." / sharedSrcDir).getCanonicalPath)
}

object Dependencies {
  val scalajvm = Seq(
    "org.scalajs" %% "scalajs-pickling-play-json" % "0.3.1"
  )

  val scalajs = Seq(
    "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % Versions.scalajsDom,
    "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6",
    "org.scalajs" %%% "scalajs-pickling" % "0.3.1",
    "org.scala-lang.modules.scalajs" %% "scalajs-jasmine-test-framework" % scalaJSVersion % "test",
    "com.typesafe.akka" %% "akka-actor" % "2.3.3"
  )
}

object Versions {
  val app = "0.1.0-SNAPSHOT"
  val scala = "2.11.1"
  val scalajsDom = "0.6"
}
