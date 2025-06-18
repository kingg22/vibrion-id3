package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class TxxxFrameTest {

    @Test
    fun `test TXXX Frame Encoding`() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0
        writer["TXXX"] = FrameValue.UserDefinedText(description = "foo", value = "bar")
        val actual = writer.addTag()

        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 29,
            84, 88, 88, 88, 0, 0, 0, 19, 0, 0,
            1, (-1).toByte(), (-2).toByte(), 102, 0, 111, 0, 111, 0, 0,
            0, (-1).toByte(), (-2).toByte(), 98, 0, 97, 0, 114, 0,
        )
        /*
        val expected = buildList {
            addAll(id3Header.toList())
            addAll("TXXX".encodeToByteArray().toMutableList())
            addAll(encodeSynchsafeInt(19).toList()) // Frame size without header
            add(0)
            add(0) // Frame flags
            add(1) // Encoding UTF-16
            addAll(bom.toMutableList()) // BOM
            addAll(encodeUtf16LE("foo").toMutableList())
            add(0)
            add(0) // Separator
            addAll(bom.toMutableList()) // BOM
            addAll(encodeUtf16LE("bar").toMutableList())
        }.toByteArray()
         */

        assertContentEquals(preComputedExpected, actual)
        // assertContentEquals(expected, actual)
    }

    @Test
    fun testTXXXWithSimpleStringThrows() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = FrameValue.TextFrame("foobar")
        }
    }

    @Test
    fun testTXXXWithNoDescriptionThrows() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = FrameValue.UserDefinedText(description = "", value = "foobar")
        }
    }

    @Test
    fun testTXXXWithNoValueThrows() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = FrameValue.UserDefinedText(description = "foobar", value = "")
        }
    }
}
