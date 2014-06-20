package game2048

import scala.util.Random

case class Board(size: Int, fields: List[List[Option[Int]]]) {
  import Board._
  import Move._

  def isEmpty(x: Int, y: Int) = !fields(y)(x).isDefined

  def put(x: Int, y: Int, what: Int) =
    new Board(size, fields.updated(y, fields(y).updated(x, Some(what))))

  def freeFields: List[(Int, Int)] = (for {
    x <- 0 until size
    y <- 0 until size
    if isEmpty(x, y)
  } yield (x, y)).toList

  def addRandomField(rand: Random): Board = freeFields match {
    case Nil => this
    case available =>
      val (addX, addY) = available(rand.nextInt(available.size))
      val randVal = (rand.nextInt(1) + 1) * 2
      put(addX, addY, randVal)
  }

  def score: Int = fields.flatten.map(_.getOrElse(0)).sum


  def performMove(t: Transformation)(move: Move): Option[Board] = {
    val newFields = move match {
      case Left => fields.map(transformLeft(t))
      case Up => fields.transpose.map(transformLeft(t)).transpose
      case Right => fields.map(transformRight(t))
      case Down => fields.transpose.map(transformRight(t)).transpose
    }
    if (newFields == fields) None else Some(new Board(size, newFields))
  }

  def isStuck(t: Transformation): Boolean = Board.Move.values
    .map(move => performMove(t)(move))
    .forall(_ == None)

  override def toString: String =
    fields.map(_.map {
      case Some(n) => f"$n% 5d"
      case None => " ____"
    }.mkString).mkString("\n")
}

object Board {
  def apply(size: Int, field: Option[Int] = None): Board =
    new Board(size, List.fill(size)(List.fill(size)(field)))

  def applyTransform(t: Transformation)(row: List[Option[Int]]): List[Option[Int]] = row match {
    case Some(h1) :: Some(h2) :: rest if t.cond(h1, h2) => applyTransform(t)(Some(t.op(h1, h2)) :: rest)
    case Some(h1) :: rest => Some(h1) :: applyTransform(t)(rest)
    case Nil => Nil
  }

  def transformLeft(t: Transformation)(first: List[Option[Int]]): List[Option[Int]] = {
    val rem = applyTransform(t)(first.filter(_.isDefined))
    rem ++ List.fill(first.size - rem.size)(None)
  }

  def transformRight(t: Transformation)(first: List[Option[Int]]): List[Option[Int]] = {
    val rem = applyTransform(t)(first.filter(_.isDefined).reverse)
    List.fill(first.size - rem.size)(None) ++ rem.reverse
  }

  object Move extends Enumeration {
    type Move = Value
    val Left, Up, Right, Down = Value
  }
}