package game2048

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.extensions._

object JSGame2048 extends js.JSApp {

  def main(): Unit = {

    val game = new Game(4) with Board with Transformation2048 with RandomGen

    val canvas = dom.document.createElement("canvas").cast[dom.HTMLCanvasElement]
    val ctx = canvas.getContext("2d").cast[dom.CanvasRenderingContext2D]

    canvas.width = 800
    canvas.height = 400
    dom.document.body.appendChild(canvas)

    ctx.fillStyle = "rgb(130,170,190)"
    ctx.fillRect(0, 0, canvas.width, canvas.height)

  }

  /** Computes the square of an integer.
   *  This demonstrates unit testing.
   */
  def square(x: Int): Int = x*x
}
