@file:JvmSynthetic
@file:JvmName("-TextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class TextFrameEncoder internal constructor(
    @JvmSynthetic override val name: String,
    @JvmSynthetic val value: String,
    @JvmSynthetic override val size: Int,
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
}
