package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import io.github.kingg22.vibrion.id3.model.IntegerFrame
import kotlin.jvm.JvmStatic
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class IntegersFramesTest {
    companion object {
        @JvmStatic
        val integerFrames = listOf("TLEN", "TYER", "TBPM")
    }

    @Test
    fun testIntegerFrames() {
        val preComputedExpectedList = listOf(
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0,
                0, 15, 84, 76, 69, 78, 0, 0,
                0, 5, 0, 0, 0, 50, 48, 50,
                51,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0,
                0, 15, 84, 89, 69, 82, 0, 0,
                0, 5, 0, 0, 0, 50, 48, 50,
                51,
            ),
            byteArrayOf(
                73, 68, 51, 3, 0, 0, 0, 0,
                0, 15, 84, 66, 80, 77, 0, 0,
                0, 5, 0, 0, 0, 50, 48, 50,
                51,
            ),
        )
        assertEquals(preComputedExpectedList.size, integerFrames.size, "Missing pre-computed expected byteArrays")

        integerFrames.forEachIndexed { index, frameName ->
            val writer = Id3AudioWriter()
            writer.padding = 0
            writer[frameName] = IntegerFrame(2023)
            val actual = writer.addTag()
            val expected = byteArrayOf(
                *id3Header,
                *encodeSynchsafeInt(15), // tag size without header
                *encodeWindows1252(frameName),
                *uint32ToUint8Array(5.toUInt()), // frame size without header
                0,
                0, // flags
                0, // encoding
                *encodeWindows1252("2023"),
            )
            val preComputedExpected = preComputedExpectedList[index]
            assertContentEquals(
                actual,
                preComputedExpected,
                "Integer frame $frameName failed. Actual: '${actual.contentToString()}' Pre-computed expected: '${preComputedExpected.contentToString()}'",
            )
            assertContentEquals(
                actual,
                expected,
                "Integer frame $frameName failed. Actual: '${actual.contentToString()}' Expected: '${expected.contentToString()}'",
            )
        }
    }
}
