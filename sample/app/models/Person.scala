package models

import play.api.libs.json.Json
import net.amoeba.play2.jscrud.schema.JsonSchemar.JsonTransformHelper

case class Person(
  name: String,
  age: Option[Int],
  gender: Option[String])

object PersonFormats {

  implicit val personFormat = Json.format[Person]

  import play.api.libs.json._
  import play.api.libs.json.Reads._
  import play.api.libs.functional.syntax._
  import JsonTransformHelper._

  val personSchemaTransform = (__.read[JsObject] and
    jstRequire("name", "age") and
    jstDescription("name", "give me your name") and
    jstMinLength("name", 5) and
    jstEnum("gender")("male", "female", "unknown") and
    jstDefault("gender", "unknown")
  ) reduce

}