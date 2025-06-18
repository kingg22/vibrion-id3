package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2FrameType.Companion.listFrames */
data class StringListFrame(val values: List<String>) : FrameValue() {
    constructor(vararg values: String) : this(values.toList())
}
