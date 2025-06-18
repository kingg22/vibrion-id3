@file:JvmSynthetic
@file:JvmName("-Frame")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

internal sealed class Frame(@JvmSynthetic open val name: String, @JvmSynthetic open val size: Int) {
    @JvmSynthetic
    internal abstract fun writeTo(buffer: ByteArray, offset: Int): Int

    protected fun writeFrameHeader(buffer: ByteArray, offset: Int): Int {
        // Frame ID (4 bytes)
        val nameBytes = encodeWindows1252(name)
        nameBytes.copyInto(buffer, offset)

        // Content size (4 bytes, big-endian)
        val frameSizeBytes = uint32ToUint8Array(size - 10) // without header
        frameSizeBytes.copyInto(buffer, offset + 4)

        // Flags (2 bytes)
        buffer[offset + 8] = 0
        buffer[offset + 9] = 0

        return offset + 10
    }

    companion object {
        @JvmStatic
        @JvmSynthetic
        protected val BOM = byteArrayOf(0xFF.toByte(), 0xFE.toByte())
    }
}
