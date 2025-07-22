package io.github.kingg22.vibrion.id3

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
    data object TPE1 : ListStringTagFrame {
        override val name = "TPE1"
    }

    /** Song composers */
    data object TCOM : ListStringTagFrame {
        override val name = "TCOM"
    }

    /** Song genres */
    data object TCON : ListStringTagFrame {
        override val name = "TCON"
    }

    // -- list of pairs --

    /** Involved people */
    data object IPLS : PairedTextTagFrame {
        override val name = "IPLS"
    }

    // -- string --

    /** Software/Hardware and settings used for encoding */
    data object TSSE : TextTagFrame {
        override val name = "TSSE"
    }

    /** Language */
    data object TLAN : TextTagFrame {
        override val name = "TLAN"
    }

    /** Content group description */
    data object TIT1 : TextTagFrame {
        override val name = "TIT1"
    }

    /** Song title */
    data object TIT2 : TextTagFrame {
        override val name = "TIT2"
    }

    /** Song subtitle */
    data object TIT3 : TextTagFrame {
        override val name = "TIT3"
    }

    /** Album title */
    data object TALB : TextTagFrame {
        override val name = "TALB"
    }

    /** Album artist */
    data object TPE2 : TextTagFrame {
        override val name = "TPE2"
    }

    /** Conductor/performer refinement */
    data object TPE3 : TextTagFrame {
        override val name = "TPE3"
    }

    /** Interpreted, remixed, or otherwise modified by */
    data object TPE4 : TextTagFrame {
        override val name = "TPE4"
    }

    /** Song number in album: 5 or 5/10 */
    data object TRCK : TextTagFrame {
        override val name = "TRCK"
    }

    /** Album disc number: 1 or 1/3 */
    data object TPOS : TextTagFrame {
        override val name = "TPOS"
    }

    /** Media type */
    data object TMED : TextTagFrame {
        override val name = "TMED"
    }

    /** Label name */
    data object TPUB : TextTagFrame {
        override val name = "TPUB"
    }

    /** Copyright */
    data object TCOP : TextTagFrame {
        override val name = "TCOP"
    }

    /** Musical key in which the sound starts */
    data object TKEY : TextTagFrame {
        override val name = "TKEY"
    }

    /** Lyricist / text writer */
    data object TEXT : TextTagFrame {
        override val name = "TEXT"
    }

    /** Album release date expressed as DDMM */
    data object TDAT : TextTagFrame {
        override val name = "TDAT"
    }

    /** Compilation flag ("1" stored as a string) */
    data object TCMP : TextTagFrame {
        override val name = "TCMP"
    }

    /** ISRC */
    data object TSRC : TextTagFrame {
        override val name = "TSRC"
    }

    // -- urls --

    /** Commercial information */
    data object WCOM : UrlTagFrame {
        override val name = "WCOM"
    }

    /** Copyright/Legal information */
    data object WCOP : UrlTagFrame {
        override val name = "WCOP"
    }

    /** Official audio file webpage */
    data object WOAF : UrlTagFrame {
        override val name = "WOAF"
    }

    /** Official artist/performer webpage */
    data object WOAR : UrlTagFrame {
        override val name = "WOAR"
    }

    /** Official audio source webpage */
    data object WOAS : UrlTagFrame {
        override val name = "WOAS"
    }

    /** Official internet radio station homepage */
    data object WORS : UrlTagFrame {
        override val name = "WORS"
    }

    /** Payment */
    data object WPAY : UrlTagFrame {
        override val name = "WPAY"
    }

    /** Publishers official webpage */
    data object WPUB : UrlTagFrame {
        override val name = "WPUB"
    }

    // -- integers --

    /** Beats per minute BPM */
    data object TBPM : IntegerTagFrame {
        override val name = "TBPM"
    }

    /** Song duration */
    data object TLEN : IntegerTagFrame {
        override val name = "TLEN"
    }

    /** Album release year */
    data object TYER : IntegerTagFrame {
        override val name = "TYER"
    }

    // -- objects --

    /** Unsynchronised lyrics */
    data object USLT : UnsynchronisedLyricsTagFrame {
        override val name = "USLT"
    }

    /** Synchronised lyrics */
    data object SYLT : SynchronizedLyricsTagFrame {
        override val name = "SYLT"
    }

    /** Song cover */
    data object APIC : AttachedPictureTagFrame {
        override val name = "APIC"
    }

    /** User defined text information */
    data object TXXX : UserDefinedTextTagFrame {
        override val name = "TXXX"
    }

    /** Comments */
    data object COMM : CommentTagFrame {
        override val name = "COMM"
    }

    /** PrivateFrameEncoder frame */
    data object PRIV : PrivateTagFrame {
        override val name = "PRIV"
    }

    /** All other frames are unsupported */
    data object UNKNOWN : Id3v2v3TagFrame {
        override val name = "UNKNOWN"
    }

    companion object {
        @JvmStatic
        val listFrames = listOf(TPE1, TCOM, TCON)

        @JvmStatic
        val stringFrames = listOf(
            TLAN, TIT1, TIT2, TIT3, TALB, TPE2, TPE3, TPE4, TRCK, TPOS, TMED,
            TPUB, TCOP, TKEY, TEXT, TDAT, TCMP, TSRC, TSSE,
        )

        @JvmStatic
        val integerFrames = listOf(TBPM, TLEN, TYER)

        @JvmStatic
        val urlFrames = listOf(WCOM, WCOP, WOAF, WOAR, WOAS, WORS, WPAY, WPUB)

        @JvmStatic
        val objectFrames = listOf(USLT, SYLT, APIC, TXXX, COMM, PRIV)

        @JvmStatic
        val stringAndUrlFrames = stringFrames + urlFrames

        @JvmStatic
        val listPairFrames = listOf(IPLS)

        @JvmStatic
        val allFrames = listFrames + stringAndUrlFrames + integerFrames + listPairFrames + objectFrames

        /**
         * Get [Id3v2v3TagFrame] from the name (or code) of the frame.
         *
         * @param code Name of the frame. Must be four char and uppercase.
         * @return [Id3v2v3TagFrame] or [UNKNOWN] if not found.
         * @see Id3v2v3TagFrame.name
         * @see Id3v2v3TagFrame.allFrames
         */
        @JvmStatic
        fun fromCode(code: String) = allFrames.firstOrNull { it.name == code.trim().uppercase() } ?: UNKNOWN
    }
}
