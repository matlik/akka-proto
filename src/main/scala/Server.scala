package net.matlik.server

import se.scalablesolutions.akka.actor.Actor
import se.scalablesolutions.akka.camel.{Message, Consumer}

class BOOT {
  Actor.actorOf[TestServer].start
}

class TestServer extends Actor with Consumer {
  def endpointUri = "jetty:http://localhost:8877/?matchOnUriPrefix=true"
  def receive = {
    case msg: Message if "GET" equals msg.headers("CamelHttpMethod") => self.reply(Message(msg.headers("id"), Map("content-type"->"text/plain")))
    //case msg: Message if "GET" equals msg.headers("CamelHttpMethod") => self.reply("Responding to Get\n %s" format msg.toString)
    case msg: Message => self.reply("Unexpected HTTP Method: %s" format msg.headers("CamelHttpMethod"))
    //case msg: Message => self.reply("Hello %s" format msg.bodyAs[String])
  }
}


