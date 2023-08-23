import cats.effect.IO
import fs2._

import scala.annotation.tailrec

class TextFormatterIO {

  def format(fullText: String, charLimit: Int): IO[Unit] = {
    val output = input(fullText.filter(_ >= ' ')).through(pipe(charLimit)) //remove new lines and other command chars
      .evalMap(line => IO(println(line)))

    output.compile.drain.handleErrorWith { error =>
      IO(println(s"Caught an error: ${error.getMessage}"))
    }
  }

  private def input(fullText: String): Stream[Pure, Char] = Stream.emits(fullText.toCharArray)

  private def pipe(charLimit: Int): Pipe[IO, Char, String] = {

    def chunkStream(stream: Stream[IO, Char], charLimit: Int, unusedChars: Array[Char] = Array.empty): Pull[IO, String, Unit] = {
      stream.pull.unconsLimit((charLimit - unusedChars.length) + 1).flatMap { //pull X+1 chars from the stream
        case Some((head, tail)) =>
          val processedChunk = processChunk(unusedChars ++ head.toArray, charLimit) //keep any used chars in memory for the next iteration
          processedChunk match {
            case Some(chunk) => Pull.output1(chunk.line) >> chunkStream(tail, charLimit, chunk.unusedChars.mkString.trim.toCharArray) //output each line, then recursion
            case None => Pull.raiseError[IO](new Exception("Invalid index"))
          }
        case None => Pull.done
      }
    }

    in => chunkStream(in, charLimit).stream
  }

  private case class ProcessedChunk(line: String, unusedChars: Array[Char] = Array.empty)

  private def processChunk(chunk: Array[Char], charLimit: Int): Option[ProcessedChunk] = {
    chunk.toList match {
      case list if list.length < charLimit => Some(ProcessedChunk(list.toArray.mkString))
      case init :+ last if last.isWhitespace => //no breakage in the word
        Some(ProcessedChunk(init.toArray.mkString))
      case init :+ secondLast :+ last if secondLast.isWhitespace => //we know the word has ended
        Some(ProcessedChunk(init.toArray.mkString, Array(last)))
      case list => //we know we are currently in the middle of a word, rollback to last whitespace
        val index = findIndexOfLastWhitespace(list)
        index.fold(None: Option[ProcessedChunk]) { validIndex =>
          Some(ProcessedChunk(chunk.splitAt(validIndex)._1.mkString, chunk.splitAt(validIndex)._2))
        }
    }
  }

  @tailrec
  private def findIndexOfLastWhitespace(chars: List[Char]): Option[Int] = {
    chars match {
      case _ :+ last if last.isWhitespace => Some(chars.length - 1)
      case list if list.nonEmpty => findIndexOfLastWhitespace(chars.init)
      case _ => None
    }
  }

}
