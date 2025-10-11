package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmOverloads

/**
 * Synchronised lyrics.
 *
 * _Requirements:_
 * - Type **in range of 0-6**. [SynchronizedLyricsType]
 * - TimestampsFormat **in range of 1-2**. [SynchronizedLyricsTimestampFormat]
 * - Language **must be in ISO 639-2 format**.
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.SYLT
 */
class SynchronizedLyrics @JvmOverloads constructor(
    val type: Int,
    val text: List<Pair<String, Int>>,
    val timestampFormat: Int,
    val language: String = "eng",
    val description: String = "",
) : FrameValue {
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

    @JvmOverloads
    constructor(
        type: SynchronizedLyricsType,
        timestampFormat: SynchronizedLyricsTimestampFormat,
        text: List<Pair<String, Int>>,
        language: String = "eng",
        description: String = "",
    ) : this(
        type.value,
        text,
        timestampFormat.value,
        language,
        description,
    )

    @JvmOverloads
    constructor(
        type: SynchronizedLyricsType,
        timestampFormat: SynchronizedLyricsTimestampFormat,
        vararg text: Pair<String, Int>,
        language: String = "eng",
        description: String = "",
    ) : this(
        type = type.value,
        timestampFormat = timestampFormat.value,
        text = text,
        language = language,
        description = description,
    )

    @JvmOverloads
    constructor(
        type: SynchronizedLyricsType,
        timestampFormat: SynchronizedLyricsTimestampFormat,
        vararg text: Map<String, Int>,
        language: String = "eng",
        description: String = "",
    ) : this(
        type = type.value,
        text = text,
        timestampFormat = timestampFormat.value,
        language = language,
        description = description,
    )

    init {
        require(type in 0..6) { "Incorrect synchronised lyrics content type" }
        require(timestampFormat in 1..2) { "Incorrect synchronised lyrics timestamp format" }
        FrameValue.languageFormat(language)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SynchronizedLyrics

        if (type != other.type) return false
        if (timestampFormat != other.timestampFormat) return false
        if (text != other.text) return false
        if (language != other.language) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + timestampFormat
        result = 31 * result + text.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String =
        "SynchronizedLyrics(type=$type, text=$text, timestampFormat=$timestampFormat, language='$language', description='$description')"
}
