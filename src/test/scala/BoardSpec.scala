import org.specs2.mutable._

class BoardSpec extends Specification {

  object Test extends Board
  import Test._

  val boardEmpty = newBoard(5)
  val boardFilled = newBoard(5, Some(1))

  object Transformation2048 extends Transformation {
    def cond: ReduceCondition = (x1: Int, x2: Int) => x1 == x2
    def op: ReduceOperation = (x1: Int, x2: Int) => x1 + x2
  }

  "newBoard" should {
    "create board with specified size" in {
      boardEmpty.size === 5
      boardEmpty.forall(_.size === 5)
    }
    "create board filled with empty fields" in {
      boardEmpty.forall(_.forall(_ === None))
    }
    "create board filled with specified fields" in {
      boardFilled.forall(_.forall(_ === Some(1)))
    }
  }

  "isEmpty" should {
    "be true on empty board" in {
      (for {
        x <- 0 until 5
        y <- 0 until 5
      } yield (x,y)).forall {
        case (x,y) => isEmpty(x, y)(boardEmpty) === true
      }
    }
    "be false on occupied fields" in {
      (for {
        x <- 0 until 5
        y <- 0 until 5
      } yield (x,y)).forall {
        case (x,y) => isEmpty(x, y)(boardFilled) === false
      }
    }
  }

  "putBoard" should {
    "replace empty field" in {
      val board1 = putBoard(1, 2, 8)(boardEmpty)
      board1(2)(1) === Some(8)
    }
    "replace occupied field" in {
      val board1 = putBoard(1, 2, 8)(boardFilled)
      board1(2)(1) === Some(8)
    }
  }

  "freeFields" should {
    "return all coords at empty board" in {
      freeFields(boardEmpty).toSet.size === 5 * 5
    }
    "return empty list at filled board" in {
      freeFields(boardFilled).toSet.size === 0
    }
  }

  "addRandomField" should {
    "add one random field at empty board" in {
      val boardAdded = addRandomField(boardEmpty)
      freeFields(boardAdded).toSet.size === 5 * 5 - 1
    }
    "add nothing at filled board" in {
      addRandomField(boardFilled) === boardFilled
    }
  }

  "transformLeft" should {
    val tr = transformLeft(Transformation2048)
    "leave empty row an empty row" in {
      val emptyRow: List[Field] = List(None, None, None)
      tr(emptyRow) === emptyRow
    }
    "add two numbers at the edge" in {
      tr(List(Some(4), Some(4), None)) === List(Some(8), None, None)
    }
    "add two numbers at the opposite edge" in {
      tr(List(None, Some(8), Some(8))) === List(Some(16), None, None)
    }
    "transform from left to right" in {
      tr(List(Some(4), Some(2), Some(2))) === List(Some(4), Some(4), None)
    }
    "gravitate to the left" in {
      tr(List(None, Some(1), None, Some(2), Some(1))) === List(Some(1), Some(2), Some(1), None, None)
    }
  }



}
