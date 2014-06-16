import org.specs2.mutable._

class GameSpec extends Specification {

  import Game.Status._

  "Game" should {
    "claim in-progress status for new game" in {
      val game = new Game(4) with Board with Transformation2048 with RandomGenFake
      game.status === InProgress
    }
    "claim new 1x1 size game to be draw" in {
      val game = new Game(1) with Board with Transformation2048 with RandomGenFake
      game.status === Draw
    }
  }

}
