@file:JvmSynthetic
@file:JvmName("-FrameMapper")

package io.github.kingg22.vibrion.id3

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

@JvmSynthetic
internal fun setIntegerFrame(name: String, value: Int) = Frame.Numeric(
    name = name,
    value = value,
    size = Sizes.getNumericFrameSize(value.toString().length),
)

/** Only for TDAT, special string trat as Numeric to write */
@JvmSynthetic
internal fun setIntegerFrame(name: String, value: String) = Frame.Numeric(
    name = name,
    value = value,
    size = Sizes.getNumericFrameSize(value.length),
)

@JvmSynthetic
internal fun setStringFrame(name: String, value: String) = Frame.Text(
    name,
    value,
    Sizes.getStringFrameSize(value.length),
)

@JvmSynthetic
internal fun setPictureFrame(
    pictureType: Int,
    data: ByteArray,
    description: String,
    useUnicodeEncoding: Boolean,
): Frame.Picture {
    val mimeType = requireNotNull(getMimeType(data)) { "Unknown picture MIME type" }
    val actualUseUnicode = description.isNotEmpty() && useUnicodeEncoding

    return Frame.Picture(
        name = "APIC",
        value = data,
        pictureType = pictureType,
        mimeType = mimeType,
        description = description,
        useUnicode = actualUseUnicode,
        size = Sizes.getPictureFrameSize(
            pictureSize = data.size,
            mimeTypeSize = mimeType.length,
            descriptionSize = description.length,
            useUnicodeEncoding = actualUseUnicode,
        ),
    )
}

@JvmSynthetic
internal fun setLyricsFrame(language: String, description: String, lyrics: String) = Frame.UnsynchronisedLyrics(
    name = "USLT",
    language = strToCodePointsByte(language),
    description = description,
    value = lyrics,
    size = Sizes.getLyricsFrameSize(description.length, lyrics.length),
)

@JvmSynthetic
internal fun setCommentFrame(language: String, description: String, text: String) = Frame.Comment(
    name = "COMM",
    language = strToCodePointsByte(language),
    description = description,
    value = text,
    size = Sizes.getCommentFrameSize(description.length, text.length),
)

@JvmSynthetic
internal fun setPrivateFrame(id: String, data: ByteArray) = Frame.Private(
    name = "PRIV",
    id = id,
    value = data,
    size = Sizes.getPrivateFrameSize(id.length, data.size),
)

@JvmSynthetic
internal fun setUserStringFrame(description: String, value: String) = Frame.UserText(
    name = "TXXX",
    description = description,
    value = value,
    size = Sizes.getUserStringFrameSize(description.length, value.length),
)

@JvmSynthetic
internal fun setUrlLinkFrame(name: String, url: String) = Frame.Url(
    name = name,
    value = url,
    size = Sizes.getUrlLinkFrameSize(url.length),
)

@JvmSynthetic
internal fun setPairedTextFrame(name: String, list: List<Pair<String, String>>) = Frame.PairedText(
    name = name,
    value = list,
    size = Sizes.getPairedTextFrameSize(list),
)

@JvmSynthetic
internal fun setSynchronisedLyricsFrame(
    type: Int,
    text: List<Pair<String, Int>>,
    timestampFormat: Int,
    language: String,
    description: String,
) = Frame.SynchronisedLyrics(
    name = "SYLT",
    value = text,
    language = strToCodePointsByte(language),
    description = description,
    type = type,
    timestampFormat = timestampFormat,
    size = Sizes.getSynchronisedLyricsFrameSize(text, description.length),
)
