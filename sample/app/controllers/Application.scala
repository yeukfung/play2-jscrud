package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits._
import models.Person
import java.util.Date

object Application extends Controller {

  def reloadDemoData = Action.async {
    val personDao = controllers.PersonRestRouter.resController.res
    personDao.batchDelete(Json.obj()) map { le =>
      (1 to 100).foreach { i =>
        personDao.insert(Person("name" + i, None, None, None))
      }

      Ok("done")
    }
  }

}