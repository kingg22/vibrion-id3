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
data class UnsynchronisedLyrics @JvmOverloads constructor(
    val lyrics: String,
    val description: String = "",
    val language: String = "eng",
) : FrameValue() {
    init {
        languageFormat(language)
    }
}
