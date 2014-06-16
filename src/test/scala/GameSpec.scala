import org.specs2.mutable._

class GameSpec extends Specification {

  import Game._

  "emptyBoard" should {
    val board = emptyBoard(5)
    "create board with specified size" in {
      board.size must beEqualTo(5)
      board.head.size must beEqualTo(5)
    }
    "create board filled with empty fields" in {
      board.forall(_.forall(_ == None)) must beTrue
    }
  }

}
