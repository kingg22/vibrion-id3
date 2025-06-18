package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class IplsFrameTest {
    @Test
    fun `should write IPLS frame correctly`() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0
        writer["IPLS"] = FrameValue.PairedTextFrame(
            "author" to "Thomas Bangalter",
            "author" to "Guy-Manuel de Homem-Christo",
            "mixer" to "DJ Falcon",
        )
        val actual = writer.addTag()
        /*
        val encodingByte = 0x01.toByte() // UTF-16 with BOM
        val bomBytes = byteArrayOf(0xFF.toByte(), 0xFE.toByte()) // UTF-16 LE BOM
        val nullTerminator = byteArrayOf(0, 0) // UTF-16 null terminator

        val expectedFrameContArrayOf(encodingByte) +
        bomBytes +
        "author".encodeUtf16LE() + nullTerminator + bomBytes +
        "Thomas Bangalter".encodeUtf16LE() + nullTerminator +
        "author".encodeUtf16LE() + nullTerminator + bomBytes +
        "Guy-Manuel de Homem-Christo".encodeUtf16LE() + nullTerminator +
        "mixer".encodeUtf16LE() + nullTerminator + bomBytes +
        "DJ Falcon".encodeUtf16LE() + nullTerminator

        val frameContentSize = expectedFrameContent.size // 163 bytes
        val totalIPLSFrameBytes = "IPLS".encodeWindows1252() +
        uint32ToUint8Array(frameContentSize) +
        byteArrayOf(0, 0) + // Flags
        expectedFrameContent

        val expected = id3Header +
        encodent(totalIPLSFrameBytes.size) + // 173 bytes
        totalIPLSFrameBytes
         */

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
        assertEquals(183, preComputedExpected.size)
// assertEquals(183, expected.size)

        assertContentEquals(preComputedExpected, actual)
// assertContentEquals(expected, actual)
    }
}
