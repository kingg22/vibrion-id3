package io.github.kingg22.vibrion.id3.model

/**
 * Paired text.
 *
 * _Requirements:_
 * - [pairs] **must not be empty**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.IPLS
 */
data class PairedTextFrame(val pairs: List<Pair<String, String>>) : FrameValue() {
    constructor(vararg pairs: Pair<String, String>) : this(pairs.toList())
    constructor(pairs: Map<String, String>) : this(pairs.map { it.key to it.value })

    init {
        require(pairs.isNotEmpty()) { "List cannot be empty" }
    }
}
