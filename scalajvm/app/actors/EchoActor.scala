package actors

import akka.actor.{Props, ActorRef, Actor}

object EchoActor {
  def props(out: ActorRef) = Props(new EchoActor(out))
}

class EchoActor(out: ActorRef) extends Actor {
  def receive = {
    case msg => out ! s"I received: $msg"
  }
}
