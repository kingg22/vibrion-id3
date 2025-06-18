package io.github.kingg22.vibrion.id3

/**
 * @see <a href="https://id3.org/id3v2.3.0#ID3v2_frame_overview">ID3v2 Frame Overview</a>
 */
sealed interface Id3TagFrame {
    /** Aka Frame ID (four characters) */
    val name: String
}
