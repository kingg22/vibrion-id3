@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-NumericFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun NumericFrameEncoder(name: String, value: Int, size: Int): FrameEncoder =
    NumericFrameEncoder(name, size, value.toString())

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun NumericFrameEncoder(name: String, value: String, size: Int): FrameEncoder =
    NumericFrameEncoder(name, size, value)

private class NumericFrameEncoder(name: String, size: Int, value: String) : FrameEncoder(name, size) {
    override val encodedFrame: ByteArray by lazy {
        val contentSize = 1 + value.length // 1 byte (encoding) + datos
        val buffer = ByteArray(HEADER + contentSize)

        // Escribir header del frame (10 bytes)
        // 1. FrameEncoder ID (4 bytes)
        val encodedName = encodeWindows1252(name)
        encodedName.copyInto(buffer, 0)

        // 2. Tamaño del contenido (4 bytes, big-endian)
        buffer[4] = (contentSize shr 24).toByte()
        buffer[5] = (contentSize shr 16).toByte()
        buffer[6] = (contentSize shr 8).toByte()
        buffer[7] = contentSize.toByte()

        // 3. Flags (2 bytes)
        buffer[8] = 0
        buffer[9] = 0

        // Escribir contenido del frame
        buffer[10] = 0 // Encoding (0 = ISO-8859-1)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, 11)
    }
}
