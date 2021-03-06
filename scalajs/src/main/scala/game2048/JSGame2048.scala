package game2048

import game2048.Board._
import game2048.Game._
import org.scalajs.dom.{Event, MessageEvent, WebSocket}

import scala.scalajs.js
import org.scalajs.jquery._
import org.scalajs.dom
import org.scalajs.dom.extensions._
import js.Dynamic.{ global => g }

import actors._

object JSGame2048 extends js.JSApp {

  val wsUri = "ws://localhost:9000/ws"

  def main(): Unit = {

    val startButton = jQuery("<button>Start</button>").attr("disabled", "disabled")
    jQuery("body").append(startButton)

    val canvas = dom.document.createElement("canvas").cast[dom.HTMLCanvasElement]
    canvas.style.margin = "0px auto"
    canvas.style.marginTop = "40px"
    canvas.style.display = "block"
    val ctx = canvas.getContext("2d").cast[dom.CanvasRenderingContext2D]

    canvas.width = 812
    canvas.height = 438
    dom.document.body.appendChild(canvas)

    val game = new Game(4) with Transformation2048 with RandomGen with GameRenderer {
      override def renderGame(g: Game) {
        val (brdW, brdH) = (400, 400)

        ctx.fillStyle = "rgb(130,170,190)"
        ctx.fillRect(0, 0, canvas.width, canvas.height)

        ctx.textAlign = "center"
        ctx.textBaseline = "middle"

        renderBoard(4, 4, brdW, brdH, g.board1)
        renderBoard(brdW+8, 4, brdW, brdH, g.board2)
        renderStatus(g)
      }

      def renderStatus(g: Game) {
        ctx.font = "22px monospace"
        ctx.fillStyle = "rgb(20,60,150)"
        ctx.fillText(g.score1 + (" " * 30) + g.score2, canvas.width / 2, 408 + 12)

        if(g.status != Game.InProgress) {
          ctx.fillStyle = "rgb(220, 120, 20)"
          ctx.fillRect(canvas.width / 4, canvas.height / 4, canvas.width / 2, canvas.height / 2)

          ctx.font = "bold 40px monospace"
          ctx.fillStyle = "black"
          ctx.fillText(g.status.toString, canvas.width / 2, canvas.height / 2)
        }
      }

      def renderBoard(x: Int, y: Int, w: Int, h: Int, board: Board) {
        val size = board.size
        val (cellW, cellH) = (w / size, h / size)
        val idxFields = board.fields.map(_.zipWithIndex).zipWithIndex

        idxFields.foreach { case (row, j) =>
          row.foreach { case (cell, i) =>
            ctx.fillStyle = "rgb(190,240,140)"
            val (cellX, cellY) = (x + cellW * i + 1, y + cellH * j + 1)

            ctx.fillRect(cellX, cellY, cellW - 2, cellH - 2)

            cell.foreach { case number =>
              ctx.font = s"bold ${cellH/3}px monospace"
              ctx.fillStyle = "rgb(90,20,10)"
              ctx.fillText(number.toString, cellX + cellW / 2, cellY + cellH / 2)
            }

          }
        }
      }
    }

    PicklingHelper.registerTypes()

    val ws = WebSocketHelper(wsUri) {
      case NewGame(size, board1, board2, imFirst) =>
        game.board1 = board1
        game.board2 = board2
        game.render()
      case StateUpdate(status, board1, board2) =>
        game.board1 = board1
        game.board2 = board2
        game.render()
    }

    ws.onConnect {
      startButton.removeAttr("disabled")
    }

    startButton click { _: JQueryEventObject =>
      ws.send(WantPlayAI)
    }

    def move(move: Move) {
      game.move(move)
      ws.send(PerformMove(move))
    }

    g.addEventListener("keydown", (e: dom.KeyboardEvent) => {
      e.keyCode match {
        case 37 => move(Left)
        case 38 => move(Up)
        case 40 => move(Down)
        case 39 => move(Right)
        case _  =>
      }
      game.render()
    }, false)

  }
}
