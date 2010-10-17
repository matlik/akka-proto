import sbt._

class ServerProject (info: ProjectInfo) extends DefaultProject(info) with AkkaProject with sbt_akka_bivy.AkkaKernelDeployment {
  override val akkaVersion = "0.10"
  //val akkaAMQP = akkaModule("amqp")
  val akkaCamel = akkaModule("camel")
  //val akkaHttp = akkaModule("http")
  //val akkaJTA = akkaModule("jta")
  val akkaKernel = akkaModule("kernel")
  //val akkaCassandra = akkaModule("persistence-cassandra")
  //val akkaMongo = akkaModule("persistence-mongo")
  //val akkaRedis = akkaModule("persistence-redis")
  //val akkaSpring = akkaModule("spring")

  lazy val async_http_clint = "com.ning" % "async-http-client" % "1.2.0"

  //val openid4jVersion = "0.9.5"
  //lazy val openid4java_consumer = "org.openid4java" % "openid4java-consumer" % openid4jVersion
  //lazy val openid4java_server = "org.openid4java" % "openid4java-server" % openid4jVersion
  //lazy val openid4java_infocard = "org.openid4java" % "openid4java-infocard" % openid4jVersion
  //lazy val openid4java-xri = "org.openid4java" % "openid4java-xri" % openid4jVersion
  //lazy val openid4java = "org.openid4java" % "openid4java" % openid4jVersion

  lazy val camel_jetty = "org.apache.camel" % "camel-jetty" % "2.4.0.1"
  //lazy val camel_spring = "org.apache.camel" % "camel-spring" % "2.4.0"
}
