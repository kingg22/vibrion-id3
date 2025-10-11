@file:JvmSynthetic
@file:JvmName("-PictureFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun PictureFrameEncoder(
    value: ByteArray,
    mimeType: String,
    pictureType: Int,
    description: String,
    useUnicode: Boolean,
    size: Int,
): FrameEncoder = PictureFrameEncoder(size, mimeType, pictureType.toByte(), description, value, useUnicode)

private class PictureFrameEncoder(
    size: Int,
    mimeType: String,
    pictureType: Byte,
    description: String,
    imageData: ByteArray,
    useUnicode: Boolean,
) : FrameEncoder("APIC", size) {
    override val encodedFrame: ByteArray by lazy {

        val buffer = ByteArray(size)
        var offset = writeFrameHeader(buffer)

        // Encoding
        val encodingByte = if (useUnicode) 1 else 0
        buffer[offset++] = encodingByte.toByte()

        // MIME
        val mimeBytes = encodeWindows1252(mimeType)
        mimeBytes.copyInto(buffer, offset)
        offset += mimeBytes.size
        buffer[offset++] = 0

        // Picture type
        buffer[offset++] = pictureType

        // Description
        if (useUnicode) {
            BOM.copyInto(buffer, offset)
            offset += BOM.size
        }

        val descriptionBytes = if (useUnicode) encodeUtf16LE(description) else encodeWindows1252(description)
        descriptionBytes.copyInto(buffer, offset)
        offset += descriptionBytes.size

        // Null terminator
        val terminatorSize = if (useUnicode) 2 else 1
        repeat(terminatorSize) { buffer[offset++] = 0 }

        // Image data
        imageData.copyInto(buffer, offset)
    }
}
