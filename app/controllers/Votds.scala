package controllers

import javax.inject.Inject

import Utility.VerseUtils
import models.votd.VotdFormData
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws._
import play.api.mvc.{Action, Controller}
import slick.driver.JdbcProfile
import tables.VerseTable
import play.Logger
import views.html.defaultpages.notFound

import scala.concurrent.Future

/**
  * Created by craft on 19/12/2015.
  */
class Votds @Inject()(val messagesApi: MessagesApi, ws: WSClient) extends Controller with I18nSupport with VerseTable with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val votdForm = Form(
    mapping(
      "verses" -> nonEmptyText,
      "themes" -> optional(text)
    )(VotdFormData.apply)(VotdFormData.unapply)
  )

  val apiKey = Play.current.configuration.getString("biblesearch.key")

  def index = ???

  def create = Action {
    Ok(views.html.votd.createVotd(votdForm))
  }

  def getVerse(verses: String) = Action.async {

    val versesTrimmed: String = verses.trim

    val maxVerses: Int = Play.current.configuration.getInt("verses.max").get //this needs to be checked at bootstrap
    val verseLength = VerseUtils.getVerseLength(versesTrimmed).getOrElse(0)

    if (verseLength == 0) {
      Logger.warn("Apparently an integer was not used for the verse number")
    }

    if (verseLength > maxVerses) {
      Logger.warn(s"You tried to retrieve $verseLength verses but the maximum is $maxVerses.")
      Future {
        BadRequest(s"Maximum number of verses exceeded. You can select only a maximum of $maxVerses verses.")
      }
    } else {
      ws.url(s"https://bibles.org/v2/passages.js?q[]=$versesTrimmed&version=eng-ESV")
        .withAuth(apiKey.get, "", WSAuthScheme.BASIC)
        .get().map { response =>
        Ok("<h3>" + ((response.json \ "response" \ "search" \ "result" \ "passages") (0) \ "display").as[String] +
          "</h3>" + ((response.json \ "response" \ "search" \ "result" \ "passages") (0) \ "text").as[String])
      }
    }

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
