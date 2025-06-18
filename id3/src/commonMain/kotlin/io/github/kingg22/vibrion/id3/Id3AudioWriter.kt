package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.FrameValue.*
import io.github.kingg22.vibrion.id3.ID3FrameType.*
import kotlin.js.JsName

// Inspired on https://github.com/egoroof/browser-id3-writer

data class Id3AudioWriter(var arrayBuffer: ByteArray) {
    var padding = 4096
    private val frames = mutableListOf<Frame>()

    companion object {
        private const val HEADER_SIZE = 10
    }

    operator fun set(type: ID3FrameType, value: FrameValue) {
        when (type) {
            in ID3FrameType.listFrames, TPE1, TCOM, TCON -> {
                require(value is StringListFrame)
                val joined = value.values.joinToString(if (type == TCON) ";" else "/")
                frames += setStringFrame(type.name, joined)
            }

            in ID3FrameType.stringFrames, TLAN, TIT1, TIT2, TIT3, TALB, TPE2, TPE3, TPE4, TRCK, TPOS, TMED,
            TPUB, TCOP, TKEY, TEXT, TDAT, TCMP, TSRC,
            -> {
                require(value is TextFrame)
                if (type == TDAT) {
                    frames += setIntegerFrame(type.name, value.value)
                    return
                }
                frames += setStringFrame(type.name, value.value)
            }

            in ID3FrameType.numericFrames, TBPM, TLEN, TYER -> {
                require(value is IntegerFrame)
                frames += setIntegerFrame(type.name, value.value)
            }

            in ID3FrameType.urlFrames, WCOM, WCOP, WOAF, WOAR, WOAS, WORS, WPAY, WPUB -> {
                require(value is UrlLink || value is TextFrame)

                if (value is TextFrame) {
                    frames += setUrlLinkFrame(type.name, value.value)
                } else if (value is UrlLink) {
                    frames += setUrlLinkFrame(type.name, value.url)
                }
            }

            USLT -> {
                require(value is UnsynchronisedLyrics)
                frames += setLyricsFrame(value.language, value.description, value.lyrics)
            }

            APIC -> {
                require(value is AttachedPicture)
                frames += setPictureFrame(value.type, value.data, value.description, value.useUnicodeEncoding)
            }

            TXXX -> {
                require(value is UserDefinedText)
                frames += setUserStringFrame(value.description, value.value)
            }

            COMM -> {
                require(value is CommentFrame)
                frames += setCommentFrame(value.language, value.description, value.text)
            }

            PRIV -> {
                require(value is PrivateFrame)
                frames += setPrivateFrame(value.id, value.data)
            }

            IPLS -> {
                require(value is PairedTextFrame)
                frames += setPairedTextFrame(type.name, value.pairs)
            }

            SYLT -> {
                require(value is SynchronisedLyrics)
                frames += setSynchronisedLyricsFrame(
                    value.type,
                    value.text,
                    value.timestampFormat,
                    value.language,
                    value.description,
                )
            }

            UNKNOWN -> error("Unsupported frame ${type.name}")
        }
    }

    @JsName("setFrame")
    operator fun set(frameName: String, value: FrameValue) = set(ID3FrameType.fromCode(frameName), value)

    fun removeTag() {
        val headerLength = 10

        if (arrayBuffer.size < headerLength) return

        val version = arrayBuffer[3].toInt() and 0xFF
        if (!isId3v2(arrayBuffer) || version < 2 || version > 4) return

        val tagSize = uint7ArrayToUint28(
            listOf(
                arrayBuffer[6],
                arrayBuffer[7],
                arrayBuffer[8],
                arrayBuffer[9],
            ).map { it.toInt() and 0xFF },
        ) + headerLength

        arrayBuffer = arrayBuffer.sliceArray(tagSize until arrayBuffer.size)
    }

    fun addTag(): ByteArray {
        this.removeTag()

        // Calcular tamaño TOTAL incluyendo headers de frame
        val totalFrameSize = frames.sumOf { it.size }
        val totalTagSize = HEADER_SIZE + totalFrameSize + padding
        val newBuffer = ByteArray(arrayBuffer.size + totalTagSize)

        var offset = 0

        // Header
        val header = byteArrayOf(
            0x49,
            0x44,
            0x33,
            3,
            0, // version
            0, // flags
        )
        header.copyInto(newBuffer, offset)
        offset += header.size

        // Escribir tamaño del tag (synchsafe)
        val tagSizeBytes = encodeSynchsafeInt(totalTagSize - HEADER_SIZE)
        tagSizeBytes.copyInto(newBuffer, offset)
        offset += tagSizeBytes.size

        // Write each frame
        frames.forEach { frame ->
            offset += frame.writeTo(newBuffer, offset)
        }

        // write padding
        if (padding > 0) {
            // workaround ArrayIndexOutOfBoundsException arraycopy: last destination index 332 out of bounds for byte[322]
            newBuffer.fill(0, offset, offset + padding)
            offset += padding
        }

        // Copy original content
        if (arrayBuffer.isNotEmpty()) {
            // workaround ArrayIndexOutOfBoundsException arraycopy: last destination index 332 out of bounds for byte[322]
            arrayBuffer.copyInto(newBuffer, offset)
        }
        arrayBuffer = newBuffer
        return arrayBuffer
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Id3AudioWriter

        if (!arrayBuffer.contentEquals(other.arrayBuffer)) return false
        if (frames != other.frames) return false

        return true
    }

    override fun hashCode(): Int {
        var result = arrayBuffer.contentHashCode()
        result = 31 * result + frames.hashCode()
        return result
    }
}
