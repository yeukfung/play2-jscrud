package net.amoeba.play2.jscrud.controllers

import play.api.mvc.Controller
import scala.reflect.runtime.{ universe => ru }
import play.api.mvc.Action
import scala.concurrent.Future
import net.amoeba.play2.jscrud.schema.JsonSchemar
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsObject

trait JsonSchemaGenMixin {
  this: Controller =>
  def tpe: ru.Type

  def schemaTransformer: Reads[JsObject] = __.read[JsObject]

  def genJsSchema = Action.async {
    Future.successful(Ok(
      JsonSchemar.genSchema(tpe).transform(schemaTransformer).get))
  }
}
