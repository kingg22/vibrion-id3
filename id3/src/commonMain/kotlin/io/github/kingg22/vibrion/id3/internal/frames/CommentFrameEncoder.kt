@file:JvmSynthetic
@file:JvmName("-CommentFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun CommentFrameEncoder(language: String, description: String, value: String, size: Int): FrameEncoder =
    CommentFrameEncoder(size, language, description, value)

private class CommentFrameEncoder(size: Int, language: String, description: String, value: String) :
    FrameEncoder("COMM", size) {

    override val encodedFrame: ByteArray by lazy {
        val buffer = ByteArray(size)
        var currentOffset = writeFrameHeader(buffer)

        // Encoding + language + BOM
        buffer[currentOffset++] = 1

        val languageBytes = encodeWindows1252(language)
        languageBytes.copyInto(buffer, currentOffset)
        currentOffset += languageBytes.size

        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Description
        val descriptionBytes = encodeUtf16LE(description)
        descriptionBytes.copyInto(buffer, currentOffset)
        currentOffset += descriptionBytes.size

        // Separator + BOM
        buffer[currentOffset++] = 0
        buffer[currentOffset++] = 0
        BOM.copyInto(buffer, currentOffset)
        currentOffset += BOM.size

        // Value
        val valueBytes = encodeUtf16LE(value)
        valueBytes.copyInto(buffer, currentOffset)
    }
}
