@file:JvmSynthetic
@file:JvmName("-FrameMapper")

package io.github.kingg22.vibrion.id3.internal

import io.github.kingg22.vibrion.id3.internal.frames.*
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

@JvmSynthetic
internal fun setIntegerFrame(name: String, value: Int) = NumericFrameEncoder(
    name = name,
    value = value,
    size = getNumericFrameSize(value.toString().length),
)

/** Only for TDAT, special string trat as NumericFrameEncoder to write */
@JvmSynthetic
internal fun setIntegerFrame(name: String, value: String) = NumericFrameEncoder(
    name = name,
    value = value,
    size = getNumericFrameSize(value.length),
)

@JvmSynthetic
internal fun setStringFrame(name: String, value: String) = TextFrameEncoder(
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
): PictureFrameEncoder {
    val mimeType = getMimeType(data)
    val actualUseUnicode = description.isNotEmpty() && useUnicodeEncoding

    return PictureFrameEncoder(
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
internal fun setLyricsFrame(language: String, description: String, lyrics: String) = UnsynchronisedLyricsFrameEncoder(
    language = strToCodePointsByte(language),
    description = description,
    value = lyrics,
    size = getLyricsFrameSize(description.length, lyrics.length),
)

@JvmSynthetic
internal fun setCommentFrame(language: String, description: String, text: String) = CommentFrameEncoder(
    language = strToCodePointsByte(language),
    description = description,
    value = text,
    size = getCommentFrameSize(description.length, text.length),
)

@JvmSynthetic
internal fun setPrivateFrame(id: String, data: ByteArray) = PrivateFrameEncoder(
    id = id,
    value = data,
    size = getPrivateFrameSize(id.length, data.size),
)

@JvmSynthetic
internal fun setUserStringFrame(description: String, value: String) = UserTextFrameEncoder(
    description = description,
    value = value,
    size = getUserStringFrameSize(description.length, value.length),
)

@JvmSynthetic
internal fun setUserUrlFrame(description: String, value: String) = UserUrlFrameEncoder(
    description = description,
    url = value,
    size = getUserUrlFrameSize(description.length, value.length),
)

@JvmSynthetic
internal fun setUrlLinkFrame(name: String, url: String) = UrlFrameEncoder(
    name = name,
    value = url,
    size = getUrlLinkFrameSize(url.length),
)

@JvmSynthetic
internal fun setPairedTextFrame(name: String, list: List<Pair<String, String>>) = PairedTextFrameEncoder(
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
) = SynchronisedLyricsFrameEncoder(
    value = text,
    language = strToCodePointsByte(language),
    description = description,
    type = type,
    timestampFormat = timestampFormat,
    size = getSynchronisedLyricsFrameSize(text, description.length),
)
