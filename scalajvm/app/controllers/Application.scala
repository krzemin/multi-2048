package controllers

import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._
import actors._
import utils.PicklingHelper

object Application extends Controller {

  import play.api.Play.current

  def index = Action {
    Ok(views.html.index())
  }

  def ws = WebSocket.acceptWithActor[JsValue,JsValue] { request => out =>
    PicklingProxyActor.props(out, PicklingHelper.apply, EchoActor.props)
  }

}





