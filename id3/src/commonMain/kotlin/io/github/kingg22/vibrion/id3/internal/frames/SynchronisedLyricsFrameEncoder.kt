@file:JvmSynthetic
@file:JvmName("-SynchronisedLyricsFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun SynchronisedLyricsFrameEncoder(
    value: List<Pair<String, Int>>,
    language: String,
    description: String,
    type: Int,
    timestampFormat: Int,
    size: Int,
): FrameEncoder = SynchronisedLyricsFrameEncoder(size, value, language, description, type, timestampFormat)

private class SynchronisedLyricsFrameEncoder(
    size: Int,
    value: List<Pair<String, Int>>,
    language: String,
    description: String,
    type: Int,
    timestampFormat: Int,
) : FrameEncoder("SYLT", size) {
    override val encodedFrame: ByteArray by lazy {
        val buffer = ByteArray(size)
        var currentOffset = writeFrameHeader(buffer)

        // Encoding + language + timestamp format + content type
        buffer[currentOffset++] = 1

        val languageBytes = encodeWindows1252(language)
        languageBytes.copyInto(buffer, currentOffset)
        currentOffset += languageBytes.size

        buffer[currentOffset++] = timestampFormat.toByte()

        buffer[currentOffset++] = type.toByte()

        // BOM + description
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        val descriptionBytes = encodeUtf16LE(description)
        descriptionBytes.copyInto(buffer, currentOffset)
        currentOffset += descriptionBytes.size

        // Separator
        buffer[currentOffset++] = 0
        buffer[currentOffset++] = 0

        // Lyrics lines
        value.forEach { (lyric, timestamp) ->
            // BOM + text
            BOM.copyInto(buffer, currentOffset)
            currentOffset += BOM.size
            val textBytes = encodeUtf16LE(lyric)
            textBytes.copyInto(buffer, currentOffset)
            currentOffset += textBytes.size

            // Separator + timestamp
            buffer[currentOffset++] = 0
            buffer[currentOffset++] = 0
            val timeBytes = uint32ToUint8Array(timestamp.toUInt())
            timeBytes.copyInto(buffer, currentOffset)
            currentOffset += timeBytes.size
        }

        buffer
    }
}
