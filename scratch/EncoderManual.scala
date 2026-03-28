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

private val charToMorse: Map[Char, String] = morseToChar.map(_.swap)

@main def main(): Unit =

  val input = "Halo Dunia"
  val output = encodeLine(input)
  println(output)

def encodeLine(input: String): String = {
  val words = input.trim.toUpperCase
    .split("""\s+""")
    .map { word =>
      word.toSeq
        .map(char => charToMorse.getOrElse(char, s"[${char}?"))
        .mkString(" ")
    }
    .mkString(" / ")
  words
}
