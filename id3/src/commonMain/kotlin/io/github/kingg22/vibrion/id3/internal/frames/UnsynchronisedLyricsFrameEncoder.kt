@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-UnsynchronisedLyricsFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun UnsynchronisedLyricsFrameEncoder(
    language: String,
    description: String,
    value: String,
    size: Int,
): FrameEncoder = UnsynchronisedLyricsFrameEncoder(size, language, description, value)

private class UnsynchronisedLyricsFrameEncoder(size: Int, language: String, description: String, value: String) :
    FrameEncoder("USLT", size) {
    override val encodedFrame: ByteArray by lazy {
        val descriptionBytes = encodeUtf16LE(description)
        val valueBytes = encodeUtf16LE(value)
        val languageBytes = encodeWindows1252(language)

        val contentSize = 1 + languageBytes.size + BOM.size + descriptionBytes.size + 2 + BOM.size + valueBytes.size

        val buffer = ByteArray(HEADER + contentSize)
        var currentOffset = writeFrameHeader(buffer)

        // Encoding + language + BOM
        buffer[currentOffset++] = 1

        languageBytes.copyInto(buffer, currentOffset)
        currentOffset += languageBytes.size

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
    }
}
