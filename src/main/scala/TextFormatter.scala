import scala.annotation.tailrec

class TextFormatter {

  def format(fullText: String, charLimit: Int): List[String] = {
    if(charLimit > 0){
      processLines(fullText.filter(_ >= ' '), charLimit) //remove control characters, new lines etc.
    } else {
      List.empty
    }
  }

  @tailrec
  private def processLines(fullText: String, charLimit: Int, formattedText: List[String] = List.empty): List[String] = {
    fullText match {
      case text if text.length <= charLimit => formattedText ++ List(fullText.trim) //final line of text
      case text => {
        findIndexFromLine(text.take(charLimit + 1), charLimit) match { //returns the correct index to split the line from
          case Some(index) =>
            val splitText = fullText.splitAt(index)
            processLines(splitText._2.trim, charLimit, formattedText ++ List(splitText._1.trim)) //add processed line to List, repeat for next line
          case None => List.empty
        }
      }
    }
  }

  @tailrec
  private def findIndexFromLine(line: String, correctIndex: Int): Option[Int] = { //takes charLimit+1, checks last 2 chars
    line match {
      case _ if line.isEmpty => None //impossible to print with charLimit
      case char if char.last.isWhitespace || line(line.length - 1).isWhitespace => Some(correctIndex) //if last or 2nd last char is space, no need to make line shorter
      case char if !char.last.isWhitespace => findIndexFromLine(line.init, correctIndex - 1) //if last char is inside a word, recursively find the start of that word
    }
  }

}
