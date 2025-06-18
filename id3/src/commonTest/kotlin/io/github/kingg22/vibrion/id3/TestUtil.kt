@file:JvmName("TestUtil")
@file:JvmMultifileClass

package io.github.kingg22.vibrion.id3

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

val id3Header = byteArrayOf(
    73,
    68,
    51, // "ID3"
    3,
    0, // Version
    0, // Flags
)

val bom = byteArrayOf(0xFF.toByte(), 0xFE.toByte())

fun getEmptyBuffer() = ByteArray(0)
