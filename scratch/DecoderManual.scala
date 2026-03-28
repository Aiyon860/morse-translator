// Unit == void in Java

@main def main(): Unit =
  println(s"Input: $input")
  val output = decode(input)
  println(s"Output: $output")

def decode(input: String): String = {
  val morseToChar = Map(
    ".-" -> "A",
    "-..." -> "B",
    "-.-." -> "C",
    "-.." -> "D",
    "." -> "E",
    "..-." -> "F",
    "--." -> "G",
    "...." -> "H",
    ".." -> "I",
    ".---" -> "J",
    "-.-" -> "K",
    ".-.." -> "L",
    "--" -> "M",
    "-." -> "N",
    "---" -> "O",
    ".--." -> "P",
    "--.-" -> "Q",
    ".-." -> "R",
    "..." -> "S",
    "-" -> "T",
    "..-" -> "U",
    "...-" -> "V",
    ".--" -> "W",
    "-..-" -> "X",
    "-.--" -> "Y",
    "--.." -> "Z"
  )

  val words = input.split("""\s{3,}""").toList;

  val decodedResult = words
    .map(word =>
      word.trim
        .split(" ")
        .toList
        .map(morseToChar.getOrElse(_, "???"))
        .mkString("")
    )
    .mkString(" ")

  decodedResult
}
