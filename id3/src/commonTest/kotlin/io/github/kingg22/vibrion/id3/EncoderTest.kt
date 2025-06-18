package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EncoderTest {

    @Test
    fun `test String To Code Points latin`() {
        val actual = strToCodePoints("Hello")
        val expected = listOf(72, 101, 108, 108, 111)
        assertEquals(expected, actual)
    }

    @Test
    fun `test String To Code Points cyrillic`() {
        val actual = strToCodePoints("Привет")
        val expected = listOf(1055, 1088, 1080, 1074, 1077, 1090)
        assertEquals(expected, actual)
    }

    @Test
    fun `test Encode Windows1252 latin`() {
        val actual = encodeWindows1252("Hello")
        val expected = byteArrayOf(72, 101, 108, 108, 111)
        assertTrue(
            actual contentEquals expected,
            "Expected ${expected.joinToString()} but got ${actual.joinToString()}",
        )
    }

    @Test
    fun `test Encode Windows1252 cyrillicLoss`() {
        val actual = encodeWindows1252("Привет")
        val expected = byteArrayOf(31, 64, 56, 50, 53, 66)
        assertTrue(
            actual contentEquals expected,
            "Expected ${expected.joinToString()} but got ${actual.joinToString()}",
        )
    }

    @Test
    fun `test Encode Utf16le latin`() {
        val actual = encodeUtf16LE("Hello")
        val expected = byteArrayOf(72, 0, 101, 0, 108, 0, 108, 0, 111, 0)
        assertTrue(
            actual contentEquals expected,
            "Expected ${expected.joinToString()} but got ${actual.joinToString()}",
        )
    }

    @Test
    fun `test Encode Utf16le cyrillic`() {
        val actual = encodeUtf16LE("Привет")
        val expected = byteArrayOf(31, 4, 64, 4, 56, 4, 50, 4, 53, 4, 66, 4)
        assertTrue(
            actual contentEquals expected,
            "Expected ${expected.joinToString()} but got ${actual.joinToString()}",
        )
    }
}
