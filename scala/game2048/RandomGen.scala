import scala.util.Random

trait RandomGen {
  lazy val rand = new Random()
}
