name := "scalajs-demo"

version := "0.1"

scalaVersion := "2.13.5"

enablePlugins(ScalaJSPlugin)
enablePlugins(ScalablyTypedConverterPlugin)
enablePlugins(ScalaJSBundlerPlugin)


scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.9.4"

Compile / npmDependencies ++= Seq(
  "kosher-zmanim" -> "0.8.0",
  "@types/luxon" -> "1.26.0"
)
