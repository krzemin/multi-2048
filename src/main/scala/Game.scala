
object Game {

  type Field = Option[Int]
  type Board = List[List[Field]]

  def emptyBoard(size: Int) = List.fill(size)(List.fill(size)(None))
  def isEmpty(x: Int, y: Int)(board: Board) = !board(y)(x).isDefined
  def putBoard(x: Int, y: Int, what: Int)(board: Board) =
    board.updated(y, board(y).updated(x, Some(what)))

  type ReduceCondition = (Int, Int) => Boolean
  type ReduceOperation = (Int, Int) => Int

  def fix[T](f:(T=>T)=>(T=>T)):T=>T = f((x:T) => fix(f)(x))

  def applyLeft(cond: ReduceCondition, op: ReduceOperation)(f: List[Field] => List[Field])(row: List[Field]): List[Field] = row match {
    case Some(h1) :: Some(h2) :: rest if cond(h1,h2) => f(Some(op(h1,h2)) :: rest)
    case Some(h1) :: rest => Some(h1) :: f(rest)
    case Nil => Nil
  }

  def eqOp: ReduceCondition = (x1: Int, x2: Int) => x1 == x2
  def addOp: ReduceOperation = (x1: Int, x2: Int) => x1 + x2

  def transformLeft(first: List[Field]): List[Field] = {
    val rem = fix(applyLeft(eqOp, addOp))(first.filter(_.isDefined))
    rem ++ List.fill(first.size - rem.size)(None)
  }

  def transformRight(first: List[Field]): List[Field] = {
    val rem = fix(applyLeft(eqOp, addOp))(first.filter(_.isDefined).reverse)
    List.fill(first.size - rem.size)(None) ++ rem.reverse
  }

  println(transformLeft(List(Some(2), Some(2), Some(4), None)))
  println(transformLeft(List(Some(4), Some(2), Some(2), None)))

  println(transformRight(List(Some(2), Some(2), Some(4), None)))
  println(transformRight(List(Some(4), Some(2), Some(2), None)))




}
