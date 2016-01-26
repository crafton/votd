package Utility

import play.Logger
import play.api.Play

/**
  * Created by Crafton Williams on 12/01/2016.
  */
object VerseUtils {

  def validateVerseLength (verses: String): Option[String] ={
    val maxVerses: Int = Play.current.configuration.getInt("verses.max").get //this needs to be checked at bootstrap
    val verseLength: Int = getVerseLength(verses).getOrElse(0)

    verseLength match {
      case 0 => Some("Something seems to be wrong with the verse format you entered. " +
        "Make sure your entry looks like this: John 4:1-5 or 1 John 1:1")
      case verseLength if verseLength > maxVerses => Some(s"Maximum number of verses exceeded. " +
        s"You selected $verseLength, but you can select only a maximum of $maxVerses verses.")
      case _ => None
    }


  }

  /**Get the number of verses the user is requesting
    *
    * @param verses
    * @return an optional integer representing the number of verses
    */
  def getVerseLength(verses: String): Option[Int] = {
    //Make sure the verses has the correct format.
    if(!verses.contains(":")){
      return None
    }
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
