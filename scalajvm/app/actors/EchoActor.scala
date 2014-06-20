package actors

import akka.actor.{Props, ActorRef, Actor}
import play.api.libs.json.JsValue

object EchoActor {
  def props(out: ActorRef) = Props(new EchoActor(out))
}

class EchoActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: JsValue => out ! msg
  }
}
