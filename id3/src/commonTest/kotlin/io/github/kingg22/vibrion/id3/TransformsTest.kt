package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import io.github.kingg22.vibrion.id3.internal.uint7ArrayToUint28
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TransformsTest {

    @Test
    fun testUint32ToUint8_255() {
        val actual = uint32ToUint8Array(255u)
        val expected = byteArrayOf(0, 0, 0, 255.toByte())
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32ToUint8_256() {
        val actual = uint32ToUint8Array(256u)
        val expected = byteArrayOf(0, 0, 1, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32ToUint8_257() {
        val actual = uint32ToUint8Array(257u)
        val expected = byteArrayOf(0, 0, 1, 1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32_2Pow16() {
        val actual = uint32ToUint8Array(65536u)
        val expected = byteArrayOf(0, 1, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32_2Pow24() {
        val actual = uint32ToUint8Array(16777216u)
        val expected = byteArrayOf(1, 0, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32_Max() {
        val actual = uint32ToUint8Array(UInt.MAX_VALUE)
        val expected = byteArrayOf(255.toByte(), 255.toByte(), 255.toByte(), 255.toByte())
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint32_Overflow() {
        val actual = uint32ToUint8Array(0u)
        val expected = byteArrayOf(0, 0, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_127() {
        val actual = encodeSynchsafeInt(127)
        val expected = byteArrayOf(0, 0, 0, 127)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_128() {
        val actual = encodeSynchsafeInt(128)
        val expected = byteArrayOf(0, 0, 1, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_129() {
        val actual = encodeSynchsafeInt(129)
        val expected = byteArrayOf(0, 0, 1, 1)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_2Pow14() {
        val actual = encodeSynchsafeInt(16384)
        val expected = byteArrayOf(0, 1, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_2Pow21() {
        val actual = encodeSynchsafeInt(2097152)
        val expected = byteArrayOf(1, 0, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_Max() {
        val actual = encodeSynchsafeInt(268435455)
        val expected = byteArrayOf(127, 127, 127, 127)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint28_Overflow() {
        val actual = encodeSynchsafeInt(0)
        val expected = byteArrayOf(0, 0, 0, 0)
        assertContentEquals(expected, actual)
    }

    @Test
    fun testUint7ArrayToUint28_127() {
        val actual = uint7ArrayToUint28(listOf(0, 0, 0, 127))
        assertEquals(127, actual)
    }

    @Test
    fun testUint7ArrayToUint28_128() {
        val actual = uint7ArrayToUint28(listOf(0, 0, 1, 0))
        assertEquals(128, actual)
    }

    @Test
    fun testUint7ArrayToUint28_129() {
        val actual = uint7ArrayToUint28(listOf(0, 0, 1, 1))
        assertEquals(129, actual)
    }

    @Test
    fun testUint7ArrayToUint28_2Pow14() {
        val actual = uint7ArrayToUint28(listOf(0, 1, 0, 0))
        assertEquals(16384, actual)
    }

    @Test
    fun testUint7ArrayToUint28_2Pow21() {
        val actual = uint7ArrayToUint28(listOf(1, 0, 0, 0))
        assertEquals(2097152, actual)
    }

    @Test
    fun testUint7ArrayToUint28_Max() {
        val actual = uint7ArrayToUint28(listOf(127, 127, 127, 127))
        assertEquals(268435455, actual)
    }
}
