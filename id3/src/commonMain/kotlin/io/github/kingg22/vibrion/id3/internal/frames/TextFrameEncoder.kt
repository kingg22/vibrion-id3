@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-TextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun TextFrameEncoder(name: String, value: String, size: Int): FrameEncoder =
    TextFrameEncoder(name, size, value)

private class TextFrameEncoder(name: String, size: Int, value: String) : FrameEncoder(name, size) {
    override val encodedFrame: ByteArray by lazy {
        val encoded = encodeUtf16LE(value)

        val buffer = ByteArray(HEADER + 1 + BOM.size + encoded.size)
        var currentOffset = writeFrameHeader(buffer)

        // Encoding (UTF-16 with BOM)
        buffer[currentOffset++] = 1
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value (UTF-16LE)
        encoded.copyInto(buffer, currentOffset)
    }
}
