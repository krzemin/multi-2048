package game2048

import scala.util.Random

trait RandomGenFake extends RandomGen {
  override lazy val rand = new Random(0)
}
