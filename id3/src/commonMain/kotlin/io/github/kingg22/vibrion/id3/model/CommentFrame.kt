package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/** @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.COMM */
data class CommentFrame @JvmOverloads constructor(
    val description: String,
    val text: String,
    val language: String = "eng",
) : FrameValue() {
    init {
        languageFormat(language)
    }
}
