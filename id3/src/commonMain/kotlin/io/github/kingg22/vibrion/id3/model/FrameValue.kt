package io.github.kingg22.vibrion.id3.model

/**
 * Define Frame Value to use in [io.github.kingg22.vibrion.id3.Id3AudioWriter] set.
 * @author Kingg22
 * @see <a href="https://picard-docs.musicbrainz.org/en/appendices/tag_mapping.html">Tag Mapping</a>
 */
sealed class FrameValue {
    protected fun languageFormat(language: String) = require(language.matches(Regex("[a-zA-Z]{3}"))) {
        "Language must follow ISO 639-2"
    }
}
