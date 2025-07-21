package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class TxxxFrameTest {

    @Test
    fun testTXXXFrameEncoding() {
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer["TXXX"] = UserDefinedText(description = "foo", value = "bar")
        val actual = writer.addTag()

        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 29,
            84, 88, 88, 88, 0, 0, 0, 19, 0, 0,
            1, (-1).toByte(), (-2).toByte(), 102, 0, 111, 0, 111, 0, 0,
            0, (-1).toByte(), (-2).toByte(), 98, 0, 97, 0, 114, 0,
        )
        // TODO generate expected programmatic

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
        val writer = Id3AudioWriter()
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = TextFrame("foobar")
        }
    }

    @Test
    fun testTXXXWithNoDescriptionThrows() {
        val writer = Id3AudioWriter()
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = UserDefinedText(description = "", value = "foobar")
        }
    }

    @Test
    fun testTXXXWithNoValueThrows() {
        val writer = Id3AudioWriter()
        assertFailsWith<IllegalArgumentException> {
            writer["TXXX"] = UserDefinedText(description = "foobar", value = "")
        }
    }
}
