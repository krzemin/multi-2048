package game2048

import scala.util.Random

trait RandomGen {
  lazy val rand = new Random()
}
