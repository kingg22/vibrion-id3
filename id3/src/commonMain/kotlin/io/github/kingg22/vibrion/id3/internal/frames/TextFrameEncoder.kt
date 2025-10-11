@file:JvmSynthetic
@file:JvmName("-TextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun TextFrameEncoder(name: String, value: String, size: Int): FrameEncoder =
    TextFrameEncoder(name, size, value)

private class TextFrameEncoder(name: String, size: Int, value: String) : FrameEncoder(name, size) {
    override val encodedFrame: ByteArray by lazy {
        val buffer = ByteArray(size)
        var currentOffset = writeFrameHeader(buffer)

        // Encoding (UTF-16 with BOM)
        buffer[currentOffset++] = 1
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value (UTF-16LE)
        val encoded = encodeUtf16LE(value)
        encoded.copyInto(buffer, currentOffset)
    }
}
