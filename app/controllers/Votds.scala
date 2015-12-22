package controllers

import javax.inject.Inject

import models.votd.{VotdFormData, Votd}
import play.api._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc.{Action, Controller}

/**
  * Created by craft on 19/12/2015.
  */
class Votds @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport{

  val votdForm = Form (
    mapping (
    "versestart" -> nonEmptyText,
    "verseend" -> nonEmptyText,
    "themes" -> nonEmptyText
    )(VotdFormData.apply)(VotdFormData.unapply)
  )

  def index = ???

  def create = Action {
    Ok(views.html.votd.createVotd(votdForm))
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
