package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3v2FrameType.*
import io.github.kingg22.vibrion.id3.internal.*
import io.github.kingg22.vibrion.id3.internal.frames.Frame
import io.github.kingg22.vibrion.id3.model.*
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * Builder for ID3 tags.
 *
 * @param arrayBuffer ByteArray to add the tags.
 * @property padding Padding to add to the end of the file.
 *
 * @see io.github.kingg22.vibrion.id3.Id3AudioWriter.set
 * @see io.github.kingg22.vibrion.id3.Id3AudioWriter.addTag
 * @see Id3v2FrameType
 * @see FrameValue
 * @see <a href="https://github.com/egoroof/browser-id3-writer">Inspired on 'egoroof/browser-id3-writer' on JavaScript / Typescript</a>
 */
data class Id3AudioWriter(var arrayBuffer: ByteArray) {
    var padding = 4096
    private val frames = mutableListOf<Frame>()

    companion object {
        private const val HEADER_SIZE = 10
    }

    /**
     * Set Frame [Id3v2FrameType] to [value]
     *
     * @see Id3v2FrameType
     * @see FrameValue
     * @throws IllegalArgumentException if [value] is not compatible with [type] or [type] is not supported.
     */
    operator fun set(type: Id3v2FrameType, value: FrameValue) {
        when (type) {
            in Id3v2FrameType.listFrames, TPE1, TCOM, TCON -> {
                require(value is StringListFrame)
                val joined = value.values.joinToString(if (type == TCON) ";" else "/")
                frames += setStringFrame(type.name, joined)
            }

            in Id3v2FrameType.stringFrames, TLAN, TIT1, TIT2, TIT3, TALB, TPE2, TPE3, TPE4, TRCK, TPOS, TMED,
            TPUB, TCOP, TKEY, TEXT, TDAT, TCMP, TSRC,
            -> {
                require(value is TextFrame)
                if (type == TDAT) {
                    frames += setIntegerFrame(type.name, value.value)
                    return
                }
                frames += setStringFrame(type.name, value.value)
            }

            in Id3v2FrameType.numericFrames, TBPM, TLEN, TYER -> {
                require(value is IntegerFrame)
                frames += setIntegerFrame(type.name, value.value)
            }

            in Id3v2FrameType.urlFrames, WCOM, WCOP, WOAF, WOAR, WOAS, WORS, WPAY, WPUB -> {
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
                require(value is SynchronizedLyrics)
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

    /**
     * Advanced set frame as string
     *
     * @see set
     * @see Id3v2FrameType
     * @see FrameValue
     * @throws IllegalArgumentException if [value] is not compatible with [frameName] or [frameName] is not supported.
     */
    @JsName("setFrame")
    operator fun set(frameName: String, value: FrameValue) = set(Id3v2FrameType.fromCode(frameName), value)

    /**
     * _implies_ ([id] == [TBPM] || [id] == [TLEN] || [id] == [TYER])
     * @see Id3v2FrameType
     * @see Id3v2FrameType.numericFrames
     * @see IntegerFrame
     */
    @JsName("setIntFrame")
    operator fun set(id: Id3v2FrameType, value: Int) {
        require(id == TBPM || id == TLEN || id == TYER)
        set(id, IntegerFrame(value))
    }

    /**
     * _implies_ ([id] in [Id3v2FrameType.stringFrames] || [id] in [Id3v2FrameType.urlFrames])
     * @see Id3v2FrameType
     * @see Id3v2FrameType.stringFrames
     * @see Id3v2FrameType.urlFrames
     * @see TextFrame
     */
    @JsName("setStringFrame")
    operator fun set(id: Id3v2FrameType, value: String) {
        require(
            id == TALB ||
                id == TCOP ||
                id == TCMP ||
                id == TDAT ||
                id == TEXT ||
                id == TIT1 ||
                id == TIT2 ||
                id == TIT3 ||
                id == TKEY ||
                id == TLAN ||
                id == TMED ||
                id == TPE2 ||
                id == TPE3 ||
                id == TPE4 ||
                id == TPOS ||
                id == TPUB ||
                id == TRCK ||
                id == TSRC ||
                id == WCOM ||
                id == WCOP ||
                id == WOAF ||
                id == WOAR ||
                id == WOAS ||
                id == WORS ||
                id == WPAY ||
                id == WPUB,
        )
        set(id, TextFrame(value))
    }

    /**
     * _implies_ ([id] == [TCOM] || [id] == [TCON] || [id] == [TPE1])
     * @see Id3v2FrameType
     * @see Id3v2FrameType.listFrames
     * @see StringListFrame
     */
    @JsName("setListStringFrame")
    @JvmName("setListStringFrame")
    operator fun set(id: Id3v2FrameType, value: List<String>) {
        require(id == TCOM || id == TCON || id == TPE1)
        set(id, StringListFrame(value))
    }

    /**
     * _implies_ ([id] == [IPLS])
     * @see Id3v2FrameType
     * @see PairedTextFrame
     */
    @JsName("setPairStringFrame")
    @JvmName("setPairStringFrame")
    operator fun set(id: Id3v2FrameType, value: List<Pair<String, String>>) {
        require(id == IPLS)
        set(id, PairedTextFrame(value))
    }

    // -- objects --

    /**
     * _implies_ ([id] == [USLT])
     * @see Id3v2FrameType
     * @see UnsynchronisedLyrics
     */
    @JsName("setUnsynchronisedLyricsFrame")
    operator fun set(id: Id3v2FrameType, value: UnsynchronisedLyrics) {
        require(id == USLT)
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [APIC])
     * @see Id3v2FrameType
     * @see AttachedPicture
     * @see AttachedPictureType
     */
    @JsName("setAttachedPictureFrame")
    operator fun set(id: Id3v2FrameType, value: AttachedPicture) {
        require(id == APIC)
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [SYLT])
     * @see Id3v2FrameType
     * @see SynchronizedLyrics
     * @see SynchronizedLyricsType
     * @see SynchronizedLyricsTimestampFormat
     */
    @JsName("setSynchronizedLyricsFrame")
    operator fun set(id: Id3v2FrameType, value: SynchronizedLyrics) {
        require(id == SYLT)
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [TXXX])
     * @see Id3v2FrameType
     * @see UserDefinedText
     */
    @JsName("setUserDefinedTextFrame")
    operator fun set(id: Id3v2FrameType, value: UserDefinedText) {
        require(id == TXXX)
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [COMM])
     * @see Id3v2FrameType
     * @see CommentFrame
     */
    @JsName("setCommentFrame")
    operator fun set(id: Id3v2FrameType, value: CommentFrame) {
        require(id == COMM)
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2FrameType
     * @see PrivateFrame
     */
    @JsName("setPrivateFrame")
    operator fun set(id: Id3v2FrameType, value: PrivateFrame) {
        require(id == PRIV)
        set(id, value as FrameValue)
    }

    // -- overloads --

    /**
     * _implies_ ([id] == [TXXX] || [id] == [COMM])
     * @see Id3v2FrameType
     * @see UserDefinedText
     * @see CommentFrame
     */
    @JsName("setTextPairFrame")
    @JvmName("setTextPairFrame")
    operator fun set(id: Id3v2FrameType, value: Pair<String, String>) {
        require(id == TXXX || id == COMM)
        if (id == TXXX) {
            set(id, UserDefinedText(value.first, value.second))
        } else {
            set(id, CommentFrame(value.first, value.second))
        }
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2FrameType
     * @see PrivateFrame
     */
    @JsName("setPairPrivateFrame")
    @JvmName("setPairPrivateFrame")
    operator fun set(id: Id3v2FrameType, value: Pair<String, ByteArray>) {
        require(id == PRIV)
        set(id, PrivateFrame(value.first, value.second))
    }

    /**
     * Add tag to [arrayBuffer].
     * Aka build.
     *
     * @return [arrayBuffer] with the tag added.
     * @see Id3AudioWriter.set
     */
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

    private fun removeTag() {
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

/** DSL */
@Id3Dsl
fun id3AudioWriter(array: ByteArray = ByteArray(0), block: Id3AudioWriter.() -> Unit) =
    Id3AudioWriter(array).apply(block)
