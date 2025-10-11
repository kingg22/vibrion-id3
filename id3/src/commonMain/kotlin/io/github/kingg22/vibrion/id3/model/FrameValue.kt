package io.github.kingg22.vibrion.id3.model

import kotlin.jvm.JvmStatic

/**
 * Define Frame Value to use in [io.github.kingg22.vibrion.id3.Id3AudioWriter] set.
 * @author Kingg22
 * @see <a href="https://picard-docs.musicbrainz.org/en/appendices/tag_mapping.html">Tag Mapping</a>
 */
interface FrameValue {
    /** Utilities for [FrameValue] implementations */
    companion object {
        private val languageRegex = Regex("[a-zA-Z]{3}")

        /**
         * Check if language is in ISO 639-2 format.
         * @throws IllegalArgumentException if language is not in ISO 639-2 format
         * @see <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-2_codes">List of ISO 639-2 codes</a>
         * @see <a href="https://id3.org/id3v2.3.0#ID3v2_frame_overview">ID3v2 Frame Overview</a>
         */
        @JvmStatic
        fun languageFormat(language: String) {
            require(language matches languageRegex) { "Language must follow ISO 639-2" }
        }
    }
}
