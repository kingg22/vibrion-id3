package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame
import io.github.kingg22.vibrion.id3.bom
import io.github.kingg22.vibrion.id3.id3Header
import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.test.Test
import kotlin.test.assertContentEquals

class IplsFrameTest {
    @Test
    fun testShouldWriteIPLSFrameCorrectly() {
        val pairs = listOf(
            "author" to "Thomas Bangalter",
            "author" to "Guy-Manuel de Homem-Christo",
            "mixer" to "DJ Falcon",
        )

        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.IPLS] = PairedTextFrame(*pairs.toTypedArray())
        val actual = writer.addTag()

        val encodedPairs = pairs.flatMap { (role, name) ->
            listOf(
                bom.toList(),
                role.encodeUtf16LE().toList(),
                listOf<Byte>(0x00, 0x00), // separator
                bom.toList(),
                name.encodeUtf16LE().toList(),
                listOf<Byte>(0x00, 0x00), // separator after value
            ).flatten()
        }

        val contentSize = 1 + encodedPairs.size
        val totalTagSize = 10 + contentSize

        val expected = byteArrayOf(
            *id3Header,
            *encodeSynchsafeInt(totalTagSize),
            *Id3v2v3TagFrame.IPLS.name.encodeWindows1252(),
            *uint32ToUint8Array(contentSize.toUInt()),
            0x00,
            0x00, // Frame flags
            0x01, // Encoding: UTF-16
            *encodedPairs.toByteArray(),
        )
        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 1, 45, 73, 80,
            76, 83, 0, 0, 0, (-93).toByte(), 0, 0, 1, (-1).toByte(), (-2).toByte(),
            97, 0, 117, 0, 116, 0, 104, 0, 111, 0, 114, 0, 0, 0, (-1).toByte(), (-2).toByte(),
            84, 0, 104, 0, 111, 0, 109, 0, 97, 0, 115, 0, 32, 0, 66, 0, 97, 0, 110, 0, 103, 0,
            97, 0, 108, 0, 116, 0, 101, 0, 114, 0, 0, 0, (-1).toByte(), (-2).toByte(), 97, 0,
            117, 0, 116, 0, 104, 0, 111, 0, 114, 0, 0, 0, (-1).toByte(), (-2).toByte(), 71, 0, 117, 0, 121,
            0, 45, 0, 77, 0, 97, 0, 110, 0, 117, 0, 101, 0, 108, 0, 32, 0, 100, 0, 101, 0, 32, 0, 72, 0, 111, 0, 109,
            0, 101, 0, 109, 0, 45, 0, 67, 0, 104, 0, 114, 0, 105, 0, 115, 0, 116, 0, 111, 0, 0, 0, (-1).toByte(),
            (-2).toByte(), 109, 0, 105, 0, 120, 0, 101, 0, 114, 0, 0, 0, (-1).toByte(), (-2).toByte(), 68, 0, 74, 0,
            32, 0, 70, 0, 97, 0, 108, 0, 99, 0, 111, 0, 110, 0, 0, 0,
        )

        assertContentEquals(preComputedExpected, actual)
        assertContentEquals(expected, actual)
    }
}
