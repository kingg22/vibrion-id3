@file:JvmMultifileClass
@file:JvmSynthetic
@file:JvmName("-PairedTextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@Suppress("ktlint:standard:function-naming", "FunctionName")
@JvmSynthetic
internal fun PairedTextFrameEncoder(name: String, value: List<Pair<String, String>>, size: Int): FrameEncoder =
    PairedTextFrameEncoder(name, size, value)

private class PairedTextFrameEncoder(name: String, size: Int, value: List<Pair<String, String>>) :
    FrameEncoder(name, size) {

    override val encodedFrame: ByteArray by lazy {
        val contentSize = 1 + value.sumOf { (first, second) ->
            2 + encodeUtf16LE(first).size + 2 + 2 + encodeUtf16LE(second).size + 2
        }
        val buffer = ByteArray(HEADER + contentSize)

        var currentOffset = writeFrameHeader(buffer)

        // Encoding
        buffer[currentOffset++] = 1

        // Pairs
        value.forEach { (role, name) ->
            // BOM + role
            BOM.copyInto(buffer, currentOffset)
            currentOffset += BOM.size
            val roleBytes = encodeUtf16LE(role)
            roleBytes.copyInto(buffer, currentOffset)
            currentOffset += roleBytes.size

            // Separator + BOM + name
            buffer[currentOffset++] = 0
            buffer[currentOffset++] = 0
            BOM.copyInto(buffer, currentOffset)
            currentOffset += BOM.size
            val nameBytes = encodeUtf16LE(name)
            nameBytes.copyInto(buffer, currentOffset)
            currentOffset += nameBytes.size

            // Separator
            buffer[currentOffset++] = 0
            buffer[currentOffset++] = 0
        }

        buffer
    }
}
