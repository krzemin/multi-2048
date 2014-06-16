import org.specs2.mutable._

class GameSpec extends Specification {

  import Game._

  "emptyBoard" should {
    val board = emptyBoard(5)
    "create board with specified size" in {
      board.size === 5
      board.forall(_.size === 5)
    }
    "create board filled with empty fields" in {
      board.forall(_.forall(_ === None))
    }
  }

  "isEmpty" should {
    val board = emptyBoard(5)
    "be true on empty board" in {
      (for {
        x <- 0 until 5
        y <- 0 until 5
      } yield (x,y)).forall {
        case (x,y) => isEmpty(x, y)(board) === true
      }
    }
    "be false on occupied fields" in {
      val board1 = putBoard(3, 4, 1)(board)
      isEmpty(3, 4)(board1) === false
    }
  }

}
