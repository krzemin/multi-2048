package actors

import akka.actor.{Props, ActorRef, Actor}

import org.scalajs.spickling._
import org.scalajs.spickling.playjson._
import play.api.libs.json.JsValue

object EchoActor {
  def props(out: ActorRef) = Props(new EchoActor(out))
}

class EchoActor(out: ActorRef) extends Actor {

  PicklerRegistry.register[String]
  PicklerRegistry.register[TestValue]

  def receive: Receive = {
    case msg: JsValue => PicklerRegistry.unpickle(msg) match {
      case TestValue(v) => out ! PicklerRegistry.pickle(TestValue(v))
    }
  }
}
