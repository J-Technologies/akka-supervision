package com.example

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.example.Child.PingMessage
import com.example.Supervisor.PongMessage
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
 
class SupervisionSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  def this() = this(ActorSystem("MySpec"))
 
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  /**
   * This test could fail if the child actor fails three times in a row (probability 0.001)
   * However, it is meant to demonstrate the recovery of the child actor by its supervisor
   */
  "A child actor" must {
    "send back a pong on a ping" in {
      val supervisor = TestProbe()
      val child = system.actorOf(Child.props(supervisor.ref), "Child")

      supervisor.send(child, PingMessage("ping"))
      supervisor.expectMsg(PongMessage("pong"))
    }
  }

}
