@file:JvmSynthetic
@file:JvmName("-PrivateFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.KoverIgnore
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class PrivateFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val id: String,
    private val value: ByteArray,
) : FrameEncoder("PRIV", size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val idBytes = encodeWindows1252(id)
        val contentSize = idBytes.size + 1 + value.size

        var currentOffset = writeFrameHeader(buffer, offset)

        // Identifier + separator
        idBytes.copyInto(buffer, currentOffset)
        currentOffset += idBytes.size
        buffer[currentOffset++] = 0

        // Value
        value.copyInto(buffer, currentOffset)

        return HEADER + contentSize
    }

    @JvmSynthetic
    @KoverIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PrivateFrameEncoder

        if (size != other.size) return false
        if (name != other.name) return false
        if (id != other.id) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    @JvmSynthetic
    @KoverIgnore
    override fun hashCode(): Int {
        var result = size
        result = 31 * result + name.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + value.contentHashCode()
        return result
    }

    internal companion object {
        @JvmSynthetic
        internal fun PrivateFrameEncoder(id: String, value: ByteArray, size: Int) = PrivateFrameEncoder(size, id, value)
    }
}
