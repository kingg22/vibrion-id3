package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/**
 * Comment frame.
 *
 * _Requirements:_
 * - Language **must be in ISO 639-2 format**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.COMM
 */
class CommentFrame @JvmOverloads constructor(val description: String, val text: String, val language: String = "eng") :
    FrameValue {
    init {
        FrameValue.languageFormat(language)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CommentFrame

        if (description != other.description) return false
        if (text != other.text) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + language.hashCode()
        return result
    }

    override fun toString(): String = "CommentFrame(description='$description', text='$text', language='$language')"
}
