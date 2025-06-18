@file:JvmSynthetic
@file:JvmName("-Frame")

package io.github.kingg22.vibrion.id3

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

internal sealed class Frame(@JvmSynthetic open val name: String, @JvmSynthetic open val size: Int) {
    @JvmSynthetic
    internal abstract fun writeTo(buffer: ByteArray, offset: Int): Int

    companion object {
        private val BOM = byteArrayOf(0xFF.toByte(), 0xFE.toByte())
    }

    @JvmSynthetic
    protected fun writeFrameHeader(buffer: ByteArray, offset: Int): Int {
        // Frame ID (4 bytes)
        val nameBytes = encodeWindows1252(name)
        nameBytes.copyInto(buffer, offset)

        // Content size (4 bytes, big-endian)
        val frameSizeBytes = uint32ToUint8Array(size - 10) // without header
        frameSizeBytes.copyInto(buffer, offset + 4)

        // Flags (2 bytes)
        buffer[offset + 8] = 0
        buffer[offset + 9] = 0

        return offset + 10
    }

    data class Text(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            var currentOffset = writeFrameHeader(buffer, offset)

            // Encoding (UTF-16 with BOM)
            buffer[currentOffset++] = 1
            BOM.copyInto(buffer, currentOffset)
            currentOffset += BOM.size

            // Value (UTF-16LE)
            val encoded = encodeUtf16LE(value)
            encoded.copyInto(buffer, currentOffset)

            return offset + 1 + BOM.size + encoded.size
        }
    }

    data class Numeric(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        constructor(name: String, value: Int, size: Int) : this(name, value.toString(), size)

        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            // Escribir header del frame (10 bytes)
            // 1. Frame ID (4 bytes)
            name.forEachIndexed { i, char ->
                buffer[offset + i] = char.code.toByte()
            }

            // 2. Tamaño del contenido (4 bytes, big-endian)
            val contentSize = 1 + value.length // 1 byte (encoding) + datos
            buffer[offset + 4] = (contentSize shr 24).toByte()
            buffer[offset + 5] = (contentSize shr 16).toByte()
            buffer[offset + 6] = (contentSize shr 8).toByte()
            buffer[offset + 7] = contentSize.toByte()

            // 3. Flags (2 bytes)
            buffer[offset + 8] = 0
            buffer[offset + 9] = 0

            // Escribir contenido del frame
            buffer[offset + 10] = 0 // Encoding (0 = ISO-8859-1)
            val encoded = encodeWindows1252(value)
            encoded.copyInto(buffer, offset + 11)

            return 10 + contentSize // Tamaño total del frame
        }
    }

    data class Url(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            val currentOffset = writeFrameHeader(buffer, offset)

            // Value (Windows-1252)
            val encoded = encodeWindows1252(value)
            encoded.copyInto(buffer, currentOffset)

            return offset + encoded.size
        }
    }

    data class Picture(
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

            // Picture type and null separator
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

            // Picture data
            value.copyInto(buffer, currentOffset)

            return offset + contentSize
        }

        @JvmSynthetic
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Picture

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

    data class Comment(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val language: List<Byte>,
        @JvmSynthetic val description: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
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

            return offset + contentSize
        }
    }

    /**
     * @see FrameValue.UserDefinedText
     * @see ID3FrameType.TXXX
     */
    data class UserText(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val description: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            val descriptionBytes = encodeUtf16LE(description)
            val valueBytes = encodeUtf16LE(value)
            val contentSize = 1 + BOM.size + descriptionBytes.size + 2 + BOM.size + valueBytes.size

            var currentOffset = writeFrameHeader(buffer, offset)

            // Encoding + BOM
            buffer[currentOffset++] = 1
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

            return offset + contentSize
        }
    }

    data class UnsynchronisedLyrics(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val language: List<Byte>,
        @JvmSynthetic val description: String,
        @JvmSynthetic val value: String,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
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

            return offset + contentSize
        }
    }

    data class SynchronisedLyrics(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val value: List<Pair<String, Int>>,
        @JvmSynthetic val language: List<Byte>,
        @JvmSynthetic val description: String,
        @JvmSynthetic val type: Int,
        @JvmSynthetic val timestampFormat: Int,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
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

            return offset + contentSize
        }
    }

    data class Private(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val id: String,
        @JvmSynthetic val value: ByteArray,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            val idBytes = encodeWindows1252(id)
            val contentSize = idBytes.size + 1 + value.size

            var currentOffset = writeFrameHeader(buffer, offset)

            // Identifier + separator
            idBytes.copyInto(buffer, currentOffset)
            currentOffset += idBytes.size
            buffer[currentOffset++] = 0

            // Value
            value.copyInto(buffer, currentOffset)

            return offset + contentSize
        }

        @JvmSynthetic
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Private

            if (size != other.size) return false
            if (name != other.name) return false
            if (id != other.id) return false
            if (!value.contentEquals(other.value)) return false

            return true
        }

        @JvmSynthetic
        override fun hashCode(): Int {
            var result = size
            result = 31 * result + name.hashCode()
            result = 31 * result + id.hashCode()
            result = 31 * result + value.contentHashCode()
            return result
        }
    }

    data class PairedText(
        @JvmSynthetic override val name: String,
        @JvmSynthetic val value: List<Pair<String, String>>,
        @JvmSynthetic override val size: Int,
    ) : Frame(name, size) {
        @JvmSynthetic
        override fun writeTo(buffer: ByteArray, offset: Int): Int {
            val pairsSize = value.sumOf { (first, second) ->
                2 + encodeUtf16LE(first).size + 2 + 2 + encodeUtf16LE(second).size + 2
            }
            val contentSize = 1 + pairsSize

            var currentOffset = writeFrameHeader(buffer, offset)

            // Encoding
            buffer[currentOffset++] = 1

            // Pairs
            value.forEach { (role, name) ->
                // BOM + role
                BOM.copyInto(buffer, currentOffset)
                currentOffset += BOM.size
                val roleBytes = encodeUtf16LE(role)
                roleBytes.copyInto(buffer, currentOffset)
                currentOffset += roleBytes.size

                // Separator + BOM + name
                buffer[currentOffset++] = 0
                buffer[currentOffset++] = 0
                BOM.copyInto(buffer, currentOffset)
                currentOffset += BOM.size
                val nameBytes = encodeUtf16LE(name)
                nameBytes.copyInto(buffer, currentOffset)
                currentOffset += nameBytes.size

                // Separator
                buffer[currentOffset++] = 0
                buffer[currentOffset++] = 0
            }

            return offset + contentSize
        }
    }
}
