@file:JvmSynthetic
@file:JvmName("-UrlFrame")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class UrlFrame internal constructor(
    @JvmSynthetic override val name: String,
    @JvmSynthetic val value: String,
    @JvmSynthetic override val size: Int,
) : Frame(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val currentOffset = writeFrameHeader(buffer, offset)

        // Value (Windows-1252)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, currentOffset)

        return offset + encoded.size
    }
}
