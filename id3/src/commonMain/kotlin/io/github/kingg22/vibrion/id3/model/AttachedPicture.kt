package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/** @see io.github.kingg22.vibrion.id3.Id3v2FrameType.APIC */
data class AttachedPicture @JvmOverloads constructor(
    val type: Int,
    val data: ByteArray,
    val description: String = "",
    val useUnicodeEncoding: Boolean = true,
) : FrameValue() {
    @JvmOverloads
    constructor(
        type: AttachedPictureType,
        data: ByteArray,
        description: String = "",
        useUnicodeEncoding: Boolean = true,
    ) : this(
        type.value,
        data,
        description,
        useUnicodeEncoding,
    )

    init {
        require(type in 0..20) { "Incorrect picture type" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AttachedPicture

        if (type != other.type) return false
        if (useUnicodeEncoding != other.useUnicodeEncoding) return false
        if (!data.contentEquals(other.data)) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + useUnicodeEncoding.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}
