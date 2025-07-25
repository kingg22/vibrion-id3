@file:JvmSynthetic
@file:JvmName("-Sizes")

package io.github.kingg22.vibrion.id3.internal

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

private const val FRAME_HEADER_SIZE = 10
private const val ENCODING_SIZE = 1
private const val BOM_SIZE = 2

@JvmSynthetic
internal fun getNumericFrameSize(frameSize: Int) = FRAME_HEADER_SIZE + ENCODING_SIZE + frameSize

@JvmSynthetic
internal fun getStringFrameSize(frameSize: Int): Int {
    val frameUtf16Size = frameSize * 2
    return FRAME_HEADER_SIZE + ENCODING_SIZE + BOM_SIZE + frameUtf16Size
}

@JvmSynthetic
internal fun getLyricsFrameSize(descriptionSize: Int, lyricsSize: Int): Int {
    val languageSize = 3
    val descriptionUtf16Size = descriptionSize * 2
    val separatorSize = 2
    val lyricsUtf16Size = lyricsSize * 2
    return FRAME_HEADER_SIZE + ENCODING_SIZE + languageSize + BOM_SIZE +
        descriptionUtf16Size + separatorSize + BOM_SIZE + lyricsUtf16Size
}

@JvmSynthetic
internal fun getPictureFrameSize(
    pictureSize: Int,
    mimeTypeSize: Int,
    descriptionSize: Int,
    useUnicodeEncoding: Boolean,
): Int {
    val separatorSize = 1
    val pictureTypeSize = 1

    val encodedDescriptionSize = if (useUnicodeEncoding) {
        BOM_SIZE + (descriptionSize + separatorSize) * 2
    } else {
        descriptionSize + separatorSize
    }

    return FRAME_HEADER_SIZE + ENCODING_SIZE + mimeTypeSize + separatorSize +
        pictureTypeSize + encodedDescriptionSize + pictureSize
}

@JvmSynthetic
internal fun getCommentFrameSize(descriptionSize: Int, textSize: Int): Int {
    val languageSize = 3
    val descriptionUtf16Size = descriptionSize * 2
    val separatorSize = 2
    val textUtf16Size = textSize * 2
    return FRAME_HEADER_SIZE + ENCODING_SIZE + languageSize + BOM_SIZE +
        descriptionUtf16Size + separatorSize + BOM_SIZE + textUtf16Size
}

@JvmSynthetic
internal fun getPrivateFrameSize(idSize: Int, dataSize: Int): Int {
    val separatorSize = 1
    return FRAME_HEADER_SIZE + idSize + separatorSize + dataSize
}

@JvmSynthetic
internal fun getUserStringFrameSize(descriptionSize: Int, valueSize: Int): Int {
    val descriptionUtf16Size = descriptionSize * 2
    val separatorSize = 2
    val valueUtf16Size = valueSize * 2
    return FRAME_HEADER_SIZE + ENCODING_SIZE + BOM_SIZE +
        descriptionUtf16Size + separatorSize + BOM_SIZE + valueUtf16Size
}

@JvmSynthetic
internal fun getUrlLinkFrameSize(urlSize: Int) = FRAME_HEADER_SIZE + urlSize

@JvmSynthetic
internal fun getUserUrlFrameSize(descriptionSize: Int, urlSize: Int): Int {
    val encodedDescription = descriptionSize * 2

    return FRAME_HEADER_SIZE + // frame header
        ENCODING_SIZE + // encoding byte
        BOM_SIZE + // BOM (2 bytes)
        encodedDescription +
        BOM_SIZE + // null terminator for UTF-16LE
        urlSize
}

@JvmSynthetic
internal fun getPairedTextFrameSize(list: List<Pair<String, String>>): Int {
    val separatorSize = 2

    val encodedListSize = list.sumOf { (key, value) ->
        BOM_SIZE + key.length * 2 + separatorSize +
            BOM_SIZE + value.length * 2 + separatorSize
    }

    return FRAME_HEADER_SIZE + ENCODING_SIZE + encodedListSize
}

@JvmSynthetic
internal fun getSynchronisedLyricsFrameSize(lyrics: List<Pair<String, Int>>, descriptionSize: Int): Int {
    val languageSize = 3
    val timestampFormatSize = 1
    val contentTypeSize = 1
    val descriptionUtf16Size = descriptionSize * 2
    val separatorSize = 2
    val timestampSize = 4

    val encodedLyricsSize = lyrics.sumOf { (text, _) ->
        BOM_SIZE + text.length * 2 + separatorSize + timestampSize
    }

    return FRAME_HEADER_SIZE + ENCODING_SIZE + languageSize + timestampFormatSize +
        contentTypeSize + BOM_SIZE + descriptionUtf16Size + separatorSize + encodedLyricsSize
}
