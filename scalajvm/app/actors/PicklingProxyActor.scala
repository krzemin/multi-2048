package actors

import akka.actor._

import org.scalajs.spickling._
import org.scalajs.spickling.playjson._
import play.api.libs.json.JsValue

object PicklingProxyActor {
  def props(out: ActorRef, proxiedProps: ActorRef => Props) =
    Props(new PicklingProxyActor(out, proxiedProps))
}


class PicklingProxyActor(out: ActorRef, proxiedProps: ActorRef => Props) extends Actor {

  PicklerRegistry.register[TestValue]

  val proxiedRef = context.actorOf(proxiedProps(self))

  def receive = {
    case incomingMsg: JsValue =>
      val unpickledMsg: Any = PicklerRegistry.unpickle(incomingMsg)
      proxiedRef ! unpickledMsg

    case outgoingMsg: Any =>
      val pickledMsg: JsValue = PicklerRegistry.pickle(outgoingMsg)
      out ! pickledMsg
  }

}
