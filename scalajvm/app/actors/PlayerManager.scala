package actors

import akka.actor.{Props, ActorRef, Actor}
import game2048._


object PlayerManager {
  def props(out: ActorRef) = Props(new PlayerManager(out))
}

class PlayerManager(out: ActorRef) extends Actor {

  var game: Game = _

  def receive: Receive = {
    case WantPlayAI =>
      game = new Game(4) with Transformation2048 with RandomGen with GameRenderer
      out ! NewGame(4, game.board1, game.board2, true)
    case PerformMove(move) =>
      game.move(move)
      out ! StateUpdate(game.status, game.board1, game.board2)
      if(game.status == Game.InProgress) {
        Thread.sleep(1000)
        game.move(game.asInstanceOf[RandomGen].rand.shuffle(game.availableMoves).head)
        out ! StateUpdate(game.status, game.board1, game.board2)
      }
  }
}
