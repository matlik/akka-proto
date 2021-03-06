import sbt._
 
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "0.10"

  val bumRepo = "Bum Networks Release Repository" at "http://repo.bumnetworks.com/releases"
  val sbtAkkaBivy = "net.evilmonkeylabs" % "sbt-akka-bivy" % "0.2.0"
}
