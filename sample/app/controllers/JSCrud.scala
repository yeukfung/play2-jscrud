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

class PersonController extends JSCRUDReactiveMongoController[Person] {
  val coll = db.collection[JSONCollection]("persons")
}

object PersonRestRouter extends JSCRUDRouter with JsonSchemaGenMixin {
  lazy val resController = new PersonController

  def tpe = ru.typeOf[Person]

  override val schemaTransformer = personSchemaTransform
}

object PersonCRUDAdmin extends JSCRUDAdminController {
  val schemaPath = controllers.routes.PersonRestRouter.genJsSchema().url

}

