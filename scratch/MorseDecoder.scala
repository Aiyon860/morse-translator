import scala.io.Source
import java.io.{PrintWriter, File}
import scala.util.Using
import scala.util.Success
import scala.util.Failure

private val morseToChar: Map[String, Char] = Map(
  // Huruf
  ".-" -> 'A',
  "-..." -> 'B',
  "-.-." -> 'C',
  "-.." -> 'D',
  "." -> 'E',
  "..-." -> 'F',
  "--." -> 'G',
  "...." -> 'H',
  ".." -> 'I',
  ".---" -> 'J',
  "-.-" -> 'K',
  ".-.." -> 'L',
  "--" -> 'M',
  "-." -> 'N',
  "---" -> 'O',
  ".--." -> 'P',
  "--.-" -> 'Q',
  ".-." -> 'R',
  "..." -> 'S',
  "-" -> 'T',
  "..-" -> 'U',
  "...-" -> 'V',
  ".--" -> 'W',
  "-..-" -> 'X',
  "-.--" -> 'Y',
  "--.." -> 'Z',

  // Angka
  "-----" -> '0',
  ".----" -> '1',
  "..---" -> '2',
  "...--" -> '3',
  "....-" -> '4',
  "....." -> '5',
  "-...." -> '6',
  "--..." -> '7',
  "---.." -> '8',
  "----." -> '9',

  // Simbol Umum
  ".-.-.-" -> '.',
  "--..--" -> ',',
  "---..." -> ':',
  "..--.." -> '?',
  ".----." -> '\'',
  "-....-" -> '-',
  "-..-." -> '/',
  "-.--." -> '(',
  "-.--.-" -> ')',
  ".-..-." -> '\"',
  "-...-" -> '=',
  ".-.-." -> '+',
  ".--.-." -> '@'
)

@main def main(inputFile: String, outputFile: String): Unit =
  println(s"Reading from: $inputFile")

  val result = Using.Manager { use =>
    val source = use(Source.fromFile(inputFile))
    val writer = use(new PrintWriter(outputFile))

    source.getLines().foreach { line =>
      writer.println(decodeMorse(line))
    }
  }

  result match
    case Success(_) => println(s"Success! Result written to: $outputFile")
    case Failure(e) => println(s"Error: ${e.getMessage}")

def decodeMorse(input: String): String =
  val normalized = input.replaceAll("""\s{3,}|/|\|""", "|")

  val words = normalized.split("""\|""")

  words.toSeq
    .map(_.trim)
    .filter(_.nonEmpty)
    .map(word =>
      word
        .split("""\s+""")
        .map(symbol => morseToChar.getOrElse(symbol, s"[$symbol?]"))
        .mkString("")
    )
    .mkString(" ")
