@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-UserUrlFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun UserUrlFrameEncoder(description: String, url: String, size: Int): FrameEncoder =
    UserUrlFrameEncoder(size, description, url)

private class UserUrlFrameEncoder(size: Int, description: String, url: String) : FrameEncoder("WXXX", size) {
    override val encodedFrame: ByteArray by lazy {
        val encodedDescription = description.encodeUtf16LE()
        val encodedUrl = url.encodeWindows1252()

        val buffer = ByteArray(HEADER + 1 + BOM.size + encodedDescription.size + 2 + encodedUrl.size)
        var currentOffset = writeFrameHeader(buffer)

        // 1. Encoding byte
        buffer[currentOffset] = 0x01 // UTF-16 with BOM
        currentOffset += 1

        // 2. BOM
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // 3. Description (UTF-16LE) + null terminator (0x00 0x00)
        encodedDescription.copyInto(buffer, currentOffset)
        currentOffset += encodedDescription.size

        buffer[currentOffset] = 0x00 // null terminator UTF-16
        buffer[currentOffset + 1] = 0x00
        currentOffset += 2

        // 4. URL (Windows-1252)
        encodedUrl.copyInto(buffer, currentOffset)
    }
}
