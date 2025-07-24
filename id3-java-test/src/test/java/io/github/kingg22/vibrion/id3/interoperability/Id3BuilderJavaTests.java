package io.github.kingg22.vibrion.id3.interoperability;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.kingg22.vibrion.id3.AttachedPictureBuilder;
import io.github.kingg22.vibrion.id3.Id3WriterBuilder;
import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame;
import io.github.kingg22.vibrion.id3.Id3AudioWriter;
import io.github.kingg22.vibrion.id3.SynchronizedLyricsBuilder;
import io.github.kingg22.vibrion.id3.UnsynchronisedLyricsBuilder;
import io.github.kingg22.vibrion.id3.model.AttachedPictureType;

class Id3BuilderJavaTests {
    @Test
    void testId3Writer() {
        final var writer = new Id3AudioWriter();
        writer.setPadding(0);
        writer.set(Id3v2v3TagFrame.TIT2.INSTANCE, "Título");
        writer.set(Id3v2v3TagFrame.TPE1.INSTANCE, List.of("Eminem", "50 Cent"));
        writer.set(Id3v2v3TagFrame.IPLS.INSTANCE, Map.of("Guitar", "Eminem", "Vocals", "50 Cent"));
        Assertions.assertNotEquals(0, writer.build().length);
    }

    @Test
    void testBuilderStyle() {
        final var builder = Id3WriterBuilder.id3Writer()
            // --- Strings ---
            .title("Título")
            .album("Album")
            .subtitle("Subtitle")
            .albumArtist("Album Artist")
            .mediaType("CD")
            .label("Label")
            .copyright("Copyright")
            .isrc("ISRC")
            .key("Key")
            .language("Language")
            .lyricist("lyricist")
            .contentGroup("group")
            .conductor("conductor")
            .remixer("remixer")
            .track("1/2")
            .disc("1/2")
            .releaseDate("2107")
            .isCompilation(true)
            .encoderSoftware("MyEncoder 1.0")

            // -- Integers --
            .bpm(128)
            .length(300000)
            .year(2024)

            // -- Lists --
            .artist("Artist 1", "Artist 2")
            .composer("Composer 1", "Composer 2")
            .genre("Genre 1", "Genre 2")

            // -- Pairs --
            .involvedPeople(Map.of("Person 1", "Role 1", "Person 2", "Role 2"))

            // -- Lyrics --
            .lyrics(new UnsynchronisedLyricsBuilder().lyrics("a lyrics"))
            .syncLyrics(new SynchronizedLyricsBuilder().line("a line", 1000).line("sdadk", 12343))

            // -- Picture
            .picture(
                new AttachedPictureBuilder()
                    .type(AttachedPictureType.CoverFront)
                    .data(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})
            )

            // -- Comment --
            .comment("Comentario", "eng", "Description")

            // -- Private --
            .privateFrame("id", new byte[]{})

            // -- User-defined text --
            .userText("Description", "Value")

            // -- URLs --
            .artistWeb("https://google.com")
            .fileWeb("https://google.com")
            .sourceWeb("https://google.com")
            .radioWeb("https://google.com")
            .payWeb("https://google.com")
            .publisherWeb("https://google.com")
            .commercialWeb("https://google.com")
            .copyrightWeb("https://google.com");

        final var generatedTags = builder.getValuesForTest().keySet();
        final var expectedTags = new HashSet<>(Id3v2v3TagFrame.getAllFrames());
        final var missing = expectedTags.parallelStream().filter( i -> !generatedTags.contains(i)).collect(Collectors.toUnmodifiableSet());
        final var unexpected = generatedTags.parallelStream().filter(i -> !expectedTags.contains(i)).collect(Collectors.toUnmodifiableSet());

        Assertions.assertTrue(missing.isEmpty(), "Missing frames: " + missing);
        Assertions.assertTrue(unexpected.isEmpty(), "Unexpected frames: " + unexpected);

        final var writer = builder.build();
        writer.setPadding(0);
        Assertions.assertNotEquals(0, writer.toByteArray().length);
    }
}
