package io.github.kingg22.vibrion.id3.model

/**
 * String lists.
 *
 * _Requirements:_
 * - [values] **must not be empty**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.listFrames
 */
class StringListFrame(val values: List<String>) : FrameValue {
    constructor(vararg values: String) : this(values.toList())

    init {
        require(values.isNotEmpty()) { "List cannot be empty" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as StringListFrame

        return values == other.values
    }

    override fun hashCode(): Int = values.hashCode()

    override fun toString(): String = "StringListFrame(values=$values)"
}
