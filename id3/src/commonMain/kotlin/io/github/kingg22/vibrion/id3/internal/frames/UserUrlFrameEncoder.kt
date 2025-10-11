@file:JvmSynthetic
@file:JvmName("-UserUrlFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun UserUrlFrameEncoder(description: String, url: String, size: Int): FrameEncoder =
    UserUrlFrameEncoder(size, description, url)

private class UserUrlFrameEncoder(size: Int, description: String, url: String) : FrameEncoder("WXXX", size) {
    override val encodedFrame: ByteArray by lazy {

        val buffer = ByteArray(size)
        var currentOffset = writeFrameHeader(buffer)

        // 1. Encoding byte
        buffer[currentOffset] = 0x01 // UTF-16 with BOM
        currentOffset += 1

        // 2. BOM
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // 3. Description (UTF-16LE) + null terminator (0x00 0x00)
        val encodedDescription = encodeUtf16LE(description)
        encodedDescription.copyInto(buffer, currentOffset)
        currentOffset += encodedDescription.size

        buffer[currentOffset] = 0x00 // null terminator UTF-16
        buffer[currentOffset + 1] = 0x00
        currentOffset += 2

        // 4. URL (Windows-1252)
        val encodedUrl = encodeWindows1252(url)
        encodedUrl.copyInto(buffer, currentOffset)
    }
}
