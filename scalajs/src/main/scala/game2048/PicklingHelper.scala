package game2048

import actors._
import game2048.Board._
import game2048.Game._
import org.scalajs.spickling._
import org.scalajs.spickling.jsany._
import scala.scalajs.js

object PicklingHelper {
  def registerTypes() {
    PicklerRegistry.register(WantPlayHuman)
    PicklerRegistry.register(WantPlayAI)
    PicklerRegistry.register[Board]
    PicklerRegistry.register(None)
    PicklerRegistry.register[Some[Any]]
    PicklerRegistry.register(Nil)
    PicklerRegistry.register[::[Any]]
    PicklerRegistry.register[NewGame]
    PicklerRegistry.register(InProgress)
    PicklerRegistry.register(Draw)
    PicklerRegistry.register(Player1Won)
    PicklerRegistry.register(Player2Won)
    PicklerRegistry.register[StateUpdate]
    PicklerRegistry.register(Left)
    PicklerRegistry.register(Up)
    PicklerRegistry.register(Right)
    PicklerRegistry.register(Down)
    PicklerRegistry.register[PerformMove]
  }

  def pickle(msg: Any): js.Any = {
    val pickled: js.Any = PicklerRegistry.pickle(msg)
    js.JSON.stringify(pickled)
  }

  def unpickle(buffer: Any): Any = {
    val obj: js.Any = js.JSON.parse(buffer.asInstanceOf[js.String]).asInstanceOf[js.Any]
    PicklerRegistry.unpickle(obj)
  }
}