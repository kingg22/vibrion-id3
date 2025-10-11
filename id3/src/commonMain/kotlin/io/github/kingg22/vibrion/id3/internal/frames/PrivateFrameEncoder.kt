@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-PrivateFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun PrivateFrameEncoder(id: String, value: ByteArray, size: Int): FrameEncoder =
    PrivateFrameEncoder(size, id, value)

private class PrivateFrameEncoder(size: Int, id: String, value: ByteArray) : FrameEncoder("PRIV", size) {
    override val encodedFrame: ByteArray by lazy {
        val idBytes = encodeWindows1252(id)
        val contentSize = idBytes.size + 1 + value.size
        val buffer = ByteArray(HEADER + contentSize)

        var currentOffset = writeFrameHeader(buffer)

        // Identifier + separator
        idBytes.copyInto(buffer, currentOffset)
        currentOffset += idBytes.size
        buffer[currentOffset++] = 0

        // Value
        value.copyInto(buffer, currentOffset)
    }
}
