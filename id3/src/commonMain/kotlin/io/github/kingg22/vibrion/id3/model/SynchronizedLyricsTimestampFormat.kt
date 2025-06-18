package io.github.kingg22.vibrion.id3.model

enum class SynchronizedLyricsTimestampFormat(val value: Int) {
    /**
     * Absolute time, 32 bit sized, using MPEG frames as unit
     */
    Frames(0x01),

    /**
     * Absolute time, 32 bit sized, using milliseconds as unit
     */
    Milliseconds(0x02),
}
