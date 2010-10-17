package net.matlik.load

import com.ning.http.client._
import se.scalablesolutions.akka.actor.Actor
import se.scalablesolutions.akka.camel.{Message, Consumer}

object Loader {
  def main(count: Int):Unit = {
    println("Running!")
    1 to count foreach ( x => send(x.toString) )
    println("Done!")
  }

  case class Sent(id:String)
  case class Done(id:String)
  class StatusMonitor extends Actor {
    var counter = 0
    var set = scala.collection.mutable.HashSet.empty[String]
    def receive = {
      case Sent(id) => { 
        if(!set.add(id))  error("Tried to add pre-existing ID to set: %s" format id)
        log.info("Sent: %s" format id)
      }
      case Done(id) => {
        if(!set.remove(id)) error("Tried to remove non-existent ID from set: %s" format id)
        counter = counter + 1
        log.info("%d done: %s".format(counter, id))
        if (set.size == 0) self.stop
      }
    }
  }
  val statusMonitor = Actor.actorOf[StatusMonitor].start
  val asyncHttpClient = new AsyncHttpClient()

  def send(id: String) {
    statusMonitor ! Sent(id)
    //java.lang.Thread.sleep(1)
    Actor.spawn {
    //statusMonitor ! Done(scala.io.Source.fromURL("http://localhost:8877/?id="+id).mkString)
    asyncHttpClient.prepareGet("http://localhost:8877/?id="+id).execute(new AsyncCompletionHandler[Unit](){
      override def onCompleted(response: Response): Unit = {
        val result = response.getResponseBody
        //println("Expecting '%s' and got '%s'" format (id, result))
        assert(id == result)
        statusMonitor ! Done(result)
      }
      override def onThrowable(t: Throwable) {
        println(t)
      }
    })
    }
  }
}
