package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame
import io.github.kingg22.vibrion.id3.id3Header
import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.jvm.JvmStatic
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class ApicFrameTest {
    companion object {
        @JvmStatic
        val imageContent = byteArrayOf(4, 8, 15, 16, 23, 42)
    }

    @Test
    fun jpeg() = testApic("image/jpeg", buildImage(0xFF, 0xD8, 0xFF))

    @Test
    fun jpegUnicode() = testApic("image/jpeg", buildImage(0xFF, 0xD8, 0xFF), unicode = true)

    @Test
    fun png() = testApic("image/png", buildImage(0x89, 0x50, 0x4E, 0x47))

    @Test
    fun gif() = testApic("image/gif", buildImage(0x47, 0x49, 0x46))

    @Test
    fun webp() = testApic("image/webp", buildImage(0, 0, 0, 0, 0, 0, 0, 0, 0x57, 0x45, 0x42, 0x50))

    @Test
    fun tiff1() = testApic("image/tiff", buildImage(0x49, 0x49, 0x2A, 0))

    @Test
    fun tiff2() = testApic("image/tiff", buildImage(0x4D, 0x4D, 0, 0x2A))

    @Test
    fun bmp() = testApic("image/bmp", buildImage(0x42, 0x4D))

    @Test
    fun icon() = testApic("image/x-icon", buildImage(0, 0, 1, 0))

    @Test
    fun testApicJpegFrameWithUnicode() {
        val signature = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        val fullImage = signature + imageContent

        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.APIC] = AttachedPicture(
            type = AttachedPictureType.CoverFront,
            data = fullImage,
            description = "yo",
            useUnicodeEncoding = true,
        )
        val actual = writer.addTag()

        val expected = buildList {
            addAll(id3Header.toList())
            addAll(encodeSynchsafeInt(40).toList()) // total tag size
            addAll(encodeWindows1252("APIC").toMutableList())
            addAll(uint32ToUint8Array(30).toMutableList()) // frame size
            add(0) // flag byte 1
            add(0) // flag byte 2
            add(1) // encoding = UTF-16
            addAll(encodeWindows1252("image/jpeg").toMutableList())
            add(0) // MIME null separator
            add(3) // PictureFrame type
            add(0xFF.toByte())
            add(0xFE.toByte()) // BOM for UTF-16LE
            addAll(encodeUtf16LE("yo").toMutableList())
            add(0) // null terminator UTF-16
            add(0)
            addAll(fullImage.toMutableList())
        }.toByteArray()

        assertContentEquals(expected, actual)
    }

    @Test
    fun forceWesternEncodingWhenDescriptionEmpty() {
        val signature = buildImage(0, 0, 1, 0)
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.APIC] = AttachedPicture(
            type = 3,
            data = signature,
            description = "",
            useUnicodeEncoding = true,
        )
        val actual = writer.addTag()
        val expected = expectedApicFrame("image/x-icon", signature.sliceArray(0..3), "", unicode = false)
        assertContentEquals(expected, actual)
    }

    @Test
    fun throwsWhenPictureTypeIsInvalid() {
        assertFailsWith<IllegalArgumentException> {
            AttachedPicture(type = 43, data = ByteArray(0))
        }
    }

    private fun buildImage(vararg bytes: Int): ByteArray = bytes.map { it.toByte() }.toByteArray() + imageContent

    private fun expectedApicFrame(
        mime: String,
        signature: ByteArray,
        description: String,
        unicode: Boolean = false,
    ): ByteArray {
        val encoding = if (unicode && description.isNotEmpty()) 1 else 0
        val mimeBytes = encodeWindows1252(mime)
        val descBytes = if (encoding == 1) {
            byteArrayOf(0xFF.toByte(), 0xFE.toByte()) + encodeUtf16LE(description) + byteArrayOf(0, 0)
        } else {
            encodeWindows1252(description) + byteArrayOf(0)
        }
        val payload =
            byteArrayOf(encoding.toByte()) + mimeBytes + byteArrayOf(0) + byteArrayOf(3) + descBytes + signature +
                imageContent

        return buildList {
            addAll(id3Header.toList())
            addAll(encodeSynchsafeInt(payload.size + 10).toList())
            addAll(encodeWindows1252("APIC").toMutableList())
            addAll(uint32ToUint8Array(payload.size).toMutableList())
            add(0)
            add(0)
            addAll(payload.toList())
        }.toByteArray()
    }

    private fun testApic(mime: String, signature: ByteArray, unicode: Boolean = false) {
        val writer = Id3AudioWriter()
        writer.padding = 0
        writer[Id3v2v3TagFrame.APIC] = AttachedPicture(
            type = 3,
            data = signature + imageContent,
            description = "yo",
            useUnicodeEncoding = unicode,
        )
        val actual = writer.addTag()
        val expected = expectedApicFrame(mime, signature, "yo", unicode)
        assertContentEquals(expected, actual)
    }
}
