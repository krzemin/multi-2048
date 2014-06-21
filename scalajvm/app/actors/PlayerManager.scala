package actors

import akka.actor.{Props, ActorRef, Actor}
import game2048.Board


object PlayerManager {
  def props(out: ActorRef) = Props(new PlayerManager(out))
}

class PlayerManager(out: ActorRef) extends Actor {

  def receive: Receive = {
    case WantPlayHuman =>
      out ! NewGame(4, Board(4).put(2,2,2), Board(4).put(3,3,2), false)
  }
}
