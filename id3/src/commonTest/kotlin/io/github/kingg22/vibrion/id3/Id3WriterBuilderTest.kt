package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3WriterBuilder.Companion.id3writerBuilder
import io.github.kingg22.vibrion.id3.model.AttachedPicture
import io.github.kingg22.vibrion.id3.model.AttachedPictureType
import io.github.kingg22.vibrion.id3.model.CommentFrame
import io.github.kingg22.vibrion.id3.model.PrivateFrame
import io.github.kingg22.vibrion.id3.model.SynchronizedLyrics
import io.github.kingg22.vibrion.id3.model.SynchronizedLyricsTimestampFormat
import io.github.kingg22.vibrion.id3.model.SynchronizedLyricsType
import io.github.kingg22.vibrion.id3.model.UserDefinedText
import kotlin.test.Test
import kotlin.test.assertNotEquals

class Id3WriterBuilderTest {
    @Test
    fun usingDsl() {
        val writer = id3writerBuilder(getEmptyBuffer()) {
            title = "Hello"
            album = "Album"
            year = 2022
            bpm = 120
            composer = listOf("Composer 1", "Composer 2")
            subtitle = "Subtitle"
            albumArtist = "Album Artist"
            mediaType = "CD"
            label = "Label"
            copyright = "Copyright"
            isrc = "ISRC"
            key = "Key"
            language = "Language"
            length = 120
            artists = listOf("Artist 1", "Artist 2")
            genres = listOf("Genre 1", "Genre 2")
            involvedPeople = listOf("Person 1" to "Role 1", "Person 2" to "Role 2")
            artistWeb = "https://google.com"
            fileWeb = "https://google.com"
            sourceWeb = "https://google.com"
            radioWeb = "https://google.com"
            payWeb = "https://google.com"
            publisherWeb = "https://google.com"
            commercialWeb = "https://google.com"
            copyrightWeb = "https://google.com"
            syncLyrics = SynchronizedLyrics(
                SynchronizedLyricsType.Lyrics,
                SynchronizedLyricsTimestampFormat.Milliseconds,
                listOf("Una línea" to 1000),
                "eng",
                "",
            )
            picture =
                AttachedPicture(
                    AttachedPictureType.CoverFront,
                    byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()),
                    "",
                    true,
                )
            comment = CommentFrame("Description", "Comentario", "eng")
            privateFrame = PrivateFrame("ID", byteArrayOf())
            userText = UserDefinedText("Description", "Value")
            involvedPeople("Person 1" to "Role 1", "Person 2" to "Role 2")
            artist("Name")
            genre("Jazz", "Funk")
            comment("Comentario general")
            lyrics {
                lyrics = "Lyrics"
            }
            syncLyrics {
                line("Una línea", 1000)
            }
            picture {
                type = AttachedPictureType.CoverFront
                data = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
            }
            privateFrame("ID", byteArrayOf())
            userText("Description", "Value")
        }
        writer.padding = 0
        writer.addTag()
        assertNotEquals(0, writer.arrayBuffer.size)
    }
}
