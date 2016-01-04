package controllers

import javax.inject.Inject

import models.votd.{VotdFormData, Votd}
import play.api._
import play.api.db.slick.{HasDatabaseConfig, HasDatabaseConfigProvider, DatabaseConfigProvider}
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.libs.ws._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc.{Action, Controller}
import slick.driver
import slick.driver.JdbcProfile
import tables.VerseTable
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by craft on 19/12/2015.
  */
class Votds @Inject()(val messagesApi: MessagesApi, ws: WSClient) extends Controller with I18nSupport with VerseTable with HasDatabaseConfig[JdbcProfile]{

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val votdForm = Form (
    mapping (
    "versestart" -> nonEmptyText,
    "verseend" -> nonEmptyText,
    "themes" -> text
    )(VotdFormData.apply)(VotdFormData.unapply)
  )

  val apiKey = Play.current.configuration.getString("biblesearch.key")

  def index = ???

  def create = Action {
    Ok(views.html.votd.createVotd(votdForm))
  }

  def getVerse(verseStart: String, verseEnd: String) = Action.async {
    ws.url(s"https://bibles.org/v2/passages.js?q[]=$verseStart-$verseEnd&version=eng-ESV")
    .withAuth(apiKey.get, "", WSAuthScheme.BASIC)
    .get().map { response =>
      Ok(((response.json \ "response" \ "search" \ "result" \ "passages")(0) \ "text").as[String])
    }
    /*val futureResponse: Future[WSResponse] = request.get()

    val futureResult = futureResponse.map{response => response.xml \ "response"}

    val result = Await.result(futureResult, 15 seconds)

    Ok(result)*/
  }

/*  def save = Action { implicit request =>
    formMapping.bindFromRequest.fold(
      errors => BadRequest(views.html.votd.createVotd)
      votd => Ok(views.html.votd.createVotd)
    )
  }*/

  def update = ???

  def delete = ???

}
