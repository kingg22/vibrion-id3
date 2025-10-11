@file:JvmSynthetic
@file:JvmName("-NumericFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun NumericFrameEncoder(name: String, value: String, size: Int): FrameEncoder =
    NumericFrameEncoder(name, size, value)

private class NumericFrameEncoder(name: String, size: Int, value: String) : FrameEncoder(name, size) {
    override val encodedFrame: ByteArray by lazy {
        val buffer = ByteArray(size)
        writeFrameHeader(buffer)
        // Escribir contenido del frame
        buffer[10] = 0 // Encoding (0 = ISO-8859-1)
        val encoded = encodeWindows1252(value)
        encoded.copyInto(buffer, 11)
    }
}
