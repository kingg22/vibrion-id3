@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-UserTextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun UserTextFrameEncoder(description: String, value: String, size: Int): FrameEncoder =
    UserTextFrameEncoder(size, description, value)

/**
 * @see io.github.kingg22.vibrion.id3.model.UserDefinedText
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.TXXX
 */
private class UserTextFrameEncoder(size: Int, description: String, value: String) : FrameEncoder("TXXX", size) {
    override val encodedFrame: ByteArray by lazy {
        val descriptionBytes = encodeUtf16LE(description)
        val valueBytes = encodeUtf16LE(value)
        val contentSize = 1 + BOM.size + descriptionBytes.size + 2 + BOM.size + valueBytes.size

        val buffer = ByteArray(HEADER + contentSize)
        var currentOffset = writeFrameHeader(buffer)

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
    }
}
