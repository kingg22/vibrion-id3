package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3AudioWriter.Companion.id3AudioWriter
import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.*
import io.github.kingg22.vibrion.id3.model.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class Id3AudioWriterTest {
    @Test
    fun testSetStringFrame() {
        id3AudioWriter {
            this[TIT2] = "My Title"
            this[WOAR] = "https://example.com"
        }
    }

    @Test
    fun testSetIntegerFrame() {
        id3AudioWriter {
            this[TLEN] = 12345
            this[TYER] = 2022
        }
    }

    @Test
    fun testSetListOfStringsFrame() {
        id3AudioWriter {
            this[TPE1] = listOf("Artist 1", "Artist 2")
            this[TCON] = listOf("Pop", "Rock")
        }
    }

    @Test
    fun testSetPairListFrame() {
        id3AudioWriter {
            this[IPLS] = listOf("Guitar" to "John", "Drums" to "Mike")
        }
    }

    @Test
    fun testSetUnsynchronisedLyricsFrame() {
        id3AudioWriter {
            this[USLT, "lyrics", "description"] = "eng"
            this[USLT] = UnsynchronisedLyrics("Lyrics here", "desc", "eng")
        }
    }

    @Test
    fun testSetAttachedPictureFrame() {
        id3AudioWriter {
            this[APIC] = AttachedPicture(3, byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 1, 2, 3), "cover")
        }
    }

    @Test
    fun testSetSynchronizedLyricsFrame() {
        id3AudioWriter {
            this[SYLT] = SynchronizedLyrics(
                SynchronizedLyricsType.Lyrics,
                SynchronizedLyricsTimestampFormat.Milliseconds,
                emptyList(),
                "eng",
                "desc",
            )
        }
    }

    @Test
    fun testSetUserDefinedTextFrame() {
        id3AudioWriter {
            this[TXXX, "key"] = "value"
            this[TXXX] = UserDefinedText("key", "value")
        }
    }

    @Test
    fun testSetCommentFrame() {
        id3AudioWriter {
            this[COMM] = CommentFrame("eng", "note")
        }
    }

    @Test
    fun testSetPrivateFrame() {
        id3AudioWriter {
            this[PRIV, "id"] = byteArrayOf(1, 2, 3)
            this[PRIV] = PrivateFrame("owner", byteArrayOf(1, 2, 3))
        }
    }

    @Test
    fun testSetPairAsUserDefined() {
        id3AudioWriter {
            this[TXXX] = "key" to "value"
        }
    }

    @Test
    fun testSetPairAsPrivate() {
        id3AudioWriter {
            this[PRIV] = "owner" to byteArrayOf(0x01, 0x02)
        }
    }

    @Test
    fun testSetFrameValueWithStringIdThatMapsToUnknown() {
        assertFailsWith<IllegalArgumentException> {
            id3AudioWriter {
                this["ZZZZ"] = TextFrame("should fail")
            }
        }
    }
}
