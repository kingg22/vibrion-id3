@file:JvmSynthetic
@file:JvmName("-Encoder")

package io.github.kingg22.vibrion.id3

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

// https://encoding.spec.whatwg.org/

@JvmSynthetic
internal fun String.toCodePoints() = this.map { it.code }

@JvmSynthetic
internal fun String.toCodePointsByte() = strToCodePointsByte(this)

@JvmSynthetic
@JvmName("strEncodeWindows1252")
internal fun String.encodeWindows1252() = encodeWindows1252(this)

@JvmSynthetic
@JvmName("strEncodeUtf16LE")
internal fun String.encodeUtf16LE() = encodeUtf16LE(this)

@JvmSynthetic
internal fun strToCodePoints(str: String) = str.toCodePoints()

@JvmSynthetic
internal fun strToCodePointsByte(str: String) = str.map { it.code.toByte() }

@JvmSynthetic
internal fun encodeWindows1252(str: String) = str.toCodePointsByte().toByteArray()

@JvmSynthetic
internal fun encodeUtf16LE(str: String): ByteArray {
    val result = ByteArray(str.length * 2)
    str.forEachIndexed { i, char ->
        val code = char.code
        result[i * 2] = (code and 0xFF).toByte() // LSB
        result[i * 2 + 1] = ((code shr 8) and 0xFF).toByte() // MSB
    }
    return result
}

@JvmSynthetic
internal fun isId3v2(buf: ByteArray) = buf.size >= 3 &&
    buf[0] == 0x49.toByte() &&
    buf[1] == 0x44.toByte() &&
    buf[2] == 0x33.toByte()

@JvmSynthetic
internal fun getMimeType(bytes: ByteArray): String? {
    if (bytes.isEmpty()) return null

    fun b(i: Int): Int = bytes.getOrNull(i)?.toInt()?.and(0xFF) ?: -1

    return when {
        b(0) == 0xFF && b(1) == 0xD8 && b(2) == 0xFF -> "image/jpeg"
        b(0) == 0x89 && b(1) == 0x50 && b(2) == 0x4E && b(3) == 0x47 -> "image/png"
        b(0) == 0x47 && b(1) == 0x49 && b(2) == 0x46 -> "image/gif"
        b(8) == 0x57 && b(9) == 0x45 && b(10) == 0x42 && b(11) == 0x50 -> "image/webp"
        (b(0) == 0x49 && b(1) == 0x49 && b(2) == 0x2A && b(3) == 0x00) ||
            (b(0) == 0x4D && b(1) == 0x4D && b(2) == 0x00 && b(3) == 0x2A) -> "image/tiff"

        b(0) == 0x42 && b(1) == 0x4D -> "image/bmp"
        b(0) == 0x00 && b(1) == 0x00 && b(2) == 0x01 && b(3) == 0x00 -> "image/x-icon"
        else -> null
    }
}
