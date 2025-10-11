@file:JvmSynthetic
@file:JvmName("-PrivateFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun PrivateFrameEncoder(id: String, value: ByteArray, size: Int): FrameEncoder =
    PrivateFrameEncoder(size, id, value)

private class PrivateFrameEncoder(size: Int, private val id: String, private val value: ByteArray) :
    FrameEncoder("PRIV", size) {

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
}
