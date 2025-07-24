@file:JvmSynthetic
@file:JvmName("-UrlFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class UrlFrameEncoder internal constructor(
    @get:JvmSynthetic override val name: String,
    @get:JvmSynthetic val value: String,
    @get:JvmSynthetic override val size: Int,
) : FrameEncoder(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val currentOffset = writeFrameHeader(buffer, offset)

        // Value (Windows-1252)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, currentOffset)

        return HEADER + encoded.size
    }
}
