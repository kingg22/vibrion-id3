@file:JvmSynthetic
@file:JvmName("-Transforms")

package io.github.kingg22.vibrion.id3.internal

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

// Inspired on https://github.com/egoroof/browser-id3-writer

@JvmSynthetic
internal fun uint32ToUint8Array(value: Int) = uint32ToUint8Array(value.toUInt())

@JvmSynthetic
internal fun uint32ToUint8Array(value: UInt) = byteArrayOf(
    ((value shr 24) and 0xFFu).toByte(),
    ((value shr 16) and 0xFFu).toByte(),
    ((value shr 8) and 0xFFu).toByte(),
    (value and 0xFFu).toByte(),
)

/**
 * Equivalent to uint28ToUint7Array
 */
@JvmSynthetic
internal fun encodeSynchsafeInt(value: Int): ByteArray {
    require(value shr 28 == 0) { "Value must be 28-bit (less than 2^28)" }

    return byteArrayOf(
        ((value shr 21) and 0x7F).toByte(),
        ((value shr 14) and 0x7F).toByte(),
        ((value shr 7) and 0x7F).toByte(),
        (value and 0x7F).toByte(),
    )
}

@JvmSynthetic
internal fun uint7ArrayToUint28(bytes: List<Int>): Int {
    require(bytes.size == 4) { "Array must have exactly 4 bytes" }

    return (bytes[0] shl 21) or
        (bytes[1] shl 14) or
        (bytes[2] shl 7) or
        bytes[3]
}
