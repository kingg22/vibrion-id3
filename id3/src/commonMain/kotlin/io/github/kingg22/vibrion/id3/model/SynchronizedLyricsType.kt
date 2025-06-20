package io.github.kingg22.vibrion.id3.model

/**
 * _There may be more than one "SYLT" frame in each tag, but only one with the same language and content descriptor._
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.SYLT
 * @see SynchronizedLyrics
 * @see io.github.kingg22.vibrion.id3.Id3WriterBuilder.syncLyrics
 * @see io.github.kingg22.vibrion.id3.SynchronizedLyricsBuilder
 * @see <a href="https://id3.org/id3v2.3.0#Synchronised_lyrics.2Ftext">Id3v2.3.0 - Synchronised lyrics/text</a>
 */
enum class SynchronizedLyricsType(val value: Int) {
    /** Other */
    Other(0x00),

    /** Lyrics */
    Lyrics(0x01),

    /** Text transcription */
    TextTranscription(0x02),

    /** Movement/part name (e.g. "Adagio") */
    MovementPartName(0x03),

    /** Events (e.g. "Don Quijote enters the stage") */
    Events(0x04),

    /** Chord (e.g. "Bb F Fsus") */
    Chord(0x05),

    /** Trivia/'pop up' information */
    Trivia(0x06),
}
