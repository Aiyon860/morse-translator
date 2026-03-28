import scala.io.{StdIn, Source}
import java.io.{PrintWriter, File}
import scala.util.{Using, Success, Failure}

object MorseDictionary:
  val morseToChar: Map[String, Char] = Map(
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

  val charToMorse: Map[Char, String] = morseToChar.map(_.swap)

object MorseProcessor:
  private val WORD_SEPARATOR = " / "
  private val UNKNOWN_CHAR_FORMAT = "[%s?]"

  def encode(input: String): String =
    val normalized = input.trim.toUpperCase
    if normalized.isEmpty then ""
    else
      normalized
        .split("""\s+""")
        .map(word =>
          word.toSeq
            .map(char =>
              MorseDictionary.charToMorse.getOrElse(
                char,
                UNKNOWN_CHAR_FORMAT.format(char)
              )
            )
            .mkString(" ")
        )
        .mkString(WORD_SEPARATOR)

  def decode(input: String): String =
    val normalized =
      input.replaceAll("""\s{2,}|/|\|""", "|").replaceAll("""\|+""", "|")
    if normalized.isEmpty then ""
    else
      normalized
        .split("""\|""")
        .map(_.trim)
        .filter(_.nonEmpty)
        .map(word =>
          word
            .split("""\s+""")
            .map(symbol =>
              MorseDictionary.morseToChar.getOrElse(
                symbol,
                UNKNOWN_CHAR_FORMAT.format(symbol)
              )
            )
            .mkString("")
        )
        .mkString(" ")

object FileHandler:
  def processFile(
      srcPath: String,
      destPath: String,
      processor: String => String,
      operationType: String
  ): Unit =
    val srcFile = File(srcPath)
    val destFile = File(destPath)

    // Validasi file input
    if !srcFile.exists() then
      println(s"[Error] Source file not found: $srcPath")
      return

    if destFile.getParentFile != null && !destFile.getParentFile.exists() then
      println(s"[Error] Destination directory doesn't exist")
      return

    println(s"[Info] Processing '$srcPath'...")
    var lineCount = 0

    val result = Using.Manager { use =>
      val source = use(Source.fromFile(srcFile))
      val writer = use(new PrintWriter(destFile))

      source.getLines().foreach { line =>
        writer.println(processor(line))
        lineCount += 1
      }
    }

    result match
      case Success(_) =>
        println(
          s"[Success] $operationType completed. Processed $lineCount lines."
        )
        println(s"[Info] Result written to: $destPath")
      case Failure(e) =>
        println(s"[Error] An error occurred: ${e.getMessage}")
        if destFile.exists() then destFile.delete()

def displayMenu(): Unit =
  println("\n" + "=" * 35)
  println("   MORSE CODE TRANSLATOR PRO")
  println("=" * 35)
  println("1. Encode (Text → Morse)")
  println("2. Decode (Morse → Text)")
  println("3. Interactive Mode")
  println("4. Exit")
  print("\nSelect an option (1-4): ")

def readValidatedPath(prompt: String): String =
  print(prompt)
  val path = StdIn.readLine().trim
  if path.isEmpty then
    println("[Error] Path cannot be empty!")
    readValidatedPath(prompt)
  else path

def processFileOperation(operationType: String): Unit =
  println(s"\n--- $operationType MODE ---")
  val src = readValidatedPath("Enter source file path: ")
  val dest = readValidatedPath("Enter destination file path: ")

  val processor = operationType match
    case "ENCODING" => MorseProcessor.encode
    case "DECODING" => MorseProcessor.decode
    case _          => (s: String) => s

  FileHandler.processFile(src, dest, processor, operationType)

def interactiveMode(): Unit =
  println("\n--- INTERACTIVE MODE ---")
  println("Enter 'E' to encode, 'D' to decode, or 'Q' to quit")
  var running = true

  while running do
    print("\n[E]ncode / [D]ecode / [Q]uit: ")
    StdIn.readLine().trim.toUpperCase match
      case "E" =>
        print("Enter text to encode: ")
        val text = StdIn.readLine()
        println(s"Result: ${MorseProcessor.encode(text)}")
      case "D" =>
        print(
          "Enter morse code to decode (use space between chars, / for words): "
        )
        val morse = StdIn.readLine()
        println(s"Result: ${MorseProcessor.decode(morse)}")
      case "Q" =>
        println("[System] Exiting interactive mode...")
        running = false
      case _ =>
        println("[Error] Invalid option. Please enter E, D, or Q.")

@main def main(): Unit =
  var isRunning = true

  while isRunning do
    displayMenu()
    val choice = StdIn.readLine().trim

    choice match
      case "1" => processFileOperation("ENCODING")
      case "2" => processFileOperation("DECODING")
      case "3" => interactiveMode()
      case "4" =>
        println("\n[System] Exiting program... Goodbye!")
        isRunning = false
      case _ =>
        println("\n[Error] Invalid selection. Please choose 1, 2, 3, or 4.")
