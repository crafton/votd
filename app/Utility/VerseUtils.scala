package Utility

import play.Logger
import play.api.Play

/**
  * Created by Crafton Williams on 12/01/2016.
  */
object VerseUtils {

  def getVerseLength(verses: String): Option[Int] = {
    val verseRange: String = verses.split(":").last.trim

    if(!verseRange.contains("-")){
      return Some(1)
    }
    try {
      val verseNumbers = verseRange.split("-").map(_.trim)
      Some(verseNumbers(1).toInt - verseNumbers(0).toInt)
    } catch {
      case e: NumberFormatException => None
    }
  }

}
