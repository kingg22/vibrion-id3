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
import kotlin.test.assertFailsWith

class TxxxFrameTest {

    @Test
    fun testTXXXFrameEncoding() {
        val description = "foo"
        val value = "bar"

        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 29,
            84, 88, 88, 88, 0, 0, 0, 19, 0, 0,
            1, (-1).toByte(), (-2).toByte(), 102, 0, 111, 0, 111, 0, 0,
            0, (-1).toByte(), (-2).toByte(), 98, 0, 97, 0, 114, 0,
        )

        // 1. Encode parts
        val encodedDescription = description.encodeUtf16LE() // f 0x66 0x00, o 0x6F 0x00, o 0x6F 0x00
        val encodedValue = value.encodeUtf16LE() // b 0x62 0x00, a 0x61 0x00, r 0x72 0x00

        // 2. Calculate size
        val contentSize = 1 + bom.size + encodedDescription.size + 2 + bom.size + encodedValue.size
        val totalTagSize = 10 + contentSize

        val expected = byteArrayOf(
            *id3Header,
            *encodeSynchsafeInt(totalTagSize), // tag size

            // --- Frame header ---
            *"TXXX".encodeWindows1252(), // Frame ID
            *uint32ToUint8Array(contentSize.toUInt()), // Frame content size
            0x00, 0x00, // Flags

            // --- Frame body ---
            0x01, // Encoding byte: UTF-16
            *bom, // BOM
            *encodedDescription, // UTF-16LE "foo"
            0x00, 0x00, // Null separator (UTF-16)
            *bom, // Second BOM
            *encodedValue, // UTF-16LE "bar"
        )

        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.TXXX] = UserDefinedText(description = "foo", value = "bar")
        val actual = writer.toByteArray()

        assertContentEquals(preComputedExpected, actual)
        assertContentEquals(expected, actual)
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
