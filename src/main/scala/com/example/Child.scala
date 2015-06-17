package com.example

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import com.example.Supervisor.PongMessage

import scala.util.Random

class Child(supervisor: ActorRef) extends Actor with ActorLogging {
  import Child._

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info(s"[Child] Pre restart - Restarting because of exception with message: $message")
  }

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable) {
    log.info("[Child] Post restart - sending a pong")
    supervisor ! PongMessage("pong")
  }

  def receive = {
  	case PingMessage(text) =>
  	  log.info(s"[Child] Received $text")

      if (rand < 0.1) {
        log.info("[Child] Throwing some exception")
        throw new SomeException("Some exception")
      }

      log.info("[Child] Sending pong to supervisor")
      supervisor ! PongMessage("pong")
  }

  def rand = new Random().nextDouble
}

object Child {
  def props(supervisor: ActorRef): Props = Props(new Child(supervisor))
  case class PingMessage(text: String)
  class SomeException(msg: String) extends RuntimeException(msg)
}
