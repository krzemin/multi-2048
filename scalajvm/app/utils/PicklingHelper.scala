package utils

import actors._
import game2048.Board
import game2048.Board._
import org.scalajs.spickling._

object PicklingHelper {

  def apply(): Unit = {
    PicklerRegistry.register(WantPlayHuman)
    PicklerRegistry.register(WantPlayAI)
    PicklerRegistry.register[Board]
    PicklerRegistry.register(None)
    PicklerRegistry.register[Some[Any]]
    PicklerRegistry.register(Nil)
    PicklerRegistry.register[::[Any]]
    PicklerRegistry.register[NewGame]
    PicklerRegistry.register[StateUpdate]
    PicklerRegistry.register(Left)
    PicklerRegistry.register(Up)
    PicklerRegistry.register(Right)
    PicklerRegistry.register(Down)
    PicklerRegistry.register[PerformMove]
  }

}
