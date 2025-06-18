package io.github.kingg22.vibrion.id3

import kotlin.jvm.JvmOverloads

// TODO improve this
sealed class FrameValue {
    protected fun languageFormat(language: String) = require(language.matches(Regex("[a-zA-Z]{3}"))) {
        "Language must follow ISO 639-2"
    }

    /** @see ID3FrameType.stringFrames */
    data class TextFrame(val value: String) : FrameValue()

    /** @see ID3FrameType.listFrames */
    data class StringListFrame(val values: List<String>) : FrameValue() {
        constructor(vararg values: String) : this(values.toList())
    }

    /** @see ID3FrameType.numericFrames */
    data class IntegerFrame(val value: Int) : FrameValue()

    /** @see ID3FrameType.USLT */
    data class UnsynchronisedLyrics @JvmOverloads constructor(
        val lyrics: String,
        val description: String = "",
        val language: String = "eng",
    ) : FrameValue() {
        init {
            languageFormat(language)
        }
    }

    /** @see ID3FrameType.APIC */
    data class AttachedPicture @JvmOverloads constructor(
        val type: Int,
        val data: ByteArray,
        val description: String = "",
        val useUnicodeEncoding: Boolean = true,
    ) : FrameValue() {
        init {
            require(type in 0..20) { "Incorrect picture type" }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AttachedPicture

            if (type != other.type) return false
            if (useUnicodeEncoding != other.useUnicodeEncoding) return false
            if (!data.contentEquals(other.data)) return false
            if (description != other.description) return false

            return true
        }

        override fun hashCode(): Int {
            var result = type
            result = 31 * result + useUnicodeEncoding.hashCode()
            result = 31 * result + data.contentHashCode()
            result = 31 * result + description.hashCode()
            return result
        }
    }

    /** @see ID3FrameType.TXXX */
    data class UserDefinedText(val description: String, val value: String) : FrameValue() {
        init {
            require(description.isNotBlank()) { "Description cannot be blank" }
            require(value.isNotBlank()) { "Value cannot be blank" }
        }
    }

    /** @see ID3FrameType.urlFrames */
    data class UrlLink(
        /** language: url */
        val url: String,
    ) : FrameValue()

    /** @see ID3FrameType.COMM */
    data class CommentFrame @JvmOverloads constructor(
        val description: String,
        val text: String,
        val language: String = "eng",
    ) : FrameValue() {
        init {
            languageFormat(language)
        }
    }

    /** @see ID3FrameType.PRIV */
    data class PrivateFrame(val id: String, val data: ByteArray) : FrameValue() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as PrivateFrame

            if (id != other.id) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    /** @see ID3FrameType.IPLS */
    data class PairedTextFrame(val pairs: List<Pair<String, String>>) : FrameValue() {
        constructor(vararg pairs: Pair<String, String>) : this(pairs.toList())
        constructor(vararg pairs: Map<String, String>) : this(pairs.toList().flatMap { it.toList() })
    }

    /** @see ID3FrameType.SYLT */
    data class SynchronisedLyrics @JvmOverloads constructor(
        // TODO tipar esto
        val type: Int,
        val text: List<Pair<String, Int>>,
        val timestampFormat: Int,
        val language: String = "eng",
        val description: String = "",
    ) : FrameValue() {
        @JvmOverloads
        constructor(
            type: Int,
            timestampFormat: Int,
            vararg text: Pair<String, Int>,
            language: String = "eng",
            description: String = "",
        ) : this(type, text = text.toList(), timestampFormat, language, description)

        @JvmOverloads
        constructor(
            type: Int,
            timestampFormat: Int,
            vararg text: Map<String, Int>,
            language: String = "eng",
            description: String = "",
        ) : this(type, text = text.toList().flatMap { it.toList() }, timestampFormat, language, description)

        init {
            require(type in 0..6) { "Incorrect synchronised lyrics content type" }
            require(timestampFormat in 1..2) { "Incorrect synchronised lyrics timestamp format" }
            languageFormat(language)
        }
    }
}
