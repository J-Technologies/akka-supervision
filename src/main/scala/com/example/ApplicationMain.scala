package com.example

import akka.actor.ActorSystem

import scala.concurrent.duration._

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val supervisor = system.actorOf(Supervisor.props, "supervisor")

  supervisor ! Supervisor.Initialize

  try {
    system.awaitTermination(3 seconds)
  } catch {
    case e: Exception => system.shutdown()
  }
}