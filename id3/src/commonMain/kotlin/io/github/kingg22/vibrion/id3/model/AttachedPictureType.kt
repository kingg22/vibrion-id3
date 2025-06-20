package io.github.kingg22.vibrion.id3.model

/**
 * Attached picture types.
 *
 * _There may be several pictures attached to one file._
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.APIC
 * @see AttachedPicture
 * @see io.github.kingg22.vibrion.id3.Id3WriterBuilder.picture
 * @see io.github.kingg22.vibrion.id3.AttachedPictureBuilder
 * @see <a href="https://id3.org/id3v2.3.0#Attached_picture">Id3v2.3.0 - Attached picture</a>
 */
enum class AttachedPictureType(val value: Int) {
    /** Other */
    Other(0x00),

    /** 32x32 pixels 'file icon' (PNG only) */
    Icon(0x01),

    /** Other file icon */
    OtherIcon(0x02),

    /** Cover (front) */
    CoverFront(0x03),

    /** Cover (back) */
    CoverBack(0x04),

    /** Leaflet page */
    Leaflet(0x05),

    /** Media (e.g. label side of CD) */
    Media(0x06),

    /** Lead artist/lead performer/soloist */
    LeadArtist(0x07),

    /** Artist/performer */
    Artist(0x08),

    /** Conductor */
    Conductor(0x09),

    /** Band/Orchestra */
    Band(0x0a),

    /** Composer */
    Composer(0x0b),

    /** Lyricist/text writer */
    Lyricist(0x0c),

    /** Recording location */
    RecordingLocation(0x0d),

    /** During recording */
    DuringRecording(0x0e),

    /** During performance */
    DuringPerformance(0x0f),

    /** Movie/video screen capture */
    MovieScreenCapture(0x10),

    /** A brightly coloured fish */
    BrightColouredFish(0x11),

    /** Illustration */
    Illustration(0x12),

    /** Band/artist logotype */
    BandLogotype(0x13),

    /** Publisher/Studio logotype */
    PublisherLogotype(0x14),
}
