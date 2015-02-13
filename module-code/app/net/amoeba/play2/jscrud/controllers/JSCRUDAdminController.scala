package net.amoeba.play2.jscrud.controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import scala.concurrent.Future

abstract class JSCRUDAdminController extends Controller {
  def schemaPath: String

  def crud = Action.async { request =>
    Future.successful(Ok(net.amoeba.play2.jscrud.views.html.index(schemaPath)))
  }
}