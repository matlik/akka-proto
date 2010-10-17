package net.matlik.server

import se.scalablesolutions.akka.actor.{Actor, ActorRef}
import se.scalablesolutions.akka.camel.{Message, Consumer}

class BOOT {
  val starter = Actor.actorOf[Starter].start
  Actor.actorOf(new TestServer(starter)).start
}

case class GO(ref: ActorRef)

class TestServer(starter:ActorRef) extends Actor with Consumer {
  def endpointUri = "jetty:http://localhost:8877/?matchOnUriPrefix=true"
  def receive = {
    case msg: Message =>  Actor.actorOf(new Worker(starter)).start.forward(msg)
  }
}

class Worker(starter: ActorRef) extends Actor {
  var go = false
  var message: Message = null

  def receive = {
    case msg: Message if "GET" equals msg.headers("CamelHttpMethod") => {
      message = Message(msg.headers("id"), Map("content-type"->"text/plain"))
      starter forward  self
      //self.reply(message); self.stop
    }
    case GO(ref) => { ref.reply(message); self.stop }
    //case msg: Message if "GET" equals msg.headers("CamelHttpMethod") => self.reply("Responding to Get\n %s" format msg.toString)
    case msg: Message => self.reply("Unexpected HTTP Method: %s" format msg.headers("CamelHttpMethod"))
    //case msg: Message => self.reply("Hello %s" format msg.bodyAs[String])
  }
}

class Starter extends Actor with Consumer {
  def endpointUri = "jetty:http://localhost:8878/start"
  var started = false
  var list: List[ActorRef] = Nil
  def receive = {
    case actor:ActorRef => {
      if (started) actor ! GO
      else list = actor :: list
    }
    case msg: Message => {
      started = true 
      //Actor.spawn {
        list.foreach(a => a ! GO(a)) 
        list = Nil
      //}
      self.reply(Message("Started", Map("content-type"->"text/plain")))
    }
  }
}

