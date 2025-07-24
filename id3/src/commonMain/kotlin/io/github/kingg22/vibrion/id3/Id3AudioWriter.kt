package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.*
import io.github.kingg22.vibrion.id3.internal.*
import io.github.kingg22.vibrion.id3.internal.frames.FrameEncoder
import io.github.kingg22.vibrion.id3.model.*
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * Builder for ID3 v2.3 tags.
 *
 * _Note_: Current is impossible to delete a tag frame after set it.
 * If you need to do that, create a [Id3AudioWriter.deepCopy] before and modify it, or only don't set the value.
 *
 * **Don't recommend to call [Id3AudioWriter.addTag] or [Id3AudioWriter.toByteArray] or [Id3AudioWriter.build] repeated.**
 *
 * Example:
 * ```kotlin
 * val writer = Id3AudioWriter()
 * writer[Id3v2v3TagFrame.TPE1] = listOf("Eminem", "50 Cent")
 * val tag = writer.addTag()
 * ```
 * Java:
 * ```java
 * class Id3BuilderJavaTest {
 *     void testId3Writer() {
 *         final var writer = new Id3AudioWriter();
 *         writer.setPadding(0);
 *         // Need to call `.INSTANCE` because is an kotlin object.
 *         writer.set(Id3v2v3TagFrame.TIT2.INSTANCE, "Title");
 *         writer.set(Id3v2v3TagFrame.TPE1.INSTANCE, List.of("Eminem", "50 Cent"));
 *         final byte[] tag = writer.build();
 *     }
 * }
 * ```
 *
 * @property padding Padding to add to the end of the file.
 *
 * @see Id3AudioWriter.set
 * @see Id3AudioWriter.addTag
 * @see Id3AudioWriter.build
 * @see Id3AudioWriter.removeTag
 * @see Id3v2v3TagFrame
 * @see FrameValue
 * @see Id3WriterBuilder
 * @see <a href="https://github.com/egoroof/browser-id3-writer">Inspired on 'egoroof/browser-id3-writer' on JavaScript / Typescript</a>
 */
@Id3Dsl
data class Id3AudioWriter @JvmOverloads constructor(var padding: Int = 4096) {
    private val frames = mutableListOf<FrameEncoder>()

    companion object {
        private const val HEADER_SIZE = 10

        /**
         * DSL for [Id3AudioWriter].
         *
         * @see Id3AudioWriter
         * @see Id3AudioWriter.set
         * @see Id3AudioWriter.addTag
         * @see Id3v2v3TagFrame
         * @see FrameValue
         */
        @JvmSynthetic
        fun id3AudioWriter(block: Id3AudioWriter.() -> Unit) = Id3AudioWriter().apply(block)

        /**
         * Remove tag of [arrayBuffer] if it is a valid ID3v2 tag.
         *
         * @see Id3AudioWriter.set
         * @see Id3AudioWriter.addTag
         */
        @JvmStatic
        fun removeTag(arrayBuffer: ByteArray): ByteArray {
            if (arrayBuffer.size < HEADER_SIZE) return arrayBuffer

            val version = arrayBuffer[3].toInt() and 0xFF
            if (!isId3v2(arrayBuffer) || version < 2 || version > 4) return arrayBuffer

            val tagSize = uint7ArrayToUint28(
                listOf(
                    arrayBuffer[6],
                    arrayBuffer[7],
                    arrayBuffer[8],
                    arrayBuffer[9],
                ).map { it.toInt() and 0xFF },
            ) + HEADER_SIZE

            return arrayBuffer.sliceArray(tagSize until arrayBuffer.size)
        }
    }

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
                val joined = value.values.joinToString(if (type is TCON) ";" else "/")
                frames += setStringFrame(type.name, joined)
            }

            // First evaluate url, after TextTagFrame because UrlTagFrame is a TextTagFrame but encode different
            is UrlTagFrame -> {
                require(value is TextFrame)
                frames += setUrlLinkFrame(type.name, value.value)
            }

            is TextTagFrame -> {
                require(value is TextFrame)
                if (type is TDAT) {
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

            UNKNOWN -> throw IllegalArgumentException("Unsupported frame ${type.name}")
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
    @Throws(IllegalArgumentException::class)
    operator fun set(frameName: String, value: FrameValue) = set(Id3v2v3TagFrame.fromCode(frameName), value)

    /**
     * _implies_ ([id] == [TBPM] || [id] == [TLEN] || [id] == [TYER])
     * @see Id3v2v3TagFrame
     * @see Id3v2v3TagFrame.integerFrames
     * @see IntegerTagFrame
     */
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
    operator fun set(id: TextTagFrame, value: String) {
        set(id, TextFrame(value))
    }

    /**
     * _implies_ ([id] == [TCOM] || [id] == [TCON] || [id] == [TPE1])
     * @see Id3v2v3TagFrame
     * @see Id3v2v3TagFrame.listFrames
     * @see StringListFrame
     */
    operator fun set(id: ListStringTagFrame, value: List<String>) {
        set(id, StringListFrame(value))
    }

    /**
     * _implies_ ([id] == [IPLS])
     * @see Id3v2v3TagFrame
     * @see PairedTextTagFrame
     */
    @JvmSynthetic
    operator fun set(id: PairedTextTagFrame, value: List<Pair<String, String>>) {
        set(id, PairedTextFrame(value))
    }

    // -- objects --

    /**
     * _implies_ ([id] == [USLT])
     * @see Id3v2v3TagFrame
     * @see UnsynchronisedLyricsTagFrame
     */
    operator fun set(id: UnsynchronisedLyricsTagFrame, value: UnsynchronisedLyrics) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [APIC])
     * @see Id3v2v3TagFrame
     * @see AttachedPictureTagFrame
     * @see AttachedPictureType
     */
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
    operator fun set(id: SynchronizedLyricsTagFrame, value: SynchronizedLyrics) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [TXXX])
     * @see Id3v2v3TagFrame
     * @see UserDefinedTextTagFrame
     */
    operator fun set(id: UserDefinedTextTagFrame, value: UserDefinedText) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [COMM])
     * @see Id3v2v3TagFrame
     * @see CommentTagFrame
     */
    operator fun set(id: CommentTagFrame, value: CommentFrame) {
        set(id, value as FrameValue)
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2v3TagFrame
     * @see PrivateTagFrame
     */
    operator fun set(id: PrivateTagFrame, value: PrivateFrame) {
        set(id, value as FrameValue)
    }

    // -- overloads --

    /**
     * _implies_ ([id] == [IPLS])
     * @see Id3v2v3TagFrame
     * @see PairedTextTagFrame
     */
    operator fun set(id: PairedTextTagFrame, value: Map<String, String>) {
        set(id, PairedTextFrame(value))
    }

    /**
     * _implies_ ([id] == [USLT])
     *
     * Example Kotlin:
     * ```kotlin
     * id3AudioWriter {
     *     this[USLT, "lyrics", "description"] = "language"
     * }
     * ```
     *
     * @see Id3v2v3TagFrame
     * @see UnsynchronisedLyricsTagFrame
     */
    operator fun set(id: UnsynchronisedLyricsTagFrame, lyrics: String, description: String, language: String) {
        set(id, UnsynchronisedLyrics(lyrics, description, language))
    }

    /**
     * _implies_ ([id] == [TXXX])
     * @see Id3v2v3TagFrame
     * @see UserDefinedTextTagFrame
     * @see UserDefinedText
     */
    @JvmSynthetic
    operator fun set(id: UserDefinedTextTagFrame, value: Pair<String, String>) {
        set(id, UserDefinedText(value.first, value.second))
    }

    /**
     * _implies_ ([id] == [TXXX])
     *
     * Example Kotlin:
     * ```kotlin
     * id3AudioWriter {
     *     this[TXXX, "description"] = "value"
     * }
     * ```
     *
     * @see Id3v2v3TagFrame
     * @see UserDefinedTextTagFrame
     * @see UserDefinedText
     */
    operator fun set(id: UserDefinedTextTagFrame, description: String, value: String) {
        set(id, UserDefinedText(description, value))
    }

    /**
     * _implies_ ([id] == [PRIV])
     * @see Id3v2v3TagFrame
     * @see PrivateTagFrame
     */
    @JvmSynthetic
    operator fun set(id: PrivateTagFrame, value: Pair<String, ByteArray>) {
        set(id, PrivateFrame(value.first, value.second))
    }

    /**
     * _implies_ ([id] == [PRIV])
     *
     * Example Kotlin:
     * ```kotlin
     * id3AudioWriter {
     *     this[PRIV, "id"] = byteArrayOf()
     * }
     * ```
     *
     * @see Id3v2v3TagFrame
     * @see PrivateTagFrame
     */
    operator fun set(tag: PrivateTagFrame, id: String, value: ByteArray) {
        set(tag, PrivateFrame(id, value))
    }

    /**
     * Build the tag and add it to a new [ByteArray].
     *
     * @return [ByteArray] with the tag added.
     * @see Id3AudioWriter.set
     */
    fun addTag(): ByteArray {
        // Calcular tamaño TOTAL incluyendo headers de frame
        val totalTagSize = HEADER_SIZE + padding + frames.sumOf { it.size }
        val newBuffer = ByteArray(totalTagSize)

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
        }

        return newBuffer
    }

    /**
     * Build the tag and add it to a new [ByteArray].
     *
     * @see addTag
     * @see Id3AudioWriter.set
     */
    fun build() = addTag()

    /**
     * Build the tag and add it to a new [ByteArray].
     *
     * @return [ByteArray] with the tag added.
     * @see Id3AudioWriter.set
     */
    fun toByteArray() = addTag()

    /**
     * Deep copy of the current [Id3AudioWriter].
     * Include all frames previously added with [Id3AudioWriter.set]
     */
    @JvmOverloads
    fun deepCopy(padding: Int = this.padding) = Id3AudioWriter(padding).apply {
        this.frames.addAll(this@Id3AudioWriter.frames)
    }
}
