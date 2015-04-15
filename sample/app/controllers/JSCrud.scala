package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import models.Person
import models.PersonFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import net.amoeba.play2.jscrud.libs.autosource.JSCRUDReactiveMongoController
import scala.concurrent.ExecutionContext.Implicits._
import net.amoeba.play2.jscrud.libs.autosource.JSCRUDRouter
import net.amoeba.play2.jscrud.controllers.JsonSchemaGenMixin
import scala.reflect.runtime.{ universe => ru }
import net.amoeba.play2.jscrud.controllers.JSCRUDAdminController
import net.amoeba.play2.jscrud.controllers.JSCRUDAdminIndexingController
import net.amoeba.play2.jscrud.models.MenuItem
import net.amoeba.play2.jscrud.controllers.JSCRUDSettings

class PersonController extends JSCRUDReactiveMongoController[Person] {
  val coll = db.collection[JSONCollection]("persons")
}

object PersonRestRouter extends JSCRUDRouter with JsonSchemaGenMixin {
  lazy val resController = new PersonController

  def tpe = ru.typeOf[Person]

  override val schemaTransformer = personSchemaTransform

}

object PersonCRUDAdmin extends JSCRUDAdminController with SampleJSCRUDSettings {
  val jsonSchemaUrl = controllers.routes.PersonRestRouter.genJsSchema().url
  val restUrl = "/api/persons/:id"
  val title = "Person"

  val columns = Map("name" -> "Name")

  val menuItem = MenuItem(title, controllers.routes.PersonCRUDAdmin.jscrud().url, "fa-dashboard")
}

object Person2CRUDAdmin extends JSCRUDAdminController with SampleJSCRUDSettings {
  val jsonSchemaUrl = controllers.routes.PersonRestRouter.genJsSchema().url
  val restUrl = "/api/persons/:id"
  val title = "Person2"

  val columns = Map("name" -> "Name2")

  val menuItem = MenuItem(title, controllers.routes.Person2CRUDAdmin.jscrud().url, "fa-dashboard")
}


object CRUDAdmin extends JSCRUDAdminIndexingController with SampleJSCRUDSettings {}

trait SampleJSCRUDSettings extends JSCRUDSettings {
  override val crudControllers = PersonCRUDAdmin.asInstanceOf[JSCRUDAdminController] :: Person2CRUDAdmin.asInstanceOf[JSCRUDAdminController] :: Nil  
}
