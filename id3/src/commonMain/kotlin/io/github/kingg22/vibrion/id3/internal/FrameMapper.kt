@file:JvmSynthetic
@file:JvmName("-FrameMapper")

package io.github.kingg22.vibrion.id3.internal

import io.github.kingg22.vibrion.id3.internal.frames.*
import io.github.kingg22.vibrion.id3.model.*
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

/** Only for TDAT, special string trat as NumericFrameEncoder to write. IntegerTagFrame the rest of valid tags. */
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
internal fun AttachedPicture.setPictureFrame(): FrameEncoder {
    val mimeType = getMimeType(this.data)
    val actualUseUnicode = this.description.isNotEmpty() && this.useUnicodeEncoding

    return PictureFrameEncoder(
        value = this.data,
        pictureType = this.type,
        mimeType = mimeType,
        description = this.description,
        useUnicode = actualUseUnicode,
        size = getPictureFrameSize(
            pictureSize = this.data.size,
            mimeTypeSize = mimeType.length,
            descriptionSize = this.description.length,
            useUnicodeEncoding = actualUseUnicode,
        ),
    )
}

@JvmSynthetic
internal fun UnsynchronisedLyrics.setLyricsFrame() = UnsynchronisedLyricsFrameEncoder(
    language = strToCodePointsByte(language),
    description = description,
    value = lyrics,
    size = getLyricsFrameSize(description.length, lyrics.length),
)

@JvmSynthetic
internal fun CommentFrame.setCommentFrame() = CommentFrameEncoder(
    language = strToCodePointsByte(language),
    description = description,
    value = text,
    size = getCommentFrameSize(description.length, text.length),
)

@JvmSynthetic
internal fun PrivateFrame.setPrivateFrame() = PrivateFrameEncoder(
    id = id,
    value = data,
    size = getPrivateFrameSize(id.length, data.size),
)

@JvmSynthetic
internal fun UserDefinedText.setUserStringFrame() = UserTextFrameEncoder(
    description = description,
    value = value,
    size = getUserStringFrameSize(description.length, value.length),
)

@JvmSynthetic
internal fun UserDefinedText.setUserUrlFrame() = UserUrlFrameEncoder(
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
internal fun SynchronizedLyrics.setSynchronisedLyricsFrame() = SynchronisedLyricsFrameEncoder(
    value = text,
    language = strToCodePointsByte(language),
    description = description,
    type = type,
    timestampFormat = timestampFormat,
    size = getSynchronisedLyricsFrameSize(text, description.length),
)
