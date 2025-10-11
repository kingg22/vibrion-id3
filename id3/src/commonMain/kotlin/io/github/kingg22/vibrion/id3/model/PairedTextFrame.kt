package io.github.kingg22.vibrion.id3.model

/**
 * Paired text.
 *
 * _Requirements:_
 * - [pairs] **must not be empty**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.IPLS
 */
class PairedTextFrame(val pairs: List<Pair<String, String>>) : FrameValue {
    constructor(vararg pairs: Pair<String, String>) : this(pairs.toList())
    constructor(pairs: Map<String, String>) : this(pairs.map { it.key to it.value })

    init {
        require(pairs.isNotEmpty()) { "List cannot be empty" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PairedTextFrame

        return pairs == other.pairs
    }

    override fun hashCode(): Int = pairs.hashCode()

    override fun toString(): String = "PairedTextFrame(pairs=$pairs)"
}
