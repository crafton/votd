package controllers

import java.util.regex.Pattern
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
import play.api.data.validation.Constraints._

import scala.concurrent.Future

/**
  * Created by craft on 19/12/2015.
  */
class Votds @Inject()(val messagesApi: MessagesApi, ws: WSClient) extends Controller with I18nSupport with VerseTable with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val votdForm = Form(
    mapping(
      "verses" -> nonEmptyText.verifying(pattern("(\\d\\s)?\\w+\\s(\\d{1,2}):(\\d{1,3})(\\s?-\\s?\\d{1,3})?".r)),
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
    val validationResult = VerseUtils.validateVerseLength(versesTrimmed)

    if (validationResult.isDefined) {
      Future {
        Logger.warn(validationResult.get)
        BadRequest(validationResult.get)
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
