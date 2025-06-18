package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.*
import io.github.kingg22.vibrion.id3.model.*
import kotlin.test.Test
import kotlin.test.assertFails

class Id3AudioWriterTest {
    @Test
    fun `set string frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[TIT2] = "My Title"
            this[WOAR] = "https://example.com"
        }
    }

    @Test
    fun `set integer frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[TLEN] = 12345
            this[TYER] = 2022
        }
    }

    @Test
    fun `set list of strings frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[TPE1] = listOf("Artist 1", "Artist 2")
            this[TCON] = listOf("Pop", "Rock")
        }
    }

    @Test
    fun `set pair list frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[IPLS] = listOf("Guitar" to "John", "Drums" to "Mike")
        }
    }

    @Test
    fun `set UnsynchronisedLyrics frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[USLT] = UnsynchronisedLyrics("Lyrics here", "desc", "eng")
        }
    }

    @Test
    fun `set AttachedPicture frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[APIC] = AttachedPicture(3, byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 1, 2, 3), "cover")
        }
    }

    @Test
    fun `set SynchronizedLyrics frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[SYLT] =
                SynchronizedLyrics(
                    SynchronizedLyricsType.Lyrics,
                    SynchronizedLyricsTimestampFormat.Milliseconds,
                    emptyList(),
                    "eng",
                    "desc",
                )
        }
    }

    @Test
    fun `set UserDefinedText frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[TXXX] = UserDefinedText("key", "value")
        }
    }

    @Test
    fun `set Comment frame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[COMM] = CommentFrame("eng", "note")
        }
    }

    @Test
    fun `set PrivateFrame`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[PRIV] = PrivateFrame("owner", byteArrayOf(1, 2, 3))
        }
    }

    @Test
    fun `set pair as user defined`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[TXXX] = "key" to "value"
        }
    }

    @Test
    fun `set pair as private`() {
        id3AudioWriter(getEmptyBuffer()) {
            this[PRIV] = "owner" to byteArrayOf(0x01, 0x02)
        }
    }

    @Test
    fun `set FrameValue with string id that maps to unknown`() {
        assertFails {
            id3AudioWriter(getEmptyBuffer()) {
                this["ZZZZ"] = TextFrame("should fail")
            }
        }
    }
}
