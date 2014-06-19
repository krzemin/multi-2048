package game2048

import org.specs2.mutable._

class GameSpec extends Specification {

  import Game.Status._
  import Board.Move._

  "Game" should {
    "claim in-progress status for new game" in {
      val game = new Game(4) with Board with Transformation2048 with RandomGenFake with GameRenderer
      game.status === InProgress
    }

    "claim new 1x1 size game to be draw" in {
      val game = new Game(1) with Board with Transformation2048 with RandomGenFake with GameRenderer
      game.status === Draw
    }

    "terminate when boards are not stuck, but there is no single move which modifies them" in {
      val game = new Game(2) with Board with Transformation2048 with RandomGenFake with GameRenderer {
        board1 = List(List(Some(2), Some(4)),
                      List(Some(8), None))
        board2 = List(List(None,    Some(4)),
                      List(Some(8), Some(16)))
      }

      game.status === Draw
    }

    "play game2048 game" in {
      val game = new Game(3) with Board with Transformation2048 with RandomGenFake with GameRenderer

      game.move(Left) === true // P1
      game.move(Down) === true // P2
      game.move(Right) === true // P1
      game.move(Right) === true // P2
      game.move(Left) === true // P1
      game.move(Up) === true // P2
      game.move(Down) === true // P1
      game.move(Right) === true // P2
      game.move(Left) === true // P1
      game.move(Right) === true // P2
      game.move(Up) === true // P1
      game.move(Down) === true // P2
      game.move(Up) === true // P1
      game.move(Right) === true // P2
      game.move(Left) === true // P1
      game.move(Right) === true // P2
      game.move(Up) === true // P1
      game.move(Down) === true // P2
      game.move(Up) === true // P1
      game.move(Down) === true // P2
      game.move(Right) === true // P1
      game.move(Left) === true // P2
      game.move(Down) === true // P1
      game.move(Right) === true // P2
      game.move(Left) === true // P1
      game.move(Right) === true // P2
      game.move(Up) === true // P1
      game.status === Player1Won
    }
  }

}
