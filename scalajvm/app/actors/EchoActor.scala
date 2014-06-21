package actors

import akka.actor.{Props, ActorRef, Actor}
import game2048.Board


object EchoActor {
  def props(out: ActorRef) = Props(new EchoActor(out))
}

class EchoActor(out: ActorRef) extends Actor {

  def receive: Receive = {
    case TestValue(v) =>
      out ! TestValue(v)
      out ! NewGame(4, Board(4).put(2,2,2), Board(4).put(3,3,2), false)
  }
}
