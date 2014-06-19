package game2048

import org.specs2.mutable._

class BoardSpec extends Specification {

  object Test extends Board with Transformation2048 with RandomGenFake
  import Test._
  import Board._

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

  "scoreBoard" should {
    "score empty board to 0" in {
      scoreBoard(boardEmpty) === 0
    }
    "score filled board to 25" in {
      scoreBoard(boardFilled) === 25
    }
  }

  "transformLeft" should {
    "leave empty row an empty row" in {
      val emptyRow: List[Field] = List(None, None, None)
      transformLeft(emptyRow) === emptyRow
    }
    "add two numbers at the edge" in {
      transformLeft(List(Some(4), Some(4), None)) === List(Some(8), None, None)
    }
    "add two numbers at the opposite edge" in {
      transformLeft(List(None, Some(8), Some(8))) === List(Some(16), None, None)
    }
    "transform from left to right" in {
      transformLeft(List(Some(4), Some(2), Some(2))) === List(Some(4), Some(4), None)
      transformLeft(List(Some(2), Some(2), Some(4))) === List(Some(8), None, None)
    }
    "gravitate to the left" in {
      transformLeft(List(None, Some(4), None, Some(2), Some(1))) === List(Some(4), Some(2), Some(1), None, None)
    }
  }

  "transformRight" should {
    "leave empty row an empty row" in {
      val emptyRow: List[Field] = List(None, None, None)
      transformRight(emptyRow) === emptyRow
    }
    "add two numbers at the edge" in {
      transformRight(List(None, Some(8), Some(8))) === List(None, None, Some(16))
    }
    "add two numbers at the opposite edge" in {
      transformRight(List(Some(4), Some(4), None)) === List(None, None, Some(8))
    }
    "transform from right to left" in {
      transformRight(List(Some(4), Some(2), Some(2))) === List(None, None, Some(8))
      transformRight(List(Some(2), Some(2), Some(4))) === List(None, Some(4), Some(4))
    }
    "gravitate to the right" in {
      transformRight(List(None, Some(4), None, Some(2), Some(1))) === List(None, None, Some(4), Some(2), Some(1))
    }
  }

  "performMove" should {
    val boardGood: Board = List(
      List(Some(2), None,    Some(2)),
      List(None,    Some(4), None),
      List(None,    Some(4), None)
    )
    val boardBad: Board = List(
      List(Some(1), Some(2), Some(4)),
      List(Some(2), Some(4), Some(8)),
      List(Some(4), Some(8), Some(16))
    )

    "move left" in {
      performMove(Move.Left, boardGood) === Some(List(
        List(Some(4), None, None),
        List(Some(4), None, None),
        List(Some(4), None, None)
      ))
    }
    "stuck left" in {
      performMove(Move.Left, boardBad) === None
    }

    "move up" in {
      performMove(Move.Up, boardGood) === Some(List(
        List(Some(2), Some(8), Some(2)),
        List(None,    None,    None),
        List(None,    None,    None)
      ))
    }
    "stuck up" in {
      performMove(Move.Up, boardBad) === None
    }

    "move right" in {
      performMove(Move.Right, boardGood) === Some(List(
        List(None, None, Some(4)),
        List(None, None, Some(4)),
        List(None, None, Some(4))
      ))
    }
    "stuck right" in {
      performMove(Move.Right, boardBad) === None
    }

    "move down" in {
      performMove(Move.Down, boardGood) === Some(List(
        List(None,    None,    None),
        List(None,    None,    None),
        List(Some(2), Some(8), Some(2))
      ))
    }
    "stuck down" in {
      performMove(Move.Down, boardBad) === None
    }
  }

  "isBoardStuck" should {
    "return false on in-game boards" in {
      isBoardStuck(boardFilled) === false
    }
    "return true on stuck boards" in {
      isBoardStuck(boardEmpty) === true
    }
  }

  "showBoard" should {
    "show empty board" in {
      showBoard(boardEmpty) ===
        """ ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____""".stripMargin
    }
    "show filled board" in {
      showBoard(boardFilled) ===
        """    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1""".stripMargin
    }
  }

}
