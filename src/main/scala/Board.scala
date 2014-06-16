import scala.util.Random

trait Board { self: Transformation with RandomGen =>

  type Field = Option[Int]
  type Board = List[List[Field]]

  import Board.Move._

  def newBoard(size: Int, field: Field = None) = List.fill(size)(List.fill(size)(field))

  def isEmpty(x: Int, y: Int)(board: Board) = !board(y)(x).isDefined

  def putBoard(x: Int, y: Int, what: Int)(board: Board) =
    board.updated(y, board(y).updated(x, Some(what)))

  def freeFields(board: Board): List[(Int,Int)] = (for {
      x <- 0 until board.size
      y <- 0 until board.size
      if isEmpty(x,y)(board)
    } yield (x,y)).toList

  def addRandomField(board: Board): Board = {
    freeFields(board) match {
      case Nil => board
      case available =>
        val (addX, addY) = available(rand.nextInt(available.size))
        val randVal = (rand.nextInt(1) + 1) * 2
        putBoard(addX, addY, randVal)(board)
    }
  }

  def scoreBoard(board: Board): Int =
    board.flatten.map(_.getOrElse(0)).sum

  def applyTransform(row: List[Field]): List[Field] = row match {
    case Some(h1) :: Some(h2) :: rest if cond(h1,h2) => applyTransform(Some(op(h1,h2)) :: rest)
    case Some(h1) :: rest => Some(h1) :: applyTransform(rest)
    case Nil => Nil
  }

  def transformLeft(first: List[Field]): List[Field] = {
    val rem = applyTransform(first.filter(_.isDefined))
    rem ++ List.fill(first.size - rem.size)(None)
  }

  def transformRight(first: List[Field]): List[Field] = {
    val rem = applyTransform(first.filter(_.isDefined).reverse)
    List.fill(first.size - rem.size)(None) ++ rem.reverse
  }

  def performMove(move: Move, board: Board): Option[Board] = {
    val movedBoard = move match {
      case Left => board.map(transformLeft)
      case Up => board.transpose.map(transformLeft).transpose
      case Right => board.map(transformRight)
      case Down => board.transpose.map(transformRight).transpose
    }
    if (movedBoard == board) None else Some(movedBoard)
  }

  def isBoardStuck(board: Board): Boolean = Board.Move.values
      .map(move => performMove(move, board))
      .forall(_ == None)

  def showBoard(board: Board): String =
    board.map(_.map {
      case Some(n) => f"$n% 5d"
      case None => " ____"
  }.mkString).mkString("\n")

}

object Board {
  object Move extends Enumeration {
    type Move = Value
    val Left,Up,Right,Down = Value
  }
}