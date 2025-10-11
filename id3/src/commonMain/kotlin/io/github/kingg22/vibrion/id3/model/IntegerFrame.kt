package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.integerFrames */
class IntegerFrame(val value: Int) : FrameValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IntegerFrame

        return value == other.value
    }

    override fun hashCode(): Int = value

    override fun toString(): String = "IntegerFrame(value=$value)"
}
