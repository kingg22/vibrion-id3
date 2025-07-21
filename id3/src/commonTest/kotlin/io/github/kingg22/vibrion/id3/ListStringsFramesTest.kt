package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import io.github.kingg22.vibrion.id3.model.StringListFrame
import kotlin.jvm.JvmStatic
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ListStringsFramesTest {
    companion object {
        @JvmStatic
        val frames = listOf("TPE1", "TCOM", "TCON")
    }

    @Test
    fun testStringListFrame() {
        // (-1).toByte(), (-2).toByte()
        // 255, 254
        val preComputedExpectedList = listOf(
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 41, 84,
                80, 69, 49, 0, 0, 0, 31, 0, 0, 1, (-1).toByte(),
                (-2).toByte(), 69, 0, 109, 0, 105, 0, 110, 0, 101, 0,
                109, 0, 47, 0, 53, 0, 48, 0, 32, 0, 67,
                0, 101, 0, 110, 0, 116, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 41, 84,
                67, 79, 77, 0, 0, 0, 31, 0, 0, 1, (-1).toByte(),
                (-2).toByte(), 69, 0, 109, 0, 105, 0, 110, 0, 101, 0,
                109, 0, 47, 0, 53, 0, 48, 0, 32, 0,
                67, 0, 101, 0, 110, 0, 116, 0,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0, 0, 41, 84,
                67, 79, 78, 0, 0, 0, 31, 0, 0, 1, (-1).toByte(),
                (-2).toByte(), 69, 0, 109, 0, 105, 0, 110, 0, 101, 0,
                109, 0, 59, 0, 53, 0, 48, 0, 32, 0, 67,
                0, 101, 0, 110, 0, 116, 0,
            ),
        )
        assertEquals(preComputedExpectedList.size, frames.size, "Missing pre computed expected byteArrays")

        frames.forEachIndexed { index, frameName ->
            val delemiter = if (frameName == "TCON") ';' else '/'
            val writer = Id3AudioWriter()
            writer.padding = 0
            writer[frameName] = StringListFrame("Eminem", "50 Cent")
            val actual = writer.addTag()
            val expected = byteArrayOf(
                *id3Header,
                *encodeSynchsafeInt(41), // tag size without header
                *encodeWindows1252(frameName),
                *uint32ToUint8Array(31.toUInt()), // frame size without header
                0,
                0, // flags
                1, // encoding
                0xFF.toByte(), // 255 → -1
                0xFE.toByte(), // 254 → -2 BOM
                *encodeUtf16LE("Eminem${delemiter}50 Cent"),
            )
            val precomputedExpected = preComputedExpectedList[index]
            assertContentEquals(
                actual,
                precomputedExpected,
                "String List Frame $frameName failed. Actual: '${actual.contentToString()}' Pre-computed expected: '${precomputedExpected.contentToString()}'",
            )
            assertContentEquals(
                actual,
                expected,
                "String List Frame $frameName failed. Actual: '${actual.contentToString()}' Expected: '${expected.contentToString()}'",
            )
        }
    }
}
