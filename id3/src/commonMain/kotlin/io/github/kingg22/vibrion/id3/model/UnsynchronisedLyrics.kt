package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/**
 * Unsynchronised lyrics.
 *
 * _Requirements:_
 * - Language **must be in ISO 639-2 format**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.USLT
 */
class UnsynchronisedLyrics @JvmOverloads constructor(
    val lyrics: String,
    val description: String = "",
    val language: String = "eng",
) : FrameValue {
    init {
        FrameValue.languageFormat(language)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UnsynchronisedLyrics

        if (lyrics != other.lyrics) return false
        if (description != other.description) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lyrics.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + language.hashCode()
        return result
    }

    override fun toString(): String =
        "UnsynchronisedLyrics(lyrics='$lyrics', description='$description', language='$language')"
}
