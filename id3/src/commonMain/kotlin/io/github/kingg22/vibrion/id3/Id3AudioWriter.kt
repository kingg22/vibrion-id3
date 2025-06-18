package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.*
import io.github.kingg22.vibrion.id3.internal.*
import io.github.kingg22.vibrion.id3.internal.frames.FrameEncoder
import io.github.kingg22.vibrion.id3.model.*
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * Builder for ID3 tags.
 *
 * @param arrayBuffer ByteArray to add the tags.
 * @property padding Padding to add to the end of the file.
 *
 * @see io.github.kingg22.vibrion.id3.Id3AudioWriter.set
 * @see io.github.kingg22.vibrion.id3.Id3AudioWriter.addTag
 * @see Id3v2v3TagFrame
 * @see FrameValue
 * @see <a href="https://github.com/egoroof/browser-id3-writer">Inspired on 'egoroof/browser-id3-writer' on JavaScript / Typescript</a>
 */
data class Id3AudioWriter(var arrayBuffer: ByteArray) {
    var padding = 4096
    private val frames = mutableListOf<FrameEncoder>()

    companion object {
        private const val HEADER_SIZE = 10
    }

    /** Special method to builders outside this data class */
    @JvmSynthetic
    internal fun setFrames(frames: List<FrameEncoder>) = apply { this.frames.addAll(frames) }

    /**
     * Set various frames of varios type [Id3v2v3TagFrame] to [values]
     *
     * @see Id3v2v3TagFrame
     * @see FrameValue
     * @throws IllegalArgumentException [types] and [values] must be the same size or
     * if [values] is not compatible with [types]
     * or [types] is not supported.
     */
    operator fun set(types: List<Id3v2v3TagFrame>, values: List<FrameValue>) {
        require(types.size == values.size)
        types.zip(values).forEach { (type, value) ->
            set(type, value)
        }
    }

    /**
     * Set Frames of type [Id3v2v3TagFrame] to [value]
     *
     * @see Id3v2v3TagFrame
     * @see FrameValue
     * @throws IllegalArgumentException if [value] is not compatible with [type] or [type] is not supported.
     */
    private operator fun set(type: Id3v2v3TagFrame, value: FrameValue) {
        when (type) {
            is ListStringTagFrame -> {
                require(value is StringListFrame)
                val joined = value.values.joinToString(if (type == TCON) ";" else "/")
                frames += setStringFrame(type::class.simpleName!!, joined)
            }

            // First evaluate url, after TextTagFrame because UrlTagFrame is a TextTagFrame but encode different
            is UrlTagFrame -> {
                require(value is TextFrame)
                frames += setUrlLinkFrame(type.name, value.value)
            }

            is TextTagFrame -> {
                require(value is TextFrame)
                if (type == TDAT) {
                    frames += setIntegerFrame(type.name, value.value)
                    return
                }
                frames += setStringFrame(type.name, value.value)
            }

            is IntegerTagFrame -> {
                require(value is IntegerFrame)
                frames += setIntegerFrame(type.name, value.value)
            }

            is UnsynchronisedLyricsTagFrame -> {
                require(value is UnsynchronisedLyrics)
                frames += setLyricsFrame(value.language, value.description, value.lyrics)
            }

            is AttachedPictureTagFrame -> {
                require(value is AttachedPicture)
                frames += setPictureFrame(value.type, value.data, value.description, value.useUnicodeEncoding)
            }

            is UserDefinedTextTagFrame -> {
                require(value is UserDefinedText)
                frames += setUserStringFrame(value.description, value.value)
            }

            is CommentTagFrame -> {
                require(value is CommentFrame)
                frames += setCommentFrame(value.language, value.description, value.text)
            }

            is PrivateTagFrame -> {
                require(value is PrivateFrame)
                frames += setPrivateFrame(value.id, value.data)
            }

            is PairedTextTagFrame -> {
                require(value is PairedTextFrame)
                frames += setPairedTextFrame(type.name, value.pairs)
            }

            is SynchronizedLyricsTagFrame -> {
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
     * @see Id3v2v3TagFrame
     * @see FrameValue
     * @throws IllegalArgumentException if [value] is not compatible with [frameName] or [frameName] is not supported.
     */
    @JsName("setFrame")
    @Throws(IllegalArgumentException::class)
    operator fun set(frameName: String, value: FrameValue) = set(Id3v2v3TagFrame.fromCode(frameName), value)

    /**
     * _implies_ ([id] == [TBPM] || [id] == [TLEN] || [id] == [TYER])
     * @see Id3v2v3TagFrame
     * @see Id3v2v3TagFrame.integerFrames
     * @see IntegerTagFrame
     */
    @JsName("setIntFrame")
    operator fun set(id: IntegerTagFrame, value: Int) {
        set(id, IntegerFrame(value))
    }

    /**
     * _implies_ ([id] in [Id3v2v3TagFrame.stringFrames] || [id] in [Id3v2v3TagFrame.urlFrames])
     * @see Id3v2v3TagFrame
     * @see Id3v2v3TagFrame.stringFrames
     * @see Id3v2v3TagFrame.urlFrames
     * @see TextTagFrame
     */
    @JsName("setStringFrame")
    operator fun set(id: TextTagFrame, value: String) {
        set(id, TextFrame(value))
    }

    /**
     * _implies_ ([id] == [TCOM] || [id] == [TCON] || [id] == [TPE1])
     * @see Id3v2v3TagFrame
     * @see Id3v2v3TagFrame.listFrames
     * @see StringListFrame
     */
    @JsName("setListStringFrame")
    @JvmName("setListStringFrame")
    operator fun set(id: ListStringTagFrame, value: List<String>) {
        set(id, StringListFrame(value))
    }

    /**
     * _implies_ ([id] == [IPLS])
     * @see Id3v2v3TagFrame
     * @see PairedTextTagFrame
     */
    @JsName("setPairStringFrame")
    @JvmName("setPairStringFrame")
    operator fun set(id: PairedTextTagFrame, value: List<Pair<String, String>>) {
        set(id, PairedTextFrame(value))
    }

    // -- objects --

    /**
     * _implies_ ([id] == [USLT])
     * @see Id3v2v3TagFrame
     * @see UnsynchronisedLyricsTagFrame
     */
    @JsName("setUnsynchronisedLyricsFrame")
    operator fun set(id: UnsynchronisedLyricsTagFrame, value: UnsynchronisedLyrics) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [APIC])
     * @see Id3v2v3TagFrame
     * @see AttachedPictureTagFrame
     * @see AttachedPictureType
     */
    @JsName("setAttachedPictureFrame")
    operator fun set(id: AttachedPictureTagFrame, value: AttachedPicture) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [SYLT])
     * @see Id3v2v3TagFrame
     * @see SynchronizedLyricsTagFrame
     * @see SynchronizedLyricsType
     * @see SynchronizedLyricsTimestampFormat
     */
    @JsName("setSynchronizedLyricsFrame")
    operator fun set(id: SynchronizedLyricsTagFrame, value: SynchronizedLyrics) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [TXXX])
     * @see Id3v2v3TagFrame
     * @see UserDefinedTextTagFrame
     */
    @JsName("setUserDefinedTextFrame")
    operator fun set(id: UserDefinedTextTagFrame, value: UserDefinedText) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [COMM])
     * @see Id3v2v3TagFrame
     * @see CommentTagFrame
     */
    @JsName("setCommentFrame")
    operator fun set(id: CommentTagFrame, value: CommentFrame) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2v3TagFrame
     * @see PrivateTagFrame
     */
    @JsName("setPrivateFrame")
    operator fun set(id: PrivateTagFrame, value: PrivateFrame) {
        set(id, value as FrameValue)
    }

    // -- overloads --

    /**
     * _implies_ ([id] == [TXXX])
     * @see Id3v2v3TagFrame
     * @see UserDefinedTextTagFrame
     * @see UserDefinedText
     */
    @JsName("setUserDefinedTextFramePair")
    @JvmName("setUserDefinedTextFramePair")
    operator fun set(id: UserDefinedTextTagFrame, value: Pair<String, String>) {
        set(id, UserDefinedText(value.first, value.second))
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2v3TagFrame
     * @see PrivateTagFrame
     */
    @JsName("setPairPrivateFrame")
    @JvmName("setPairPrivateFrame")
    operator fun set(id: PrivateTagFrame, value: Pair<String, ByteArray>) {
        set(id, PrivateFrame(value.first, value.second))
    }

    /**
     * Add tag to [arrayBuffer].
     * Aka build.
     *
     * @return [arrayBuffer] with the tag added.
     * @see Id3AudioWriter.set
     * @see Id3AudioWriter.removeTag
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

    /**
     * Remove tag from [arrayBuffer].
     *
     * @see Id3AudioWriter.set
     * @see Id3AudioWriter.addTag
     */
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
fun id3AudioWriter(array: ByteArray, block: Id3AudioWriter.() -> Unit) = Id3AudioWriter(array).apply(block)
