@file:JvmSynthetic
@file:JvmName("-PairedTextFrameEncoder")

package io.github.kingg22.vibrion.id3.internal.frames

import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

@ConsistentCopyVisibility
internal data class PairedTextFrameEncoder private constructor(
    @get:JvmSynthetic @field:JvmSynthetic override val name: String,
    @get:JvmSynthetic @field:JvmSynthetic override val size: Int,
    private val value: List<Pair<String, String>>,
) : FrameEncoder(name, size) {
    @JvmSynthetic
    override fun writeTo(buffer: ByteArray, offset: Int): Int {
        val pairsSize = value.sumOf { (first, second) ->
            2 + encodeUtf16LE(first).size + 2 + 2 + encodeUtf16LE(second).size + 2
        }
        val contentSize = 1 + pairsSize

        var currentOffset = writeFrameHeader(buffer, offset)

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

        return HEADER + contentSize
    }

    internal companion object {
        @JvmSynthetic
        internal fun PairedTextFrameEncoder(name: String, value: List<Pair<String, String>>, size: Int) =
            PairedTextFrameEncoder(name, size, value)
    }
}
