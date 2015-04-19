package models

import play.api.libs.json.Json

case class Group(name: String, desc: Option[String])

object GroupFormat {
  implicit val groupFormat = Json.format[Group]
}