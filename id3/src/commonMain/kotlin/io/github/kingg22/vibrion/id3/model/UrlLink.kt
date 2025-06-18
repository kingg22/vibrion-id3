package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2FrameType.Companion.urlFrames */
data class UrlLink(
    /** language: url */
    val url: String,
) : FrameValue()
// Maybe delete this?
