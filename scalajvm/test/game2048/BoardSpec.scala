package game2048

import org.specs2.mutable._

class BoardSpec extends Specification {

  object RandomGenFake extends RandomGenFake
  object T2048 extends Transformation2048
  import Board._
  import Move._

  val boardEmpty = Board(5)
  val boardFilled = Board(5, Some(1))


  "newBoard" should {
    "create board with specified size" in {
      boardEmpty.fields.size === 5
      boardEmpty.fields.forall(_.size === 5)
    }
    "create board filled with empty fields" in {
      boardEmpty.fields.forall(_.forall(_ === None))
    }
    "create board filled with specified fields" in {
      boardFilled.fields.forall(_.forall(_ === Some(1)))
    }
  }

  "isEmpty" should {
    "be true on empty board" in {
      (for {
        x <- 0 until 5
        y <- 0 until 5
      } yield (x,y)).forall {
        case (x,y) => boardEmpty.isEmpty(x, y) === true
      }
    }
    "be false on occupied fields" in {
      (for {
        x <- 0 until 5
        y <- 0 until 5
      } yield (x,y)).forall {
        case (x,y) => boardFilled.isEmpty(x, y) === false
      }
    }
  }

  "putBoard" should {
    "replace empty field" in {
      val board1 = boardEmpty.put(1, 2, 8)
      board1.fields(2)(1) === Some(8)
    }
    "replace occupied field" in {
      val board1 = boardFilled.put(1, 2, 8)
      board1.fields(2)(1) === Some(8)
    }
  }

  "freeFields" should {
    "return all coords at empty board" in {
      boardEmpty.freeFields.toSet.size === 5 * 5
    }
    "return empty list at filled board" in {
      boardFilled.freeFields.toSet.size === 0
    }
  }

  "addRandomField" should {
    "add one random field at empty board" in {
      val boardAdded = boardEmpty.addRandomField(RandomGenFake.rand)
      boardAdded.freeFields.toSet.size === 5 * 5 - 1
    }
    "add nothing at filled board" in {
      boardFilled.addRandomField(RandomGenFake.rand) === boardFilled
    }
  }

  "scoreBoard" should {
    "score empty board to 0" in {
      boardEmpty.score === 0
    }
    "score filled board to 25" in {
      boardFilled.score === 25
    }
  }

  "transformLeft" should {
    "leave empty row an empty row" in {
      val emptyRow: List[Option[Int]] = List(None, None, None)
      transformLeft(T2048)(emptyRow) === emptyRow
    }
    "add two numbers at the edge" in {
      transformLeft(T2048)(List(Some(4), Some(4), None)) === List(Some(8), None, None)
    }
    "add two numbers at the opposite edge" in {
      transformLeft(T2048)(List(None, Some(8), Some(8))) === List(Some(16), None, None)
    }
    "transform from left to right" in {
      transformLeft(T2048)(List(Some(4), Some(2), Some(2))) === List(Some(4), Some(4), None)
      transformLeft(T2048)(List(Some(2), Some(2), Some(4))) === List(Some(8), None, None)
    }
    "gravitate to the left" in {
      transformLeft(T2048)(List(None, Some(4), None, Some(2), Some(1))) === List(Some(4), Some(2), Some(1), None, None)
    }
  }

  "transformRight" should {
    "leave empty row an empty row" in {
      val emptyRow: List[Option[Int]] = List(None, None, None)
      transformRight(T2048)(emptyRow) === emptyRow
    }
    "add two numbers at the edge" in {
      transformRight(T2048)(List(None, Some(8), Some(8))) === List(None, None, Some(16))
    }
    "add two numbers at the opposite edge" in {
      transformRight(T2048)(List(Some(4), Some(4), None)) === List(None, None, Some(8))
    }
    "transform from right to left" in {
      transformRight(T2048)(List(Some(4), Some(2), Some(2))) === List(None, None, Some(8))
      transformRight(T2048)(List(Some(2), Some(2), Some(4))) === List(None, Some(4), Some(4))
    }
    "gravitate to the right" in {
      transformRight(T2048)(List(None, Some(4), None, Some(2), Some(1))) === List(None, None, Some(4), Some(2), Some(1))
    }
  }

  "performMove" should {
    val boardGood: Board = new Board(3, List(
      List(Some(2), None,    Some(2)),
      List(None,    Some(4), None),
      List(None,    Some(4), None)
    ))
    val boardBad: Board = new Board(3, List(
      List(Some(1), Some(2), Some(4)),
      List(Some(2), Some(4), Some(8)),
      List(Some(4), Some(8), Some(16))
    ))

    "move left" in {
      boardGood.performMove(T2048)(Left) === Some(new Board(3, List(
        List(Some(4), None, None),
        List(Some(4), None, None),
        List(Some(4), None, None)
      )))
    }
    "stuck left" in {
      boardBad.performMove(T2048)(Left) === None
    }

    "move up" in {
      boardGood.performMove(T2048)(Up) === Some(new Board(3, List(
        List(Some(2), Some(8), Some(2)),
        List(None,    None,    None),
        List(None,    None,    None)
      )))
    }
    "stuck up" in {
      boardBad.performMove(T2048)(Up) === None
    }

    "move right" in {
      boardGood.performMove(T2048)(Right) === Some(new Board(3, List(
        List(None, None, Some(4)),
        List(None, None, Some(4)),
        List(None, None, Some(4))
      )))
    }
    "stuck right" in {
      boardBad.performMove(T2048)(Right) === None
    }

    "move down" in {
      boardGood.performMove(T2048)(Down) === Some(new Board(3, List(
        List(None,    None,    None),
        List(None,    None,    None),
        List(Some(2), Some(8), Some(2))
      )))
    }
    "stuck down" in {
      boardBad.performMove(T2048)(Down) === None
    }
  }

  "isBoardStuck" should {
    "return false on in-game boards" in {
      boardFilled.isStuck(T2048) === false
    }
    "return true on stuck boards" in {
      boardEmpty.isStuck(T2048) === true
    }
  }

  "toString" should {
    "show empty board" in {
      boardEmpty.toString ===
        """ ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____
          | ____ ____ ____ ____ ____""".stripMargin
    }
    "show filled board" in {
      boardFilled.toString ===
        """    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1
          |    1    1    1    1    1""".stripMargin
    }
  }

}
