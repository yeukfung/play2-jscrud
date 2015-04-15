package net.amoeba.play2.jscrud.controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import scala.concurrent.Future
import scala.reflect.runtime.{ universe => ru }
import net.amoeba.play2.jscrud.schema.JsonSchemar
import net.amoeba.play2.jscrud.models.MenuItem

abstract class JSCRUDAdminController extends Controller { this: JSCRUDSettings =>
  def jsonSchemaUrl: String
  def restUrl: String
  def title: String
  def columns: Map[String, String]

  def menuItem: MenuItem

  def jscrud = Action.async { implicit request =>
    Future.successful(Ok(net.amoeba.play2.jscrud.views.html.index(title, jsonSchemaUrl, restUrl)))
  }
}

abstract class JSCRUDAdminIndexingController extends Controller { this: JSCRUDSettings =>

  def indexPage = Action.async { implicit request =>
    Future.successful(Ok(net.amoeba.play2.jscrud.views.html.adminHome("JsCRUD Home")))
  }
}

trait JSCRUDSettings {
  def crudControllers: List[JSCRUDAdminController]
  implicit def menuItems: List[MenuItem] = crudControllers.map(_.menuItem)
}
