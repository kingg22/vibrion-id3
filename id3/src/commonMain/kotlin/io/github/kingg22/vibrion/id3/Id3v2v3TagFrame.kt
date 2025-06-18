@file:OptIn(ExperimentalJsStatic::class)

package io.github.kingg22.vibrion.id3

import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

/**
 * ID3 v2.3 frame types
 *
 * @see <a href="https://id3.org/id3v2.3.0#Declared_ID3v2_frames">ID3v2.3.0 Declared ID3v2 frames</a>
 */
sealed interface Id3v2v3TagFrame : Id3TagFrame {
    sealed interface ListStringTagFrame : Id3v2v3TagFrame
    sealed interface TextTagFrame : Id3v2v3TagFrame
    sealed interface IntegerTagFrame : Id3v2v3TagFrame
    sealed interface UrlTagFrame : TextTagFrame
    sealed interface UnsynchronisedLyricsTagFrame : Id3v2v3TagFrame
    sealed interface AttachedPictureTagFrame : Id3v2v3TagFrame
    sealed interface SynchronizedLyricsTagFrame : Id3v2v3TagFrame
    sealed interface UserDefinedTextTagFrame : Id3v2v3TagFrame
    sealed interface CommentTagFrame : Id3v2v3TagFrame
    sealed interface PrivateTagFrame : Id3v2v3TagFrame
    sealed interface PairedTextTagFrame : Id3v2v3TagFrame

    // -- list --

    /** Song artists */
    object TPE1 : ListStringTagFrame {
        override val name = "TPE1"
    }

    /** Song composers */
    object TCOM : ListStringTagFrame {
        override val name = "TCOM"
    }

    /** Song genres */
    object TCON : ListStringTagFrame {
        override val name = "TCON"
    }

    // -- list of pairs --

    /** Involved people */
    object IPLS : PairedTextTagFrame {
        override val name = "IPLS"
    }

    // -- string --

    /** Language */
    object TLAN : TextTagFrame {
        override val name = "TLAN"
    }

    /** Content group description */
    object TIT1 : TextTagFrame {
        override val name = "TIT1"
    }

    /** Song title */
    object TIT2 : TextTagFrame {
        override val name = "TIT2"
    }

    /** Song subtitle */
    object TIT3 : TextTagFrame {
        override val name = "TIT3"
    }

    /** Album title */
    object TALB : TextTagFrame {
        override val name = "TALB"
    }

    /** Album artist */
    object TPE2 : TextTagFrame {
        override val name = "TPE2"
    }

    /** Conductor/performer refinement */
    object TPE3 : TextTagFrame {
        override val name = "TPE3"
    }

    /** Interpreted, remixed, or otherwise modified by */
    object TPE4 : TextTagFrame {
        override val name = "TPE4"
    }

    /** Song number in album: 5 or 5/10 */
    object TRCK : TextTagFrame {
        override val name = "TRCK"
    }

    /** Album disc number: 1 or 1/3 */
    object TPOS : TextTagFrame {
        override val name = "TPOS"
    }

    /** Media type */
    object TMED : TextTagFrame {
        override val name = "TMED"
    }

    /** Label name */
    object TPUB : TextTagFrame {
        override val name = "TPUB"
    }

    /** Copyright */
    object TCOP : TextTagFrame {
        override val name = "TCOP"
    }

    /** Musical key in which the sound starts */
    object TKEY : TextTagFrame {
        override val name = "TKEY"
    }

    /** Lyricist / text writer */
    object TEXT : TextTagFrame {
        override val name = "TEXT"
    }

    /** Album release date expressed as DDMM */
    object TDAT : TextTagFrame {
        override val name = "TDAT"
    }

    /** Compilation flag ("1" stored as a string) */
    object TCMP : TextTagFrame {
        override val name = "TCMP"
    }

    /** ISRC */
    object TSRC : TextTagFrame {
        override val name = "TSRC"
    }

    // -- urls --

    /** Commercial information */
    object WCOM : UrlTagFrame {
        override val name = "WCOM"
    }

    /** Copyright/Legal information */
    object WCOP : UrlTagFrame {
        override val name = "WCOP"
    }

    /** Official audio file webpage */
    object WOAF : UrlTagFrame {
        override val name = "WOAF"
    }

    /** Official artist/performer webpage */
    object WOAR : UrlTagFrame {
        override val name = "WOAR"
    }

    /** Official audio source webpage */
    object WOAS : UrlTagFrame {
        override val name = "WOAS"
    }

    /** Official internet radio station homepage */
    object WORS : UrlTagFrame {
        override val name = "WORS"
    }

    /** Payment */
    object WPAY : UrlTagFrame {
        override val name = "WPAY"
    }

    /** Publishers official webpage */
    object WPUB : UrlTagFrame {
        override val name = "WPUB"
    }

    // -- integers --

    /** Beats per minute BPM */
    object TBPM : IntegerTagFrame {
        override val name = "TBPM"
    }

    /** Song duration */
    object TLEN : IntegerTagFrame {
        override val name = "TLEN"
    }

    /** Album release year */
    object TYER : IntegerTagFrame {
        override val name = "TYER"
    }

    // -- objects --

    /** Unsynchronised lyrics */
    object USLT : UnsynchronisedLyricsTagFrame {
        override val name = "USLT"
    }

    /** Synchronised lyrics */
    object SYLT : SynchronizedLyricsTagFrame {
        override val name = "SYLT"
    }

    /** Song cover */
    object APIC : AttachedPictureTagFrame {
        override val name = "APIC"
    }

    /** User defined text information */
    object TXXX : UserDefinedTextTagFrame {
        override val name = "TXXX"
    }

    /** Comments */
    object COMM : CommentTagFrame {
        override val name = "COMM"
    }

    /** PrivateFrameEncoder frame */
    object PRIV : PrivateTagFrame {
        override val name = "PRIV"
    }

    /** All other frames are unsupported */
    object UNKNOWN : Id3v2v3TagFrame {
        override val name = "UNKNOWN"
    }

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
        val integerFrames = listOf(TBPM, TLEN, TYER)

        @JvmStatic
        @JsStatic
        val urlFrames = listOf(WCOM, WCOP, WOAF, WOAR, WOAS, WORS, WPAY, WPUB)

        @JvmStatic
        @JsStatic
        val objectFrames = listOf(USLT, SYLT, APIC, TXXX, COMM, PRIV)

        @JvmStatic
        @JsStatic
        val stringAndUrlFrames = stringFrames + urlFrames

        @JvmStatic
        @JsStatic
        val listPairFrames = listOf(IPLS)

        @JvmStatic
        @JsStatic
        val allFrames = listFrames + stringAndUrlFrames + integerFrames + listPairFrames + objectFrames

        @JvmStatic
        @JsStatic
        fun fromCode(code: String) = allFrames.firstOrNull { it.name == code.trim().uppercase() } ?: UNKNOWN
    }
}
