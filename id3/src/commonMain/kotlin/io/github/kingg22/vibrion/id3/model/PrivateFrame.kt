package io.github.kingg22.vibrion.id3.model

/** @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.PRIV */
data class PrivateFrame(val id: String, val data: ByteArray) : FrameValue() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PrivateFrame

        if (id != other.id) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
