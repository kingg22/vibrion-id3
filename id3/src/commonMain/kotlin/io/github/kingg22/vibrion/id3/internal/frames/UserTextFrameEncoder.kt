@file:JvmSynthetic
@file:JvmName("-UserTextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * @see io.github.kingg22.vibrion.id3.model.UserDefinedText
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.TXXX
 */
@ConsistentCopyVisibility
internal data class UserTextFrameEncoder internal constructor(
    @JvmSynthetic override val name: String,
    @JvmSynthetic val description: String,
    @JvmSynthetic val value: String,
    @JvmSynthetic override val size: Int,
) : FrameEncoder(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val descriptionBytes = encodeUtf16LE(description)
        val valueBytes = encodeUtf16LE(value)
        val contentSize = 1 + BOM.size + descriptionBytes.size + 2 + BOM.size + valueBytes.size

        var currentOffset = writeFrameHeader(buffer, offset)

        // Encoding + BOM
        buffer[currentOffset++] = 1
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Description
        descriptionBytes.copyInto(buffer, currentOffset)
        currentOffset += descriptionBytes.size

        // Separator + BOM
        buffer[currentOffset++] = 0
        buffer[currentOffset++] = 0
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value
        valueBytes.copyInto(buffer, currentOffset)

        return offset + contentSize
    }
}
