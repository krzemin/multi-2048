class Game(size: Int) { self: Board =>

  var board1 = addRandomField(newBoard(size))
  var board2 = addRandomField(newBoard(size))

  import Game.Status._
  import Board.Move._

  def move(move: Move): Boolean = {
    val nextBoards = for {
      m1 <- performMove(move, board1)
      m2 <- performMove(move, board2)
    } yield (m1, m2)

    nextBoards.fold(false)({ case (b1, b2) =>
      board1 = b1
      board2 = b2
      true
    })
  }

  def status: Status = (isBoardStuck(board1), isBoardStuck(board2)) match {
    case (false, false) => InProgress
    case (false, true) => Player1Won
    case (true, false) => Player2Won
    case (true, true) => Draw
  }
  override def toString() = showBoard(board1) ++ "--------\n" ++ showBoard(board2)
}

object Game {

  object Status extends Enumeration {
    type Status = Value
    val InProgress, Draw, Player1Won, Player2Won = Value
  }

}