@file:JvmSynthetic
@file:JvmName("-SynchronisedLyricsFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class SynchronisedLyricsFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val value: List<Pair<String, Int>>,
    private val language: List<Byte>,
    private val description: String,
    private val type: Int,
    private val timestampFormat: Int,
) : FrameEncoder("SYLT", size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val descriptionBytes = encodeUtf16LE(description)
        val linesSize = value.sumOf {
            2 + encodeUtf16LE(it.first).size + 2 + 4 // BOM + text + separator + timestamp
        }
        val contentSize = 1 + 3 + 1 + 1 + BOM.size + descriptionBytes.size + 2 + linesSize

        var currentOffset = writeFrameHeader(buffer, offset)

        // Encoding + language + timestamp format + content type
        buffer[currentOffset++] = 1
        val languageBytes = language.toByteArray()
        languageBytes.copyInto(buffer, currentOffset)
        currentOffset += languageBytes.size
        buffer[currentOffset++] = timestampFormat.toByte()
        buffer[currentOffset++] = type.toByte()

        // BOM + description
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size
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

        return HEADER + contentSize
    }

    internal companion object {
        @JvmSynthetic
        internal fun SynchronisedLyricsFrameEncoder(
            value: List<Pair<String, Int>>,
            language: List<Byte>,
            description: String,
            type: Int,
            timestampFormat: Int,
            size: Int,
        ) = SynchronisedLyricsFrameEncoder(size, value, language, description, type, timestampFormat)
    }
}
