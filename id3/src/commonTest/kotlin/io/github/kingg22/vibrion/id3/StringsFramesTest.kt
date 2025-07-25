package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import io.github.kingg22.vibrion.id3.model.TextFrame
import io.github.kingg22.vibrion.id3.model.UserDefinedText
import kotlin.jvm.JvmStatic
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StringsFramesTest {
    companion object {
        @JvmStatic
        val oneByteEncodedFrames = listOf("TDAT")

        @JvmStatic
        val twoByteEncodedFrames = listOf(
            "TLAN", "TIT1", "TIT2", "TIT3", "TALB",
            "TPE2", "TPE3", "TPE4", "TRCK", "TPOS",
            "TPUB", "TKEY", "TMED", "TSRC", "TCOP",
            "TEXT", "TSSE",
        )

        @JvmStatic
        val urlLinkFrames = listOf("WCOM", "WCOP", "WOAF", "WOAR", "WOAS", "WORS", "WPAY", "WPUB")
    }

    @Test
    fun testUrlLinkFrames() {
        val preComputedExpectedList = listOf(
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 67, 79, 77, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 67, 79, 80, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 79, 65, 70, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 79, 65, 82, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 79, 65, 83, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 79, 82, 83, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 80, 65, 89, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
                87, 80, 85, 66, 0, 0, 0, 18, 0, 0,
                104, 116, 116, 112, 115, 58, 47, 47, 103, 111,
                111, 103, 108, 101, 46, 99, 111, 109,
            ),
        )
        assertEquals(preComputedExpectedList.size, urlLinkFrames.size, "Missing pre computed expected byteArrays")

        urlLinkFrames.forEachIndexed { index, frame ->
            val writer = Id3AudioWriter()
            writer.padding = 0
            writer[frame] = TextFrame("https://google.com")
            val actual = writer.addTag()
            val expected = byteArrayOf(
                *id3Header,
                *encodeSynchsafeInt(28),
                *encodeWindows1252(frame),
                *uint32ToUint8Array(18.toUInt()),
                0,
                0,
                *encodeWindows1252("https://google.com"),
            )
            val preComputedExpected = preComputedExpectedList[index]
            assertContentEquals(
                actual,
                preComputedExpected,
                "URL Frame $frame failed pre-computed",
            )
            assertContentEquals(
                actual,
                expected,
                "URL Frame $frame failed build",
            )
        }
    }

    @Test
    fun testOneByteEncodedFrames() {
        val preComputedExpectedList = listOf(
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 31,
                84, 68, 65, 84, 0, 0, 0, 21, 0, 0,
                0, 76, 121, 114, 105, 99, 105, 115, 116, 47,
                84, 101, 120, 116, 32, 119, 114, 105, 116, 101,
                114,
            ),
        )
        oneByteEncodedFrames.forEachIndexed { index, frame ->
            val writer = Id3AudioWriter()
            writer.padding = 0
            writer[frame] = TextFrame("Lyricist/Text writer")
            val actual = writer.addTag()
            val preComputedExpected = preComputedExpectedList[index]
            val expected = byteArrayOf(
                *id3Header,
                *encodeSynchsafeInt(31),
                *encodeWindows1252(frame),
                *uint32ToUint8Array(21.toUInt()),
                0,
                0,
                0,
                *encodeWindows1252("Lyricist/Text writer"),
            )
            assertContentEquals(
                actual,
                expected,
                "One-byte encoded frame $frame failed.",
            )
            assertContentEquals(
                actual,
                preComputedExpected,
                "One-byte encoded frame $frame failed.",
            )
        }
    }

    @Test
    fun testTwoByteEncodedFrames() {
        // (-1).toByte(), (-2).toByte()
        // 255, 254
        val preComputedExpectedList = listOf(
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 76,
                65, 78, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 73,
                84, 49, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 73,
                84, 50, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 73,
                84, 51, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 65,
                76, 66, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 80,
                69, 50, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 80,
                69, 51, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 80,
                69, 52, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 82,
                67, 75, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 80,
                79, 83, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 80,
                85, 66, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 75,
                69, 89, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 77,
                69, 68, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 83,
                82, 67, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 67,
                79, 80, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 69,
                88, 84, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(), 76,
                0, 121, 0, 114, 0, 105, 0, 99, 0, 105, 0, 115,
                0, 116, 0, 47, 0, 84, 0, 101, 0, 120, 0, 116,
                0, 32, 0, 119, 0, 114, 0, 105, 0, 116, 0, 101,
                0, 114, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 53, 84, 83,
                83, 69, 0, 0, 0, 43, 0, 0, 1, (-1).toByte(), (-2).toByte(),
                76, 0, 121, 0, 114, 0, 105, 0, 99, 0, 105,
                0, 115, 0, 116, 0, 47, 0, 84, 0, 101, 0,
                120, 0, 116, 0, 32, 0, 119, 0, 114, 0, 105,
                0, 116, 0, 101, 0, 114, 0,
            ),
        )
        assertEquals(
            preComputedExpectedList.size,
            twoByteEncodedFrames.size,
            "Missing pre computed expected byteArrays",
        )

        twoByteEncodedFrames.forEachIndexed { index, frame ->
            val writer = Id3AudioWriter()
            writer.padding = 0
            writer[frame] = TextFrame("Lyricist/Text writer")
            val actual = writer.addTag()
            val expected = byteArrayOf(
                *id3Header,
                *encodeSynchsafeInt(53),
                *encodeWindows1252(frame),
                *uint32ToUint8Array(43.toUInt()),
                0, 0, 1,
                0xFF.toByte(),
                0xFE.toByte(),
                *encodeUtf16LE("Lyricist/Text writer"),
            )
            val preComputedExpected = preComputedExpectedList[index]
            assertContentEquals(
                actual,
                preComputedExpected,
                "Two-byte encoded frame $frame failed.",
            )
            assertContentEquals(
                actual,
                expected,
                "Two-byte encoded frame $frame failed.",
            )
        }
    }

    @Test
    fun testUserDefinedUrlFrame() {
        val description = "abcd#"
        val url = "https://google.com"

        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 43, 87, 88,
            88, 88, 0, 0, 0, 33, 0, 0, 1, (-1).toByte(), (-2).toByte(), 97,
            0, 98, 0, 99, 0, 100, 0, 35, 0, 0, 0, 104,
            116, 116, 112, 115, 58, 47, 47, 103, 111, 111, 103, 108,
            101, 46, 99, 111, 109,
        )
        val encodedDescription = description.encodeUtf16LE()
        val encodedUrl = url.encodeWindows1252()

        val contentSize = 1 + bom.size + encodedDescription.size + 2 + encodedUrl.size
        val totalTagSize = 10 + contentSize

        val expected = byteArrayOf(
            *id3Header,
            *encodeSynchsafeInt(totalTagSize), // Size of tag (syncsafe)

            // Frame Header "WXXX"
            *"WXXX".encodeWindows1252(), // Frame ID
            *uint32ToUint8Array(contentSize.toUInt()), // Frame size
            0x00, 0x00, // Flags

            // Frame body
            0x01, // Encoding: UTF-16 with BOM
            *bom, // BOM
            *encodedDescription,
            0x00, 0x00, // UTF-16 null terminator
            *encodedUrl,
        )
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.WXXX] = UserDefinedText(description, url)
        val actual = writer.build()
        assertContentEquals(preComputedExpected, actual, "User-defined URL frame failed in pre-computed format.")
        assertContentEquals(expected, actual, "User-defined URL frame failed.")
    }
}
