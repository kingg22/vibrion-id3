package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3WriterBuilder.Companion.id3Writer
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
import kotlin.test.assertTrue

class Id3WriterBuilderTest {
    @Test
    fun usingDsl() {
        val writer = id3Writer {
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
        assertNotEquals(0, writer.addTag().size)
    }

    @Test
    fun builderShouldSupportAllDeclaredFrames() {
        val builder = Id3WriterBuilder().apply {
            // --- Strings ---
            title("title")
            album("album")
            subtitle("subtitle")
            albumArtist("albumArtist")
            mediaType("mediaType")
            label("label")
            copyright("copyright")
            isrc("ISRC123")
            key("C#m")
            language("eng")
            lyricist("lyricist")
            contentGroup("group")
            conductor("conductor")
            remixer("remixer")
            track("1/12")
            disc("1/2")
            releaseDate("2107") // 21 July
            isCompilation(true)
            encoderSoftware("MyEncoder 1.0")

            // --- Integers ---
            bpm(128)
            length(300000)
            year(2024)

            // --- Lists ---
            artist("Main", "Feat1", "Feat2")
            genre("Pop", "Electronic")
            composer("Composer1", "Composer2")

            // --- Pairs ---
            involvedPeople("Producer" to "John", "Mixer" to "Anna")

            // --- Lyrics ---
            lyrics { lyrics = "Some unsynced lyrics" }
            syncLyrics { line("Line 1", 1000) }

            // --- Picture ---
            picture {
                data = byteArrayOf(1, 2, 3)
                description = "Cover"
            }

            // --- Comment ---
            comment("Nice song", "eng", "desc")

            // --- Private ---
            privateFrame("com.myapp", byteArrayOf(1, 2, 3, 4))

            // --- User-defined text ---
            userText("MyDesc", "MyValue")

            // --- URLs ---
            artistWeb("https://artist.com")
            fileWeb("https://file.com")
            sourceWeb("https://source.com")
            radioWeb("https://radio.com")
            payWeb("https://pay.com")
            publisherWeb("https://pub.com")
            commercialWeb("https://buy.com")
            copyrightWeb("https://rights.com")
        }

        val generatedTags = builder.valuesForTest.keys.toSet()
        val expectedTags = Id3v2v3TagFrame.allFrames.toSet()

        val missing = expectedTags - generatedTags
        val unexpected = generatedTags - expectedTags

        assertTrue(missing.isEmpty(), "Missing frames: $missing")
        assertTrue(unexpected.isEmpty(), "Unexpected frames: $unexpected")
    }
}
