package game2048

class Game(size: Int) { self: Transformation with RandomGen with GameRenderer =>

  import Game.Status._
  import Board.Move._

  require(size > 1)

  var board1: Board = Board(size).addRandomField(rand)
  var board2: Board = Board(size).addRandomField(rand)

  while (status != InProgress) {
    board1 = Board(size).addRandomField(rand)
    board2 = Board(size).addRandomField(rand)
  }

  def move(move: Move): Boolean = {
    val nextBoards = for {
      m1 <- board1.performMove(self)(move)
      m2 <- board2.performMove(self)(move)
    } yield (m1, m2)

    nextBoards.fold(false)({ case (b1, b2) =>
      board1 = b1.addRandomField(rand)
      board2 = b2.addRandomField(rand)
      true
    })
  }

  def availableMoves: List[Move] =
    Board.Move.values.toList
      .map(move => (move, board1.performMove(self)(move), board2.performMove(self)(move)))
      .filter(p => p._2.isDefined && p._3.isDefined)
      .map(_._1)

  def status: Status = (board1.isStuck(self), board2.isStuck(self)) match {
    case (false, false) =>
      availableMoves.isEmpty match {
        case true => Draw
        case false => InProgress
      }
    case (false, true) => Player1Won
    case (true, false) => Player2Won
    case (true, true) => Draw
  }

  def score1: Int = board1.score
  def score2: Int = board2.score

  def render() = renderGame(self)

  override def toString() =
    s"P1: ${board1.score}\n$board1\nP2: ${board2.score}\n$board2\nStatus: $status\n"
}

object Game {

  object Status extends Enumeration {
    type Status = Value
    val InProgress, Draw, Player1Won, Player2Won = Value
  }

}