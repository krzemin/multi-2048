import scala.util.Random

trait Board {

  type Field = Option[Int]
  type Board = List[List[Field]]

  val rand = new Random()

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

  type ReduceCondition = (Int, Int) => Boolean
  type ReduceOperation = (Int, Int) => Int

  trait Transformation {
    val cond: ReduceCondition
    val op: ReduceOperation
  }

  def fix[T](f:(T=>T)=>(T=>T)):T=>T = f((x:T) => fix(f)(x))

  def applyLeft(t: Transformation)(f: List[Field] => List[Field])(row: List[Field]): List[Field] = row match {
    case Some(h1) :: Some(h2) :: rest if t.cond(h1,h2) => f(Some(t.op(h1,h2)) :: rest)
    case Some(h1) :: rest => Some(h1) :: f(rest)
    case Nil => Nil
  }

  def transformLeft(t: Transformation)(first: List[Field]): List[Field] = {
    val rem = fix(applyLeft(t))(first.filter(_.isDefined))
    rem ++ List.fill(first.size - rem.size)(None)
  }

  def transformRight(t: Transformation)(first: List[Field]): List[Field] = {
    val rem = fix(applyLeft(t))(first.filter(_.isDefined).reverse)
    List.fill(first.size - rem.size)(None) ++ rem.reverse
  }
}
