@file:JvmSynthetic
@file:JvmName("-PictureFrame")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class PictureFrame internal constructor(
    @JvmSynthetic override val name: String,
    @JvmSynthetic val value: ByteArray,
    @JvmSynthetic val pictureType: Int,
    @JvmSynthetic val mimeType: String,
    @JvmSynthetic val description: String,
    @JvmSynthetic val useUnicode: Boolean,
    @JvmSynthetic override val size: Int,
) : Frame(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val descriptionBytes = if (useUnicode) {
            encodeUtf16LE(description)
        } else {
            encodeWindows1252(description)
        }

        val contentSize = 1 + mimeType.length + 1 + 1 + descriptionBytes.size +
            (if (useUnicode) 2 else 0) + 1 + value.size

        var currentOffset = writeFrameHeader(buffer, offset)

        // Encoding
        val encoding = if (useUnicode) 1 else 0
        buffer[currentOffset++] = encoding.toByte()

        // MIME type
        encodeWindows1252(mimeType).copyInto(buffer, currentOffset)
        currentOffset += mimeType.length

        // PictureFrame type and null separator
        buffer[currentOffset++] = 0
        buffer[currentOffset++] = pictureType.toByte()

        // Description
        if (useUnicode) {
            BOM.copyInto(buffer, currentOffset)
            currentOffset += BOM.size
        }
        descriptionBytes.copyInto(buffer, currentOffset)
        currentOffset += descriptionBytes.size

        // Null separator
        buffer[currentOffset++] = 0
        if (useUnicode) buffer[currentOffset++] = 0

        // PictureFrame data
        value.copyInto(buffer, currentOffset)

        return offset + contentSize
    }

    @JvmSynthetic
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PictureFrame

        if (pictureType != other.pictureType) return false
        if (useUnicode != other.useUnicode) return false
        if (size != other.size) return false
        if (name != other.name) return false
        if (!value.contentEquals(other.value)) return false
        if (mimeType != other.mimeType) return false
        if (description != other.description) return false

        return true
    }

    @JvmSynthetic
    override fun hashCode(): Int {
        var result = pictureType
        result = 31 * result + useUnicode.hashCode()
        result = 31 * result + size
        result = 31 * result + name.hashCode()
        result = 31 * result + value.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}
