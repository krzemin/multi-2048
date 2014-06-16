import scala.util.Random

trait RandomGenFake extends RandomGen {
  override val rand = new Random(0)
}
