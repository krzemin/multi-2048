package actors

import game2048.Board
import game2048.Board._
import game2048.Game._

case object WantPlayHuman
case object WantPlayAI

case class NewGame(size: Int, board1: Board, board2: Board, yourFirstMove: Boolean)
case class StateUpdate(status: Status, board1: Board, board2: Board)
case class PerformMove(move: Move)
