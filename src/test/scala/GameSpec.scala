import org.specs2.mutable._

class GameSpec extends Specification {

  import Game.Status._

  "Game" should {
    val game = new Game(4) with Board with Transformation2048 with RandomGenFake
    "claim in-progress status for new game" in {
      game.status === InProgress
    }
  }

}
