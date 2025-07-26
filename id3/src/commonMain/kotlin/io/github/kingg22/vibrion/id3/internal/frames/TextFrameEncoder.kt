@file:JvmSynthetic
@file:JvmName("-TextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class TextFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val name: String,
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val value: String,
) : FrameEncoder(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        var currentOffset = writeFrameHeader(buffer, offset)

        // Encoding (UTF-16 with BOM)
        buffer[currentOffset++] = 1
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value (UTF-16LE)
        val encoded = encodeUtf16LE(value)
        encoded.copyInto(buffer, currentOffset)

        return HEADER + 1 + BOM.size + encoded.size
    }

    internal companion object {
        @JvmSynthetic
        internal fun TextFrameEncoder(name: String, value: String, size: Int) = TextFrameEncoder(name, size, value)
    }
}
