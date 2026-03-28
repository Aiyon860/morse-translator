@main def main(): Unit =
  println(hasilValid)
  println(hasilInvalid)

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

// secara default map bersifat immutable
// jika ingin menambah data, Scala akan membuat map baru dengan data yang ditambahkan
// val mapBaru = morseToChar + ("???" -> "[]");

// Mengambil data dari Map
// Unsafe
// val hasil = morseToChar("[[[");
// Caused by: java.util.NoSuchElementException: key not found: [[[
// 	at scala.collection.immutable.BitmapIndexedMapNode.apply(HashMap.scala:674)

// Safe (menggunakan metode get dan return Option seperti misalnya Option[String])
// val hasil = morseToChar.get("[[[");
// Jika ada: Some("E"), jika tidak ada: None

// GetOrElse (|| di JS atau ?? di PHP)
// val hasil = morseToChar.getOrElse("[[[", '?')

// Challenge
val pesanInputInvalid = "Tidak ditemukan"
val (validInput, invalidInput) = ("...", "[[[")
val hasilValid = morseToChar.getOrElse(validInput, pesanInputInvalid)
val hasilInvalid = morseToChar.getOrElse(invalidInput, pesanInputInvalid)
