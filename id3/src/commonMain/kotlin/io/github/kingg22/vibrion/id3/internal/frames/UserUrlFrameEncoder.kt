package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class UserUrlFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val description: String,
    private val url: String,
) : FrameEncoder("WXXX", size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        var currentOffset = writeFrameHeader(buffer, offset)

        // 1. Encoding byte
        buffer[currentOffset] = 0x01 // UTF-16 with BOM
        currentOffset += 1

        // 2. BOM
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // 3. Description (UTF-16LE) + null terminator (0x00 0x00)
        val encodedDescription = description.encodeUtf16LE()
        encodedDescription.copyInto(buffer, currentOffset)
        currentOffset += encodedDescription.size

        buffer[currentOffset] = 0x00 // null terminator UTF-16
        buffer[currentOffset + 1] = 0x00
        currentOffset += 2

        // 4. URL (Windows-1252)
        val encodedUrl = url.encodeWindows1252()
        encodedUrl.copyInto(buffer, currentOffset)
        currentOffset += encodedUrl.size

        return currentOffset
    }

    internal companion object {
        @JvmSynthetic
        internal fun UserUrlFrameEncoder(description: String, url: String, size: Int) =
            UserUrlFrameEncoder(size, description, url)
    }
}
