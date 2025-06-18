@file:OptIn(ExperimentalJsStatic::class)

package io.github.kingg22.vibrion.id3

import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

/**
 * ID3 frame types
 *
 * @see <a href="https://id3.org/id3v2.3.0#Declared_ID3v2_frames">ID3v2.3.0 Declared ID3v2 frames</a>
 */
enum class Id3v2FrameType {
    // -- lists --

    /** Song artists */
    TPE1,

    /** Song composers */
    TCOM,

    /** Song genres */
    TCON,
    // -- string --

    /** Language */
    TLAN,

    /** Content group description */
    TIT1,

    /** Song title */
    TIT2,

    /** Song subtitle */
    TIT3,

    /** Album title */
    TALB,

    /** Album artist // spec doesn't say anything about separator, so it is a string, not array */
    TPE2,

    /** Conductor/performer refinement */
    TPE3,

    /** Interpreted, remixed, or otherwise modified by */
    TPE4,

    /** Song number in album: 5 or 5/10 */
    TRCK,

    /** Album disc number: 1 or 1/3 */
    TPOS,

    /** Media type */
    TMED,

    /** Label name */
    TPUB,

    /** Copyright */
    TCOP,

    /** Musical key in which the sound starts */
    TKEY,

    /** Lyricist / text writer */
    TEXT,

    /** Album release date expressed as DDMM */
    TDAT,

    /** Compilation flag ("1" stored as a string) */
    TCMP,

    /** ISRC */
    TSRC,

    // -- integers --

    /** Beats per minute */
    TBPM,

    /** Song duration */
    TLEN,

    /** Album release year */
    TYER,

    // -- Objects --

    /** Unsynchronised lyrics */
    USLT,

    /** Synchronised lyrics */
    SYLT,

    /** Song cover */
    APIC,

    /** User defined text information */
    TXXX,

    /** Comments */
    COMM,

    /** PrivateFrame frame */
    PRIV,
    // -- Urls --

    /** Commercial information */
    WCOM,

    /** Copyright/Legal information */
    WCOP,

    /** Official audio file webpage */
    WOAF,

    /** Official artist/performer webpage */
    WOAR,

    /** Official audio source webpage */
    WOAS,

    /** Official internet radio station homepage */
    WORS,

    /** Payment */
    WPAY,

    /** Publishers official webpage */
    WPUB,

    // -- list of pairs --

    /** Involved people */
    IPLS,

    /** All other frames are unsupported */
    UNKNOWN,
    ;

    companion object {
        @JvmStatic
        @JsStatic
        val listFrames = listOf(TPE1, TCOM, TCON)

        @JvmStatic
        @JsStatic
        val stringFrames = listOf(
            TLAN, TIT1, TIT2, TIT3, TALB, TPE2, TPE3, TPE4, TRCK, TPOS, TMED,
            TPUB, TCOP, TKEY, TEXT, TDAT, TCMP, TSRC,
        )

        @JvmStatic
        @JsStatic
        val numericFrames = listOf(TBPM, TLEN, TYER)

        @JvmStatic
        @JsStatic
        val urlFrames = listOf(WCOM, WCOP, WOAF, WOAR, WOAS, WORS, WPAY, WPUB)

        @JvmStatic
        @JsStatic
        fun fromCode(code: String) = entries.firstOrNull { it.name == code.uppercase() } ?: UNKNOWN
    }
}
