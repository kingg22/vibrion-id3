@file:JvmSynthetic
@file:JvmName("-NumericFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class NumericFrameEncoder internal constructor(
    @get:JvmSynthetic override val name: String,
    @get:JvmSynthetic val value: String,
    @get:JvmSynthetic override val size: Int,
) : FrameEncoder(name, size) {
    internal constructor(name: String, value: Int, size: Int) : this(name, value.toString(), size)

    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        // Escribir header del frame (10 bytes)
        // 1. FrameEncoder ID (4 bytes)
        name.forEachIndexed { i, char ->
            buffer[offset + i] = char.code.toByte()
        }

        // 2. Tamaño del contenido (4 bytes, big-endian)
        val contentSize = 1 + value.length // 1 byte (encoding) + datos
        buffer[offset + 4] = (contentSize shr 24).toByte()
        buffer[offset + 5] = (contentSize shr 16).toByte()
        buffer[offset + 6] = (contentSize shr 8).toByte()
        buffer[offset + 7] = contentSize.toByte()

        // 3. Flags (2 bytes)
        buffer[offset + 8] = 0
        buffer[offset + 9] = 0

        // Escribir contenido del frame
        buffer[offset + 10] = 0 // Encoding (0 = ISO-8859-1)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, offset + 11)

        return HEADER + contentSize // Tamaño total del frame
    }
}
