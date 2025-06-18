package io.github.kingg22.vibrion.id3.model

enum class SynchronizedLyricsType(val value: Int) {
    Other(0x00),
    Lyrics(0x01),

    /**
     * Text transcription
     */
    TextTranscription(0x02),

    /**
     * Movement/part name (e.g. "Adagio")
     */
    MovementPartName(0x03),

    /**
     * Events (e.g. "Don Quijote enters the stage")
     */
    Events(0x04),

    /**
     * Chord (e.g. "Bb F Fsus")
     */
    Chord(0x05),

    /**
     * Trivia/'pop up' information
     */
    Trivia(0x06),
}
