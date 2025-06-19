package io.github.kingg22.vibrion.id3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import io.github.kingg22.vibrion.id3.model.AttachedPictureType;

class Id3BuilderJavaTest {
    @Test
    void testBuilderStyle() {
        final var writer = Id3WriterBuilder.id3writer(new byte[]{})
            .title("Título")
            .artist("Artista")
            .picture(new AttachedPictureBuilder().type(AttachedPictureType.CoverFront).data(new byte[]{
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF
            }))
            .album("Album")
            .year(2022)
            .bpm(120)
            .composer("Composer 1", "Composer 2")
            .subtitle("Subtitle")
            .albumArtist("Album Artist")
            .mediaType("CD")
            .label("Label")
            .copyright("Copyright")
            .isrc("ISRC")
            .key("Key")
            .language("Language")
            .length(120)
            .artist("Artist 1", "Artist 2")
            .genre("Genre 1", "Genre 2")
            .involvedPeople(Map.of("Person 1", "Role 1", "Person 2", "Role 2"))
            .artistWeb("https://google.com")
            .fileWeb("https://google.com")
            .sourceWeb("https://google.com")
            .radioWeb("https://google.com")
            .payWeb("https://google.com")
            .publisherWeb("https://google.com")
            .commercialWeb("https://google.com")
            .copyrightWeb("https://google.com")
            .comment("Comentario", "eng", "Description")
            .privateFrame("id", new byte[]{})
            .userText("Description", "Value")
            .lyrics(new UnsynchronisedLyricsBuilder().lyrics("a lyrics"))
            .syncLyrics(new SynchronizedLyricsBuilder().line("a line", 1000).line("sdadk", 12343))
            .build();
        writer.setPadding(0);
        writer.addTag();
        Assertions.assertNotEquals(0, writer.getArrayBuffer().length);
    }
}
