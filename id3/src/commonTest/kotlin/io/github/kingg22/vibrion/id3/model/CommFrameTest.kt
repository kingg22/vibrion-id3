package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import io.github.kingg22.vibrion.id3.Id3v2FrameType
import io.github.kingg22.vibrion.id3.getEmptyBuffer
import io.github.kingg22.vibrion.id3.id3Header
import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeUtf16LE
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.test.Test
import kotlin.test.assertContentEquals

class CommFrameTest {

    @Test
    fun testDefaultCommFrame() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0
        writer[Id3v2FrameType.COMM] = CommentFrame(
            description = "advert",
            text = "free hugs",
        )
        writer.addTag()
        val actual = writer.arrayBuffer

        val expected = buildList {
            addAll(id3Header.toList())
            addAll(encodeSynchsafeInt(50).toList()) // tag size
            addAll(encodeWindows1252("COMM").toList())
            addAll(uint32ToUint8Array(40).toList()) // frame size
            add(0) // flag byte 1
            add(0) // flag byte 2
            add(1) // encoding
            addAll(encodeWindows1252("eng").toList())
            addAll(byteArrayOf(0xFF.toByte(), 0xFE.toByte()).toList()) // BOM
            addAll(encodeUtf16LE("advert").toList())
            add(0) // 0x00 UTF-16 separator
            add(0) // 0x00 second byte of UTF-16 null
            addAll(byteArrayOf(0xFF.toByte(), 0xFE.toByte()).toList()) // BOM
            addAll(encodeUtf16LE("free hugs").toList())
        }.toByteArray()

        assertContentEquals(expected, actual)
    }

    @Test
    fun testCustomLangCommFrame() {
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0
        writer["COMM"] = CommentFrame(
            description = "この世界",
            text = "俺の名前",
            language = "jpn",
        )
        writer.addTag()
        val actual = writer.arrayBuffer

        val expected = buildList {
            addAll(id3Header.toList())
            addAll(encodeSynchsafeInt(36).toList()) // tag size
            addAll(encodeWindows1252("COMM").toList())
            addAll(uint32ToUint8Array(26).toList()) // frame size
            add(0) // flag byte 1
            add(0) // flag byte 2
            add(1) // encoding
            addAll(encodeWindows1252("jpn").toList())
            addAll(byteArrayOf(0xFF.toByte(), 0xFE.toByte()).toList()) // BOM
            addAll(encodeUtf16LE("この世界").toList())
            add(0)
            add(0)
            addAll(byteArrayOf(0xFF.toByte(), 0xFE.toByte()).toList()) // BOM
            addAll(encodeUtf16LE("俺の名前").toList())
        }.toByteArray()

        assertContentEquals(expected, actual)
    }
}
