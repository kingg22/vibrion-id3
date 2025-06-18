package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2FrameType.IPLS */
data class PairedTextFrame(val pairs: List<Pair<String, String>>) : FrameValue() {
    constructor(vararg pairs: Pair<String, String>) : this(pairs.toList())
    constructor(vararg pairs: Map<String, String>) : this(pairs.toList().flatMap { it.toList() })
}
