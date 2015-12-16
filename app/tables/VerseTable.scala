package tables

import models.Verse
import slick.driver.JdbcProfile

/**
  * Created by Crafton Williams on 13/12/2015.
  */
trait VerseTable {

    protected val driver: JdbcProfile
    import driver.api._
    class Verses(tag: Tag) extends Table[Verse](tag, "VERSES") {
        def book = column[String]("book")
        def chapter = column[Int]("chapter")
        def verseNumber = column[Int]("versenumber")

        def * : (book, chapter, verseNumber) <> (Verse.tupled, Verse.unapply _)
    }

}
