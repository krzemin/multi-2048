package actors

import game2048.Board
import game2048.Board._
import game2048.Game._
import Status._
import Move._

case object WantPlayHuman
case object WantPlayAI // not really supported yet

case class NewGame(size: Int, board1: Board, board2: Board, yourFirstMove: Boolean)
case class StateUpdate(status: Status, board1: Board, board2: Board)
case class PerformMove(move: Move)

case class TestValue(value: String)


