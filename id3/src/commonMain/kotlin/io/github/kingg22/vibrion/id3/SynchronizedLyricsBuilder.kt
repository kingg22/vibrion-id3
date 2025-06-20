package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.model.SynchronizedLyrics
import io.github.kingg22.vibrion.id3.model.SynchronizedLyricsTimestampFormat
import io.github.kingg22.vibrion.id3.model.SynchronizedLyricsType

/** Builder for [SynchronizedLyrics] */
class SynchronizedLyricsBuilder {
    var type = SynchronizedLyricsType.Lyrics
    var timestampFormat = SynchronizedLyricsTimestampFormat.Milliseconds
    var language: String = "eng"
    var description: String = ""
    private val lines = mutableListOf<Pair<String, Int>>()

    fun type(type: SynchronizedLyricsType) = apply { this.type = type }
    fun timestampFormat(format: SynchronizedLyricsTimestampFormat) = apply { this.timestampFormat = format }
    fun language(language: String) = apply { this.language = language }
    fun description(description: String) = apply { this.description = description }
    fun build() = SynchronizedLyrics(type, timestampFormat, lines, language, description)

    /**
     * Add a line to the lyrics. _Repeatable_.
     */
    fun line(text: String, timestamp: Int) = apply {
        lines += text to timestamp
    }
}
