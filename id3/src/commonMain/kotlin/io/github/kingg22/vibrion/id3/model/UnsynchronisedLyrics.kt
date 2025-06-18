package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/** @see io.github.kingg22.vibrion.id3.Id3v2FrameType.USLT */
data class UnsynchronisedLyrics @JvmOverloads constructor(
    val lyrics: String,
    val description: String = "",
    val language: String = "eng",
) : FrameValue() {
    init {
        languageFormat(language)
    }
}
