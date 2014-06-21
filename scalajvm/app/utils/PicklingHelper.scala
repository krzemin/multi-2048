package utils

import actors._
import game2048.Board
import game2048.Board._
import game2048.Game._
import org.scalajs.spickling._

object PicklingHelper {

  def registerTypes(): Unit = {
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

}
