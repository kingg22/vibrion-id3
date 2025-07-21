package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.model.UnsynchronisedLyrics
import kotlin.jvm.JvmSynthetic

/**
 * Builder for [UnsynchronisedLyrics].
 *
 * **Is required** [UnsynchronisedLyricsBuilder.lyrics]
 */
class UnsynchronisedLyricsBuilder {
    lateinit var lyrics: String
    var description: String = ""
    var language: String = "eng"

    fun lyrics(lyrics: String) = apply { this.lyrics = lyrics }
    fun description(description: String) = apply { this.description = description }
    fun language(language: String) = apply { this.language = language }
    fun build() = UnsynchronisedLyrics(lyrics, description, language)

    companion object {
        /** DSL Builder for [UnsynchronisedLyrics] */
        @JvmSynthetic
        fun builder(block: UnsynchronisedLyricsBuilder.() -> Unit) = UnsynchronisedLyricsBuilder().apply(block).build()
    }
}
