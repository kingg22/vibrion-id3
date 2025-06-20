package io.github.kingg22.vibrion.id3.model

/**
 * String lists.
 *
 * _Requirements:_
 * - [values] **must not be empty**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.listFrames
 */
data class StringListFrame(val values: List<String>) : FrameValue() {
    constructor(vararg values: String) : this(values.toList())

    init {
        require(values.isNotEmpty()) { "List cannot be empty" }
    }
}
