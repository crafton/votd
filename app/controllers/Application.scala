package controllers

import models.votd.Verse
import play.api._
import play.api.data.Form
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.mvc._
import slick.driver.JdbcProfile
import tables.VerseTable
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with VerseTable with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  //Create instance of the verses table
  val Verses = TableQuery[Verses]

  def index = Action.async { implicit request =>
    dbConfig.db.run(Verses.filter(_.book like "%John%").result).map(res => Ok(views.html.index(res.toList)))
  }

}