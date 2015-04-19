package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import models.Person
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.ExecutionContext.Implicits._
import net.amoeba.play2.jscrud.controllers.JsonSchemaGenMixin
import scala.reflect.runtime.{ universe => ru }
import net.amoeba.play2.jscrud.controllers.JSCRUDAdminController
import net.amoeba.play2.jscrud.controllers.JSCRUDAdminIndexingController
import net.amoeba.play2.jscrud.models._
import net.amoeba.play2.jscrud.controllers.JSCRUDSettings
import play.autosource.reactivemongo.ReactiveMongoAutoSourceController
import models.Group
import models.GroupFormat._
import models.RoleFormat._
import models.PersonFormat._
import models.Role
import play.api.libs.json.JsArray
import play.api.libs.json.Json
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** supportive REST API router **/
object GroupRestRouter extends ReactiveMongoAutoSourceController[Group] with JsonSchemaGenMixin {

  lazy val coll = db.collection[JSONCollection]("groups")

  def tpe = ru.typeOf[Group]
}

object RoleRestRouter extends ReactiveMongoAutoSourceController[Role] with JsonSchemaGenMixin {

  lazy val coll = db.collection[JSONCollection]("roles")

  def tpe = ru.typeOf[Role]
}

object PersonRestRouter extends ReactiveMongoAutoSourceController[Person] with JsonSchemaGenMixin {

  lazy val coll = db.collection[JSONCollection]("persons")

  def tpe = ru.typeOf[Person]

  override val schemaTransformer = personSchemaTransform

  override def schemaFormFields: JsArray = {
    val groupList = Await.result(GroupRestRouter.res.find(Json.obj(), 1000, 0).map {
      case l => l.foldLeft(Json.arr())((acc, item) => acc.append(Json.obj("value" -> item._2.stringify, "name" -> item._1.name)))
    }, Duration(5, "seconds"))
    Json.parse(s"""["name", "age", {
     "key": "group",
     "type" : "select",
     "titleMap" : ${groupList}
     }]""").as[JsArray]
  }

}

/** Admin main page and settings **/
object CRUDAdmin extends JSCRUDAdminIndexingController with SampleJSCRUDSettings {}

trait SampleJSCRUDSettings extends JSCRUDSettings {
  override val crudControllers =
    PersonCRUDAdmin.asInstanceOf[JSCRUDAdminController] ::
      GroupCRUDAdmin.asInstanceOf[JSCRUDAdminController] ::
      RoleCRUDAdmin.asInstanceOf[JSCRUDAdminController] :: Nil
}

/** Admin Controller **/

object PersonCRUDAdmin extends JSCRUDAdminController with SampleJSCRUDSettings {
  val title = "Person"
  val menuItem = MenuItem(title, controllers.routes.PersonCRUDAdmin.jscrud().url, "fa-dashboard")

  val jscrudParam = JSCRUDParam(
    schemaUrl = controllers.routes.PersonRestRouter.genJsSchema().url,
    restUrl = "/api/persons/:id",
    columns = Map(
      "name" -> ColumnParam("Name"),
      "age" -> ColumnParam("Age"),
      "gender" -> ColumnParam("Gender"),
      "group" -> ColumnParam("Group")))

  override def dictionary = {
    val groupMaps = Await.result(GroupRestRouter.res.find(Json.obj(), 1000, 0).map {
      case l => l.foldLeft(Map[String, String]())((acc, item) => acc + (item._2.stringify -> item._1.name))
    }, Duration(5, "seconds"))
    Map("group" -> groupMaps)
  }
}

object GroupCRUDAdmin extends JSCRUDAdminController with SampleJSCRUDSettings {
  val title = "Group"
  val menuItem = MenuItem(title, controllers.routes.GroupCRUDAdmin.jscrud().url, "fa-group")

  val jscrudParam = JSCRUDParam(
    schemaUrl = controllers.routes.GroupRestRouter.genJsSchema().url,
    restUrl = "/api/groups/:id",
    columns = Map(
      "name" -> ColumnParam("Name"),
      "desc" -> ColumnParam("Desc")))
}

object RoleCRUDAdmin extends JSCRUDAdminController with SampleJSCRUDSettings {
  val title = "Role"
  val menuItem = MenuItem(title, controllers.routes.RoleCRUDAdmin.jscrud().url, "fa-group")

  val jscrudParam = JSCRUDParam(
    schemaUrl = controllers.routes.RoleRestRouter.genJsSchema().url,
    restUrl = "/api/roles/:id",
    columns = Map(
      "name" -> ColumnParam("Name"),
      "desc" -> ColumnParam("Description")))

}


