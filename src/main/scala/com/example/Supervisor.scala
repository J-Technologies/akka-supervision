package com.example

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor._
import com.example.Child.{PingMessage, SomeException}

class Supervisor extends Actor with ActorLogging {
  import Supervisor._
  import scala.concurrent.duration._

  var child: ActorRef = _

  override val supervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 3,
    withinTimeRange = 5 seconds
  ) {
    case _: SomeException => Restart
    case _: Exception     => Escalate
  }

  def receive = {
  	case Initialize => 
	    log.info("[Supervisor] Initializing")
      child = context.actorOf(Child.props(self), "Child")
  	  child ! PingMessage("ping")
    case PongMessage(text: String) =>
      log.info(s"[Supervisor] Received $text")
      child ! PingMessage("ping")
  }
}

object Supervisor {
  val props = Props[Supervisor]
  case object Initialize
  case class PongMessage(text: String)
}