
trait Transformation2048 extends Transformation {
  def cond: ReduceCondition = (x1: Int, x2: Int) => x1 == x2
  def op: ReduceOperation = (x1: Int, x2: Int) => x1 + x2
}