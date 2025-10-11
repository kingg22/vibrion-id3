@file:JvmSynthetic
@file:JvmName("-Encoder")
@file:JvmMultifileClass

package io.github.kingg22.vibrion.id3.internal

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

// https://encoding.spec.whatwg.org/

@JvmSynthetic
internal fun encodeWindows1252(str: String) = str.map { it.code.toByte() }.toByteArray()

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
