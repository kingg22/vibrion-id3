package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import io.github.kingg22.vibrion.id3.id3Header
import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.test.Test
import kotlin.test.assertContentEquals

class UsltFrameTest {

    @Test
    fun testUSLT_DefaultLanguage() {
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer["USLT"] = UnsynchronisedLyrics(
            description = "Ярл", // Ярл
            lyrics = "Лирика", // Лирика
        )
        val actual = writer.addTag()
        val expected = byteArrayOf(
            *id3Header,
            *encodeSynchsafeInt(38),
            *encodeWindows1252("USLT"),
            *uint32ToUint8Array(28),
            0, 0,
            1, // UTF-16LE
            *encodeWindows1252("eng"),
            0xFF.toByte(), 0xFE.toByte(), // BOM
            *encodeUtf16LE("Ярл"),
            0, 0,
            0xFF.toByte(), 0xFE.toByte(),
            *encodeUtf16LE("Лирика"),
        )
        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 38, 85,
            83, 76, 84, 0, 0, 0, 28, 0, 0, 1, 101,
            110, 103, (-1).toByte(), (-2).toByte(), 47, 4, 64, 4, 59, 4, 0,
            0, (-1).toByte(), (-2).toByte(), 27, 4, 56, 4, 64, 4, 56, 4,
            58, 4, 48, 4,
        )

        assertContentEquals(preComputedExpected, actual, "USLT frame failed pre-computed")
        assertContentEquals(expected, actual, "USLT frame failed")
    }

    @Test
    fun testUSLT_ChangedLanguage() {
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer["USLT"] = UnsynchronisedLyrics(
            language = "rus",
            description = "Ярл", // Ярл
            lyrics = "Лирика", // Лирика
        )
        val actual = writer.addTag()

        val expected = byteArrayOf(
            *id3Header,
            *encodeSynchsafeInt(38),
            *encodeWindows1252("USLT"),
            *uint32ToUint8Array(28.toUInt()),
            0, 0,
            1, // UTF-16LE
            *encodeWindows1252("rus"),
            0xFF.toByte(), 0xFE.toByte(), // BOM
            *encodeUtf16LE("Ярл"),
            0, 0,
            0xFF.toByte(), 0xFE.toByte(),
            *encodeUtf16LE("Лирика"),
        )

        assertContentEquals(expected, actual, "USLT frame with custom language failed")
    }
}
