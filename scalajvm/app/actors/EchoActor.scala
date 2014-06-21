package actors

import akka.actor.{Props, ActorRef, Actor}


object EchoActor {
  def props(out: ActorRef) = Props(new EchoActor(out))
}

class EchoActor(out: ActorRef) extends Actor {



  def receive: Receive = {
    case TestValue(v) => out ! TestValue(v)
  }
}
