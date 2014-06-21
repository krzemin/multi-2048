package game2048

import org.scalajs.dom._

class WebSocketHelper(uri: String, receive: PartialFunction[Any, Unit]) {
  val ws = new WebSocket(uri)

  ws.onmessage = (evt: MessageEvent) => {
    val msg: Any = PicklingHelper.unpickle(evt.data)
    receive.applyOrElse(msg, (m: Any) => println(s"unhandled message: $m"))
  }

  def send(msg: Any) {
    val buff = PicklingHelper.pickle(msg)
    ws.send(buff)
  }

  def onConnect(code: => Unit) {
    ws.onopen = (evt: Event) => code
  }
}