class MorseEncoderTest extends munit.FunSuite:

  // ── Empty input ──────────────────────────────────────────────────────────
  test("encode: empty string returns empty string"):
    assertEquals(MorseProcessor.encode(""), "")

  // ── Single character ─────────────────────────────────────────────────────
  test("encode: single letter A"):
    assertEquals(MorseProcessor.encode("A"), ".-")

  test("encode: single letter E (shortest morse, one dot)"):
    assertEquals(MorseProcessor.encode("E"), ".")

  test("encode: single letter T (shortest morse, one dash)"):
    assertEquals(MorseProcessor.encode("T"), "-")

  test("encode: single letter Z"):
    assertEquals(MorseProcessor.encode("Z"), "--..")

  // ── Case insensitive ─────────────────────────────────────────────────────
  test("encode: lowercase input is converted to uppercase before encoding"):
    assertEquals(MorseProcessor.encode("hello"), ".... . .-.. .-.. ---")

  test("encode: mixed case input is normalized to uppercase"):
    assertEquals(MorseProcessor.encode("Hello"), ".... . .-.. .-.. ---")

  // ── Single word ──────────────────────────────────────────────────────────
  test("encode: single word SOS"):
    assertEquals(MorseProcessor.encode("SOS"), "... --- ...")

  test("encode: single word HELLO"):
    assertEquals(MorseProcessor.encode("HELLO"), ".... . .-.. .-.. ---")

  test("encode: single word WORLD"):
    assertEquals(MorseProcessor.encode("WORLD"), ".-- --- .-. .-.. -..")

  // ── Multi-word (words separated by ' / ') ────────────────────────────────
  test("encode: multi-word HELLO WORLD uses ' / ' as word separator"):
    assertEquals(
      MorseProcessor.encode("HELLO WORLD"),
      ".... . .-.. .-.. --- / .-- --- .-. .-.. -.."
    )

  test(
    "encode: multi-word with multiple spaces is treated as single word boundary"
  ):
    assertEquals(
      MorseProcessor.encode("HELLO  WORLD"),
      ".... . .-.. .-.. --- / .-- --- .-. .-.. -.."
    )

  test("encode: leading and trailing whitespace is trimmed"):
    assertEquals(MorseProcessor.encode("  SOS  "), "... --- ...")

  // ── Numbers ──────────────────────────────────────────────────────────────
  test("encode: digit 0"):
    assertEquals(MorseProcessor.encode("0"), "-----")

  test("encode: digit 5"):
    assertEquals(MorseProcessor.encode("5"), ".....")

  test("encode: number sequence 911"):
    assertEquals(MorseProcessor.encode("911"), "----. .---- .----")

  test("encode: mixed word and number SOS 911"):
    assertEquals(
      MorseProcessor.encode("SOS 911"),
      "... --- ... / ----. .---- .----"
    )

  // ── Symbols ──────────────────────────────────────────────────────────────
  test("encode: period symbol '.'"):
    assertEquals(MorseProcessor.encode("."), ".-.-.-")

  test("encode: comma symbol ','"):
    assertEquals(MorseProcessor.encode(","), "--..--")

  test("encode: question mark '?'"):
    assertEquals(MorseProcessor.encode("?"), "..--..")

  test("encode: at symbol '@'"):
    assertEquals(MorseProcessor.encode("@"), ".--.-.")

  // ── Unknown characters ───────────────────────────────────────────────────
  test("encode: unknown character is wrapped in [char?] notation"):
    assertEquals(MorseProcessor.encode("!"), "[!?]")

  test("encode: known and unknown characters are mixed correctly"):
    assertEquals(MorseProcessor.encode("A!"), ".- [!?]")

class MorseDecoderTest extends munit.FunSuite:

  // ── Empty input ──────────────────────────────────────────────────────────
  test("decode: empty string returns empty string"):
    assertEquals(MorseProcessor.decode(""), "")

  test("decode: whitespace-only input returns empty string"):
    assertEquals(MorseProcessor.decode("   "), "")

  // ── Single character ─────────────────────────────────────────────────────
  test("decode: '.-' decodes to A"):
    assertEquals(MorseProcessor.decode(".-"), "A")

  test("decode: '.' decodes to E"):
    assertEquals(MorseProcessor.decode("."), "E")

  test("decode: '-' decodes to T"):
    assertEquals(MorseProcessor.decode("-"), "T")

  test("decode: '--..' decodes to Z"):
    assertEquals(MorseProcessor.decode("--.."), "Z")

  // ── Single word ──────────────────────────────────────────────────────────
  test("decode: SOS morse code"):
    assertEquals(MorseProcessor.decode("... --- ..."), "SOS")

  test("decode: HELLO morse code"):
    assertEquals(MorseProcessor.decode(".... . .-.. .-.. ---"), "HELLO")

  test("decode: WORLD morse code"):
    assertEquals(MorseProcessor.decode(".-- --- .-. .-.. -.."), "WORLD")

  // ── Multi-word: / separator ──────────────────────────────────────────────
  test("decode: HELLO WORLD using '/' as word separator"):
    assertEquals(
      MorseProcessor.decode(".... . .-.. .-.. --- / .-- --- .-. .-.. -.."),
      "HELLO WORLD"
    )

  // ── Multi-word: | separator ──────────────────────────────────────────────
  test("decode: HELLO WORLD using '|' as word separator"):
    assertEquals(
      MorseProcessor.decode(".... . .-.. .-.. --- | .-- --- .-. .-.. -.."),
      "HELLO WORLD"
    )

  // ── Multi-word: 2+ spaces separator ─────────────────────────────────────
  test("decode: HELLO WORLD using 2 spaces as word separator"):
    assertEquals(
      MorseProcessor.decode(".... . .-.. .-.. ---  .-- --- .-. .-.. -.."),
      "HELLO WORLD"
    )

  test("decode: HELLO WORLD using 3 spaces as word separator"):
    assertEquals(
      MorseProcessor.decode(".... . .-.. .-.. ---   .-- --- .-. .-.. -.."),
      "HELLO WORLD"
    )

  // ── Multiple consecutive separators ──────────────────────────────────────
  test(
    "decode: consecutive '||' separators are collapsed into one word boundary"
  ):
    assertEquals(
      MorseProcessor.decode("... --- ... || ... --- ..."),
      "SOS SOS"
    )

  test(
    "decode: mixed separators '  ||  ' are collapsed into one word boundary"
  ):
    assertEquals(
      MorseProcessor.decode("... --- ...  ||  ... --- ..."),
      "SOS SOS"
    )

  // ── Numbers ──────────────────────────────────────────────────────────────
  test("decode: number sequence 911"):
    assertEquals(MorseProcessor.decode("----. .---- .----"), "911")

  test("decode: mixed text and number SOS 911"):
    assertEquals(
      MorseProcessor.decode("... --- ... / ----. .---- .----"),
      "SOS 911"
    )

  // ── Symbols ──────────────────────────────────────────────────────────────
  test("decode: period morse '.-.-.-'"):
    assertEquals(MorseProcessor.decode(".-.-.-"), ".")

  test("decode: question mark morse '..--..'"):
    assertEquals(MorseProcessor.decode("..--.."), "?")

  // ── Unknown morse symbols ─────────────────────────────────────────────────
  test("decode: unknown morse sequence is wrapped in [symbol?] notation"):
    assertEquals(MorseProcessor.decode("......"), "[......?]")

  test("decode: known and unknown morse are mixed correctly"):
    assertEquals(MorseProcessor.decode(".- ......"), "A[......?]")

class MorseRoundTripTest extends munit.FunSuite:

  test("round-trip: single word SOS survives encode then decode"):
    assertEquals(MorseProcessor.decode(MorseProcessor.encode("SOS")), "SOS")

  test("round-trip: multi-word HELLO WORLD survives encode then decode"):
    assertEquals(
      MorseProcessor.decode(MorseProcessor.encode("HELLO WORLD")),
      "HELLO WORLD"
    )

  test(
    "round-trip: mixed text and numbers SOS 911 survives encode then decode"
  ):
    assertEquals(
      MorseProcessor.decode(MorseProcessor.encode("SOS 911")),
      "SOS 911"
    )

  test("round-trip: lowercase input encodes and decodes back as uppercase"):
    assertEquals(MorseProcessor.decode(MorseProcessor.encode("hello")), "HELLO")

  test("round-trip: symbols survive encode then decode"):
    assertEquals(MorseProcessor.decode(MorseProcessor.encode("SOS?")), "SOS?")
