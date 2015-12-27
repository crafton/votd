package controllers

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.i18n.{ MessagesApi, I18nSupport }
import javax.inject.Inject

import play.api.mvc.{Action, Controller}

class Application @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {


  def index = Action { implicit request =>
     Ok(views.html.index("Hello World!"))
  }

}