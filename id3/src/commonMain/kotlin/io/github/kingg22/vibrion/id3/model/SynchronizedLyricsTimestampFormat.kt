package io.github.kingg22.vibrion.id3.model

/**
 * _For a more accurate description the tempo of a musical piece._
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.SYLT
 * @see SynchronizedLyrics
 * @see io.github.kingg22.vibrion.id3.Id3WriterBuilder.syncLyrics
 * @see io.github.kingg22.vibrion.id3.SynchronizedLyricsBuilder
 * @see <a href="https://id3.org/id3v2.3.0#Synchronised_tempo_codes">Id3v2.3.0 - Synchronised tempo codes</a>
 */
enum class SynchronizedLyricsTimestampFormat(val value: Int) {
    /** Absolute time, 32 bit sized, using MPEG frames as unit */
    Frames(0x01),

    /** Absolute time, 32 bit sized, using milliseconds as unit */
    Milliseconds(0x02),
}
