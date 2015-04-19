package models

import play.api.libs.json.Json

case class Role (name:String, desc:String)

object RoleFormat {
  implicit val roleFormat = Json.format[Role]
}