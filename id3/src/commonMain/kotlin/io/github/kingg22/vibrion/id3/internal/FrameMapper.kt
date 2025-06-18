@file:JvmSynthetic
@file:JvmName("-FrameMapper")

package io.github.kingg22.vibrion.id3.internal

import io.github.kingg22.vibrion.id3.internal.frames.*
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

@JvmSynthetic
internal fun setIntegerFrame(name: String, value: Int) = NumericFrame(
    name = name,
    value = value,
    size = getNumericFrameSize(value.toString().length),
)

/** Only for TDAT, special string trat as NumericFrame to write */
@JvmSynthetic
internal fun setIntegerFrame(name: String, value: String) = NumericFrame(
    name = name,
    value = value,
    size = getNumericFrameSize(value.length),
)

@JvmSynthetic
internal fun setStringFrame(name: String, value: String) = TextFrame(
    name,
    value,
    getStringFrameSize(value.length),
)

@JvmSynthetic
internal fun setPictureFrame(
    pictureType: Int,
    data: ByteArray,
    description: String,
    useUnicodeEncoding: Boolean,
): PictureFrame {
    val mimeType = requireNotNull(getMimeType(data)) { "Unknown picture MIME type" }
    val actualUseUnicode = description.isNotEmpty() && useUnicodeEncoding

    return PictureFrame(
        name = "APIC",
        value = data,
        pictureType = pictureType,
        mimeType = mimeType,
        description = description,
        useUnicode = actualUseUnicode,
        size = getPictureFrameSize(
            pictureSize = data.size,
            mimeTypeSize = mimeType.length,
            descriptionSize = description.length,
            useUnicodeEncoding = actualUseUnicode,
        ),
    )
}

@JvmSynthetic
internal fun setLyricsFrame(language: String, description: String, lyrics: String) = UnsynchronisedLyricsFrame(
    name = "USLT",
    language = strToCodePointsByte(language),
    description = description,
    value = lyrics,
    size = getLyricsFrameSize(description.length, lyrics.length),
)

@JvmSynthetic
internal fun setCommentFrame(language: String, description: String, text: String) = CommentFrame(
    name = "COMM",
    language = strToCodePointsByte(language),
    description = description,
    value = text,
    size = getCommentFrameSize(description.length, text.length),
)

@JvmSynthetic
internal fun setPrivateFrame(id: String, data: ByteArray) = PrivateFrame(
    name = "PRIV",
    id = id,
    value = data,
    size = getPrivateFrameSize(id.length, data.size),
)

@JvmSynthetic
internal fun setUserStringFrame(description: String, value: String) = UserTextFrame(
    name = "TXXX",
    description = description,
    value = value,
    size = getUserStringFrameSize(description.length, value.length),
)

@JvmSynthetic
internal fun setUrlLinkFrame(name: String, url: String) = UrlFrame(
    name = name,
    value = url,
    size = getUrlLinkFrameSize(url.length),
)

@JvmSynthetic
internal fun setPairedTextFrame(name: String, list: List<Pair<String, String>>) = PairedTextFrame(
    name = name,
    value = list,
    size = getPairedTextFrameSize(list),
)

@JvmSynthetic
internal fun setSynchronisedLyricsFrame(
    type: Int,
    text: List<Pair<String, Int>>,
    timestampFormat: Int,
    language: String,
    description: String,
) = SynchronisedLyricsFrame(
    name = "SYLT",
    value = text,
    language = strToCodePointsByte(language),
    description = description,
    type = type,
    timestampFormat = timestampFormat,
    size = getSynchronisedLyricsFrameSize(text, description.length),
)
