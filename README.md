# Vibrion ID3 – Kotlin Multiplatform ID3 Tag Writer

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-%237F52FF.svg?style=flat-square&logo=kotlin&logoColor=white)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kingg22/vibrion-id3)
![Maven Central Last Update](https://img.shields.io/maven-central/last-update/io.github.kingg22/vibrion-id3)

![GitHub License](https://img.shields.io/github/license/kingg22/vibrion-id3)
![GitHub last commit (branch)](https://img.shields.io/github/last-commit/kingg22/vibrion-id3/main)

A Kotlin Multiplatform library for write ID3 (v2.3) tag to MP3 files. Java Compatible. Currently, reading ID3 tags is not supported.

**Online documentation**: https://kingg22.github.io/vibrion-id3/

**Disclaimer**: This library was inspired on [`browser-id3-writer` for JavaScript/Typescript](https://github.com/egoroof/browser-id3-writer).

A JavaScript library for writing ID3 tags in the browser.
While this implementation was built from scratch in Kotlin Multiplatform and does not reuse code from that project,
some implementation and core writer were adapted from it. Some tests use a tag built using it.
Full credit to [@egoroof](https://github.com/egoroof) for the original concept.

> [!NOTE]
>
> This library doesn't read, remove or scan byteArray, only generate the tag. You can use `Id3AudioWriter.removeTag(bytearray)`.
>
> This library works on byteArray, doesn't use files, stream, sink or other format. You can open an issue or PR to add a module.

## Installation
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kingg22/vibrion-id3)
```kotlin
implementation("io.github.kingg22:vibrion-id3:<current-version>")
```
```groovy
implementation 'io.github.kingg22:vibrion-id3:<current-version>'
```
```toml
[versions]
vibrion-id3 = "<current-version>"

[libraries]
vibrion-id3 = { group = "io.github.kingg22", name = "vibrion-id3", version.ref = "vibrion-id3" }
```
```xml
<dependency>
    <groupId>io.github.kingg22</groupId>
    <artifactId>vibrion-id3</artifactId>
    <version>current-version</version>
</dependency>
```

## Supported targets:
- Android ![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)
- JVM (**[Java compatible](https://kotlinlang.org/docs/java-to-kotlin-interop.html)**)
- Linux X64
- Linux Arm 64
- Mingw X64
- Android Native Arm 32
- Android Native Arm 64
- Android Native X64
- Android Native X86

_You required targets is not available?_ Please make a PR to add it or create an issue.

_You need an `Export` annotation for specific target?_ Please make a PR to add it or create an issue.

_Java inaccessible?_ Please create an issue with demonstration of the code. I try to make java-friendly.

## Samples of use
- Using verbose builder (work in progress):

  Kotlin:
  ```kotlin
  val track = "..." // origin of data
  val tag: ByteArray = Id3WriterBuilder.id3Writer {
      title = track.title
      artist(track.artist.name)
      // Null is not considered as value for frame tag, but is valid for builder style
      album = track.album?.title
      length = track.duration
      year = track.releaseDate?.year
      picture {
          // DSL marker
          type = AttachedPictureType.CoverFront
          data = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
      }
      syncLyrics {
        line("One line of the song", timestamp = 1000) // repeteable function
      }
  }.toByteArray()
  ```
  Java builder style:
  ```java
  final byte[] tag = Id3WriterBuilder.id3Writer()
      .title("Título")
      .artist("Artista")
      .picture(new AttachedPictureBuilder().type(AttachedPictureType.CoverFront).data(new byte[]{
          (byte) 0xFF, (byte) 0xD8, (byte) 0xFF
      }))
      .syncLyrics(new SynchronizedLyricsBuilder().line("a line", 1000).line("other line", 12343))
      .toByteArray();
  ```

- Using direct frames and data:

  Kotlin:
  ```kotlin
  val writer = Id3AudioWriter()
  writer[Id3v2v3TagFrame.TIT2] = "Title"
  writer[Id3v2v3TagFrame.TPE1] = listOf("Eminem", "50 Cent")
  // ⚠️ Advanced use only. May lead to invalid tag structure if not used carefully.
  writer["TXXX"] = "key" to "value"
  val tag: ByteArray = writer.build()
  ```
  Java:
  ```java
  class Id3BuilderJavaTest {
    void testBuilderStyle() {
      final var writer = new Id3AudioWriter();
      // Need to call `.INSTANCE` because is a kotlin object.
      writer.set(Id3v2v3TagFrame.TIT2.INSTANCE, "Title");
      writer.set(Id3v2v3TagFrame.TPE1.INSTANCE, List.of("Eminem", "50 Cent"));
      // ⚠️ Advanced use only. May lead to invalid tag structure if not used carefully.
      // Java consumers don't need to use kotlin Pair, can use Map thinking is only for pair key-value
      writer.set("TXXX", Map.of("key", value));
      final byte[] tag = writer.build();
      }
  }
  ```

## Supported frames

_A specific frame is not supported and you require it?_ Please open a PR or create an issue to try to deliver it.

_Grouped by type, where (`name`) is the type in code_

**Array of strings (`StringListFrame`):**

- TPE1 (song artists)
- TCOM (song composers)
- TCON (song genres)

**Pairs of string (`PairedTextFrame`):**

- IPLS (involved people list)

**String (`TextFrame`):**

- TLAN (language)
- TIT1 (content group description)
- TIT2 (song title)
- TIT3 (song subtitle)
- TALB (album title)
- TPE2 (album artist)
- TPE3 (conductor/performer refinement)
- TPE4 (interpreted, remixed, or otherwise modified by)
- TRCK (song number in album): '5' or '5/10'
- TPOS (album disc number): '1' or '1/3'
- TPUB (label name)
- TKEY (initial key)
- TMED (media type)
- TDAT (album release date expressed as 'DDMM')
- TSRC (isrc - international standard recording code)
- TSSE (software/hardware and settings used for encoding)
- TCOP (copyright message)
- TCMP (iTunes compilation flag)
- TEXT (lyricist / text writer)

**URL (`TextFrame`):**
- WCOM (commercial information)
- WCOP (copyright/Legal information)
- WOAF (official audio file webpage)
- WOAR (official artist/performer webpage)
- WOAS (official audio source webpage)
- WORS (official internet radio station homepage)
- WPAY (payment)
- WPUB (publishers official webpage)

**Integer (`IntegerFrame`):**

- TLEN (song duration in milliseconds)
- TYER (album release year)
- TBPM (beats per minute)

**Object:**

- COMM (`CommentFrame`) (comments)
- USLT (`UnsynchronisedLyrics`) (unsychronised lyrics)
- SYLT (`SynchronizedLyrics`) (synchronised lyrics)
- APIC (`AttachedPicture`) (attached picture)
- TXXX (`UserDefinedText`) (user defined text)
- PRIV (`PrivateFrame`) (private frame)

## APIC picture types (`AttachedPictureType`)

| Type | Name                                |
|------|-------------------------------------|
| 0    | Other                               |
| 1    | 32x32 pixels 'file icon' (PNG only) |
| 2    | Other file icon                     |
| 3    | Cover (front)                       |
| 4    | Cover (back)                        |
| 5    | Leaflet page                        |
| 6    | Media (e.g. label side of CD)       |
| 7    | Lead artist/lead performer/soloist  |
| 8    | Artist/performer                    |
| 9    | Conductor                           |
| 10   | Band/Orchestra                      |
| 11   | Composer                            |
| 12   | Lyricist/text writer                |
| 13   | Recording location                  |
| 14   | During recording                    |
| 15   | During performance                  |
| 16   | Movie/video screen capture          |
| 17   | A bright coloured fish              |
| 18   | Illustration                        |
| 19   | Band/artist logotype                |
| 20   | Publisher/Studio logotype           |

## SYLT content types (`SynchronizedLyricsType`)

| Type | Name                                         |
|------|----------------------------------------------|
| 0    | Other                                        |
| 1    | Lyrics                                       |
| 2    | Text transcription                           |
| 3    | Movement/part name (e.g. "Adagio")           |
| 4    | Events (e.g. "Don Quijote enters the stage") |
| 5    | Chord (e.g. "Bb F Fsus")                     |
| 6    | Trivia/'pop up' information                  |

## SYLT timestamp formats (`SynchronizedLyricsTimestampFormat`)

| Type | Name                                                    |
|------|---------------------------------------------------------|
| 1    | Absolute time, 32 bit sized, using MPEG frames as unit  |
| 2    | Absolute time, 32 bit sized, using milliseconds as unit |

## Roadmap

- [ ] Kotlin JS / iOS / WASM / WASI target
- [ ] Document all the API
- [ ] Use of contract API, still waiting [enable contract for operator set issue](https://youtrack.jetbrains.com/issue/KT-77175/Enable-contracts-for-operators-participating-in-augmented-assignment-desugaring).
- [ ] All frames of Id3 v2.3
- [ ] ID3 v2.4 support
- [ ] Maybe, read tags

### 📚 References

* [ID3v2.3 Official Spec](https://id3.org/id3v2.3.0)
* [`ID3v2.3 frames` TagLib](https://taglib.org/api/classTagLib_1_1ID3v2_1_1Frame.html)
* [ID3 on Wikipedia](https://en.wikipedia.org/wiki/ID3)

## License

This library is licensed under the MIT License. See [LICENSE](https://github.com/kingg22/vibrion-id3/blob/main/LICENSE.txt) for details.

## Contributing

Found a bug or missing frame? Contributions are welcome!

- Fork the project
- Create a feature branch
- Write tests if applicable
- Make a pull request

For larger changes (breaking changes, adding features), please open an issue first to discuss ideas.

## Testing

The library includes unit tests for core frames and encoding logic.
See the [tests folder](https://github.com/kingg22/vibrion-id3/tree/main/id3/src/commonTest/kotlin/io/github/kingg22/vibrion/id3) for examples.
Contributions with additional test coverage are welcome!
Contribution for specific Java interop are really welcome!
