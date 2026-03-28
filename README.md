# Morse Code Translator

A command-line Morse code encoder and decoder written in Scala, powered by [Scala CLI](https://scala-cli.virtuslab.org/).

## Features

- **Encode** — Convert plain text to Morse code
- **Decode** — Convert Morse code back to plain text
- **File Mode** — Process entire text files in one command
- **Interactive Mode** — Quickly encode/decode directly in the terminal
- **Flexible Separators** — Supports `/`, `|`, and multiple spaces as word boundaries when decoding
- **Unknown Character Handling** — Unrecognized characters are wrapped as `[char?]` instead of crashing

## Requirements

- [Scala CLI](https://scala-cli.virtuslab.org/) >= 1.0

## Project Structure

```
.
├── MorseEncoderAndDecoder.scala   # Main application source
├── MorseTest.scala                # Test suite (MUnit)
├── project.scala                  # Scala CLI configuration
├── samples/
│   ├── input/
│   │   ├── sample_text.txt        # Sample plain text for encoding
│   │   └── sample_morse.txt       # Sample Morse code for decoding
│   └── output/                    # Decoded/encoded output files (gitignored)
└── scratch/                       # Learning exercises (excluded from build)
```

## Getting Started

### Run the Application

```sh
scala-cli run MorseEncoderAndDecoder.scala
```

### Run the Tests

```sh
scala-cli test MorseTest.scala MorseEncoderAndDecoder.scala
```

## Usage

When you start the application, you will see the following menu:

```
========================================
   MORSE CODE TRANSLATOR PRO v2.0
========================================
1. Encode (Text → Morse)
2. Decode (Morse → Text)
3. Interactive Mode
4. Exit

Select an option (1-4):
```

---

### 1. Encode Mode — Text → Morse

Reads every line from a source file, encodes it to Morse code, and writes the result to a destination file.

**Prompt flow:**
```
--- ENCODING MODE ---
Enter source file path: samples/input/sample_text.txt
Enter destination file path: samples/output/encoded.txt
[Info] Processing 'samples/input/sample_text.txt'...
[Success] ENCODING completed. Processed 3 lines.
[Info] Result written to: samples/output/encoded.txt
```

**Input** (`samples/input/sample_text.txt`):
```
HELLO WORLD
SOS 911
MORSE CODE
```

**Output** (`samples/output/encoded.txt`):
```
.... . .-.. .-.. --- / .-- --- .-. .-.. -..
... --- ... / ----. .---- .----
-- --- .-. ... . / -.-. --- -.. .
```

> Words are separated by ` / ` in the encoded output.
> Characters within a word are separated by a single space.

---

### 2. Decode Mode — Morse → Text

Reads every line from a source file, decodes it from Morse code, and writes the result to a destination file.

**Prompt flow:**
```
--- DECODING MODE ---
Enter source file path: samples/input/sample_morse.txt
Enter destination file path: samples/output/decoded.txt
[Info] Processing 'samples/input/sample_morse.txt'...
[Success] DECODING completed. Processed 4 lines.
[Info] Result written to: samples/output/decoded.txt
```

**Supported word separators:**

| Separator            | Example                          |
|----------------------|----------------------------------|
| ` / ` (slash)        | `... --- ... / .- -...`          |
| ` \| ` (pipe)        | `... --- ... \| .- -...`         |
| 2 or more spaces     | `... --- ...  .- -...`           |

> Characters within a word are always separated by a single space.

---

### 3. Interactive Mode

Encode or decode without using any files — useful for quick lookups.

```
--- INTERACTIVE MODE ---
Enter 'E' to encode, 'D' to decode, or 'Q' to quit

[E]ncode / [D]ecode / [Q]uit: E
Enter text to encode: Hello World
Result: .... . .-.. .-.. --- / .-- --- .-. .-.. -..

[E]ncode / [D]ecode / [Q]uit: D
Enter morse code to decode (use space between chars, / for words): ... --- ...
Result: SOS

[E]ncode / [D]ecode / [Q]uit: Q
[System] Exiting interactive mode...
```

---

## Morse Code Reference

### Letters

| Letter | Code   | Letter | Code   | Letter | Code   |
|--------|--------|--------|--------|--------|--------|
| A      | `.-`   | J      | `.---` | S      | `...`  |
| B      | `-...` | K      | `-.-`  | T      | `-`    |
| C      | `-.-.` | L      | `.-..` | U      | `..-`  |
| D      | `-..`  | M      | `--`   | V      | `...-` |
| E      | `.`    | N      | `-.`   | W      | `.--`  |
| F      | `..-.` | O      | `---`  | X      | `-..-` |
| G      | `--.`  | P      | `.--.` | Y      | `-.--` |
| H      | `....` | Q      | `--.-` | Z      | `--..` |
| I      | `..`   | R      | `.-.`  |        |        |

### Digits

| Digit | Code    | Digit | Code    |
|-------|---------|-------|---------|
| 0     | `-----` | 5     | `.....` |
| 1     | `.----` | 6     | `-....` |
| 2     | `..---` | 7     | `--...` |
| 3     | `...--` | 8     | `---..` |
| 4     | `....-` | 9     | `----.` |

### Common Symbols

| Symbol | Code     |
|--------|----------|
| `.`    | `.-.-.-` |
| `,`    | `--..--` |
| `:`    | `---...` |
| `?`    | `..--..` |
| `'`    | `.----.` |
| `-`    | `-....-` |
| `/`    | `-..-.`  |
| `(`    | `-.--.-` |
| `)`    | `-.--.-` |
| `"`    | `.-..-.` |
| `=`    | `-...-`  |
| `+`    | `.-.-.`  |
| `@`    | `.--.-.` |

---

## Architecture

The code is organised into three top-level objects and three top-level functions for clarity:

| Component          | Type     | Responsibility                                         |
|--------------------|----------|--------------------------------------------------------|
| `MorseDictionary`  | `object` | Holds both lookup maps (morse ↔ char)                  |
| `MorseProcessor`   | `object` | Pure `encode` and `decode` logic                       |
| `FileHandler`      | `object` | File I/O with validation and error handling            |
| `displayMenu`      | `def`    | Renders the main menu                                  |
| `processFileOperation` | `def` | Handles file-mode encode/decode (DRY wrapper)         |
| `interactiveMode`  | `def`    | REPL-style interactive encode/decode loop              |

---

## Tests

Tests are written with [MUnit](https://scalameta.org/munit/) and cover three test classes:

| Class                | What it covers                                        |
|----------------------|-------------------------------------------------------|
| `MorseEncoderTest`   | Empty input, single chars, case sensitivity, numbers, symbols, unknown chars |
| `MorseDecoderTest`   | Empty input, single chars, all separator types, numbers, symbols, unknown morse |
| `MorseRoundTripTest` | End-to-end encode → decode round trips               |

Run all tests with:

```sh
scala-cli test MorseTest.scala MorseEncoderAndDecoder.scala
```

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).