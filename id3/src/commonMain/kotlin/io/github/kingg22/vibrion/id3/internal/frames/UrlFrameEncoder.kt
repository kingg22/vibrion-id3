@file:JvmSynthetic
@file:JvmName("-UrlFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun UrlFrameEncoder(name: String, value: String, size: Int): FrameEncoder = UrlFrameEncoder(name, size, value)

private class UrlFrameEncoder(name: String, size: Int, private val value: String) : FrameEncoder(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val currentOffset = writeFrameHeader(buffer, offset)

        // Value (Windows-1252)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, currentOffset)

        return HEADER + encoded.size
    }
}
