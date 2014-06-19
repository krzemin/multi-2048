package game2048

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.extensions._

object JSGame2048 extends js.JSApp {

  def main(): Unit = {


    val canvas = dom.document.createElement("canvas").cast[dom.HTMLCanvasElement]
    val ctx = canvas.getContext("2d").cast[dom.CanvasRenderingContext2D]

    canvas.width = 812
    canvas.height = 408
    dom.document.body.appendChild(canvas)

    val game = new Game(4) with Board with Transformation2048 with RandomGen with GameRenderer {
      override def renderGame(g: Game) {
        val (brdW, brdH) = (400, 400)

        ctx.fillStyle = "rgb(130,170,190)"
        ctx.fillRect(0, 0, canvas.width, canvas.height)

        renderBoard(4, 4, brdW, brdH, g.board1)
        renderBoard(brdW+8, 4, brdW, brdH, g.board2)

        println(g)
      }

      def renderBoard(x: Int, y: Int, w: Int, h: Int, board: Board) {
        val size = board.size
        val (cellW, cellH) = (w / size, h / size)
        val idxBoard = board.map(_.zipWithIndex).zipWithIndex


        idxBoard.foreach { case (row, j) =>
          row.foreach { case (cell, i) =>
            ctx.fillStyle = "rgb(190,240,140)"
            val (cellX, cellY) = (x + cellW * i + 1, y + cellH * j + 1)

            ctx.fillRect(cellX, cellY, cellW - 2, cellH - 2)

            cell.foreach { case number =>
              ctx.font = s"${3*cellH/4}px monospace"
              ctx.fillStyle = "rgb(90,20,10)"
              ctx.textAlign = "center"
              ctx.textBaseline = "middle"
              ctx.fillText(number.toString, cellX + cellW / 2, cellY + cellH / 2)
            }

          }
        }
      }
    }

    game.render()

    game.move(Board.Move.Up)

    game.render()


  }

  /** Computes the square of an integer.
   *  This demonstrates unit testing.
   */
  def square(x: Int): Int = x*x
}
