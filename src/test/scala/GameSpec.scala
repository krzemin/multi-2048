import org.specs2.mutable._

class GameSpec extends Specification {

  import Game._

  val boardEmpty = newBoard(5)
  val boardFilled = newBoard(5, Some(1))

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

}
