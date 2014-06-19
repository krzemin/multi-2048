package game2048

trait Transformation {
  type ReduceCondition = (Int, Int) => Boolean
  type ReduceOperation = (Int, Int) => Int

  def cond: ReduceCondition
  def op: ReduceOperation
}