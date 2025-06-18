package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.TXXX */
data class UserDefinedText(val description: String, val value: String) : FrameValue() {
    init {
        require(description.isNotBlank()) { "Description cannot be blank" }
        require(value.isNotBlank()) { "Value cannot be blank" }
    }
}
