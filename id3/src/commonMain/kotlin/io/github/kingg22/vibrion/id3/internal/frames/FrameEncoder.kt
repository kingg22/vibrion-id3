package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

internal abstract class FrameEncoder protected constructor(
    name: String,
    @get:JvmSynthetic @field:JvmSynthetic internal val size: Int,
) {
    private val encodedFrameHeader: ByteArray by lazy {
        val buffer = ByteArray(10)
        // FrameEncoder ID (4 bytes)
        val nameBytes = encodeWindows1252(name)
        nameBytes.copyInto(buffer, 0)

        // Content size (4 bytes, big-endian)
        val frameSizeBytes = uint32ToUint8Array(size - 10)
        frameSizeBytes.copyInto(buffer, 4)

        // Flags (2 bytes)
        buffer[8] = 0
        buffer[9] = 0

        buffer
    }

    /** The encoded frame data. Needs to be computed lazily to avoid unnecessary computation. */
    @get:JvmSynthetic
    protected abstract val encodedFrame: ByteArray

    /**
     * Writes the encoded frame data to the specified byte array buffer starting at the provided offset.
     *
     * @param buffer The byte array buffer where the encoded frame data will be written.
     * @param offset The starting position in the buffer from where the frame data should be written.
     * @return The size of the written frame data.
     */
    @JvmSynthetic
    internal fun writeTo(buffer: ByteArray, offset: Int): Int {
        encodedFrame.copyInto(buffer, offset)
        return encodedFrame.size
    }

    /**
     * Writes the frame header to the given byte array buffer at the specified offset.
     * The header includes the frame identifier, content size, and flags.
     *
     * @param buffer The byte array buffer where the frame header will be written.
     * @param offset The starting position in the buffer from where the frame header should be written.
     * @return The updated offset after writing the frame header.
     */
    protected fun writeFrameHeader(buffer: ByteArray): Int {
        encodedFrameHeader.copyInto(buffer, 0)
        return 10
    }

    protected companion object {
        @JvmField
        protected val BOM = byteArrayOf(0xFF.toByte(), 0xFE.toByte())
    }
}
