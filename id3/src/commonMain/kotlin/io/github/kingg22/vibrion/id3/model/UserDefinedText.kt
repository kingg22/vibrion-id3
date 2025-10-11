package io.github.kingg22.vibrion.id3.model

/**
 * User defined text frames.
 *
 * _Requirements:_
 * - Description **must not be blank**.
 * - Value **must not be blank**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.TXXX
 */
class UserDefinedText(val description: String, val value: String) : FrameValue {
    init {
        require(description.isNotBlank()) { "Description cannot be blank" }
        require(value.isNotBlank()) { "Value cannot be blank" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserDefinedText

        if (description != other.description) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString(): String = "UserDefinedText(description='$description', value='$value')"
}
