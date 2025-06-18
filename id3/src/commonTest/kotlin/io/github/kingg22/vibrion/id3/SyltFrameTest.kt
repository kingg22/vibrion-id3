package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SyltFrameTest {
    @Test
    fun `should write SYLT frame correctly`() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0

        writer["SYLT"] = FrameValue.SynchronisedLyrics(
            type = 1,
            timestampFormat = 2,
            text = listOf(
                "She's up all night 'til the sun" to 1,
                "I'm up all night to get some" to 2,
                "She's up all night for good fun" to 3,
                "I'm up all night to get lucky" to 4,
            ),
            language = "eng",
            description = "Description",
        )

        val actual = writer.addTag()

        val expected = byteArrayOf(
            // id3Header + size
            *id3Header,
            *encodeSynchsafeInt(312),
            *encodeWindows1252("SYLT"),
            *uint32ToUint8Array(302),
            0x00,
            0x00, // flags
            0x01, // encoding (UTF-16 with BOM)
            *encodeWindows1252("eng"),
            0x02, // timestamp format
            0x01, // content type
            0xff.toByte(),
            0xfe.toByte(), // BOM
            *encodeUtf16LE("Description"),
            0x00,
            0x00,
            // Cada línea de texto + timestamp
            0xff.toByte(), 0xfe.toByte(),
            *encodeUtf16LE("She's up all night 'til the sun"),
            0x00, 0x00,
            0x00, 0x00, 0x00, 0x01,

            0xff.toByte(), 0xfe.toByte(),
            *encodeUtf16LE("I'm up all night to get some"),
            0x00, 0x00,
            0x00, 0x00, 0x00, 0x02,

            0xff.toByte(), 0xfe.toByte(),
            *encodeUtf16LE("She's up all night for good fun"),
            0x00, 0x00,
            0x00, 0x00, 0x00, 0x03,

            0xff.toByte(), 0xfe.toByte(),
            *encodeUtf16LE("I'm up all night to get lucky"),
            0x00, 0x00,
            0x00, 0x00, 0x00, 0x04,
        )

        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 2, 56, 83, 89, 76, 84, 0, 0, 1, 46, 0, 0, 1, 101, 110, 103, 2, 1, (-1).toByte(),
            (-2).toByte(), 68, 0, 101, 0, 115, 0, 99, 0, 114, 0, 105, 0, 112, 0, 116, 0, 105, 0, 111, 0, 110, 0, 0, 0,
            (-1).toByte(), (-2).toByte(), 83, 0, 104, 0, 101, 0, 39, 0, 115, 0, 32, 0, 117, 0, 112, 0, 32, 0, 97, 0,
            108, 0, 108, 0, 32, 0, 110, 0, 105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 39, 0, 116, 0, 105, 0, 108, 0, 32, 0,
            116, 0, 104, 0, 101, 0, 32, 0, 115, 0, 117, 0, 110, 0, 0, 0, 0, 0, 0, 1, (-1).toByte(), (-2).toByte(), 73,
            0, 39, 0, 109, 0, 32, 0, 117, 0, 112, 0, 32, 0, 97, 0, 108, 0, 108, 0, 32, 0, 110, 0, 105, 0, 103, 0, 104,
            0, 116, 0, 32, 0, 116, 0, 111, 0, 32, 0, 103, 0, 101, 0, 116, 0, 32, 0, 115, 0, 111, 0, 109, 0, 101, 0, 0,
            0, 0, 0, 0, 2, (-1).toByte(), (-2).toByte(), 83, 0, 104, 0, 101, 0, 39, 0, 115, 0, 32, 0, 117, 0, 112, 0,
            32, 0, 97, 0, 108, 0, 108, 0, 32, 0, 110, 0, 105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 102, 0, 111, 0, 114, 0,
            32, 0, 103, 0, 111, 0, 111, 0, 100, 0, 32, 0, 102, 0, 117, 0, 110, 0, 0, 0, 0, 0, 0, 3, (-1).toByte(),
            (-2).toByte(), 73, 0, 39, 0, 109, 0, 32, 0, 117, 0, 112, 0, 32, 0, 97, 0, 108, 0, 108, 0, 32, 0, 110, 0,
            105, 0, 103, 0, 104, 0, 116, 0, 32, 0, 116, 0, 111, 0, 32, 0, 103, 0, 101, 0, 116, 0, 32, 0, 108, 0, 117, 0,
            99, 0, 107, 0, 121, 0, 0, 0, 0, 0, 0, 4,
        )
        assertEquals(preComputedExpected.size, expected.size)

        assertContentEquals(preComputedExpected, actual)
        assertContentEquals(expected, actual)
    }
}
