@file:JvmSynthetic
@file:JvmName("-UnsynchronisedLyricsFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class UnsynchronisedLyricsFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val language: List<Byte>,
    private val description: String,
    private val value: String,
) : FrameEncoder("USLT", size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val descriptionBytes = encodeUtf16LE(description)
        val valueBytes = encodeUtf16LE(value)
        val contentSize = 1 + 3 + BOM.size + descriptionBytes.size + 2 + BOM.size + valueBytes.size

        var currentOffset = writeFrameHeader(buffer, offset)

        // Encoding + language + BOM
        buffer[currentOffset++] = 1
        language.forEach { byte -> buffer[currentOffset++] = byte }
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Description
        descriptionBytes.copyInto(buffer, currentOffset)
        currentOffset += descriptionBytes.size

        // Separator + BOM
        buffer[currentOffset++] = 0
        buffer[currentOffset++] = 0
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value
        valueBytes.copyInto(buffer, currentOffset)

        return HEADER + contentSize
    }

    internal companion object {
        @JvmSynthetic
        internal fun UnsynchronisedLyricsFrameEncoder(
            language: List<Byte>,
            description: String,
            value: String,
            size: Int,
        ) = UnsynchronisedLyricsFrameEncoder(size, language, description, value)
    }
}
