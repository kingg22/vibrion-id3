package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.*
import io.github.kingg22.vibrion.id3.model.*
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Builder for [Id3AudioWriter], aka _a verbose builder for tag frame_.
 *
 * All properties and function correspond to a [Id3v2v3TagFrame] supported.
 *
 * _Note_: Current is impossible to delete a value from the builder.
 * If you need to do that, create a [Id3WriterBuilder.copy] before and modify it, or only don't set the value.
 *
 * **For advanced user** with knowledge in tags frame of the Id3 tag,
 * you can use [Id3AudioWriter.id3AudioWriter] directly, it is type safe with all overloads of [Id3AudioWriter.set].
 *
 * Example:
 * ```kotlin
 * val track = ...
 * val tag: ByteArray = Id3WriterBuilder.id3Writer {
 *     title = track.title
 *     artist(track.artist.name)
 *     album = track.album?.title
 *     bpm = track.bpm?.toInt()
 *     length = track.duration
 *     year = track.releaseDate?.year
 * }.toByteArray()
 * ```
 * Java builder style:
 * ```java
 * class Id3BuilderJavaTest {
 *     void testBuilderStyle() {
 *         final byte[] tag = Id3WriterBuilder.id3Writer()
 *             .title("Título")
 *             .artist("Artista")
 *             .picture(new AttachedPictureBuilder().type(AttachedPictureType.CoverFront).data(new byte[]{
 *                 (byte) 0xFF, (byte) 0xD8, (byte) 0xFF
 *             }))
 *             .toByteArray();
 *      }
 * }
 * ```
 *
 * @see Id3AudioWriter
 * @see Id3v2v3TagFrame
 * @see Id3WriterBuilder.build
 * @see Id3WriterBuilder.toByteArray
 * @see Id3WriterBuilder.id3Writer
 */
@Id3Dsl
class Id3WriterBuilder {
    private val values = mutableListOf<Pair<Id3v2v3TagFrame, FrameValue>>()

    // --- Strings ---
    var title: String? = null
        set(value) {
            field = value
            value?.let { values += TIT2 to TextFrame(it) }
        }

    var album: String? = null
        set(value) {
            field = value
            value?.let { values += TALB to TextFrame(it) }
        }

    var subtitle: String? = null
        set(value) {
            field = value
            value?.let { values += TIT3 to TextFrame(it) }
        }

    var albumArtist: String? = null
        set(value) {
            field = value
            value?.let { values += TPE2 to TextFrame(it) }
        }

    var mediaType: String? = null
        set(value) {
            field = value
            value?.let { values += TMED to TextFrame(it) }
        }

    var label: String? = null
        set(value) {
            field = value
            value?.let { values += TPUB to TextFrame(it) }
        }

    var copyright: String? = null
        set(value) {
            field = value
            value?.let { values += TCOP to TextFrame(it) }
        }

    var isrc: String? = null
        set(value) {
            field = value
            value?.let { values += TSRC to TextFrame(it) }
        }

    var key: String? = null
        set(value) {
            field = value
            value?.let { values += TKEY to TextFrame(it) }
        }

    var language: String? = null
        set(value) {
            field = value
            value?.let { values += TLAN to TextFrame(it) }
        }

    // --- Integers ---
    var bpm: Int? = null
        set(value) {
            field = value
            value?.let { values += TBPM to IntegerFrame(it) }
        }

    var length: Int? = null
        set(value) {
            field = value
            value?.let { values += TLEN to IntegerFrame(it) }
        }

    var year: Int? = null
        set(value) {
            field = value
            value?.let { values += TYER to IntegerFrame(it) }
        }

    // -- List of strings --
    var artists: List<String>? = null
        set(value) {
            field = value
            value?.let { values += TPE1 to StringListFrame(it) }
        }

    var genres: List<String>? = null
        set(value) {
            field = value
            value?.let { values += TCON to StringListFrame(it) }
        }

    var composer: List<String>? = null
        set(value) {
            field = value
            value?.let { values += TCOM to StringListFrame(it) }
        }

    // --- Pairs ---
    var involvedPeople: List<Pair<String, String>>? = null
        set(value) {
            field = value
            value?.let { values += IPLS to PairedTextFrame(it) }
        }

    // --- Lyrics ---
    var lyrics: UnsynchronisedLyrics? = null
        set(value) {
            field = value
            value?.let { values += USLT to it }
        }

    var syncLyrics: SynchronizedLyrics? = null
        set(value) {
            field = value
            value?.let { values += SYLT to it }
        }

    // --- Picture ---
    var picture: AttachedPicture? = null
        set(value) {
            field = value
            value?.let { values += APIC to it }
        }

    // --- Comment ---
    var comment: CommentFrame? = null
        set(value) {
            field = value
            value?.let { values += COMM to it }
        }

    // --- Private ---
    var privateFrame: PrivateFrame? = null
        set(value) {
            field = value
            value?.let { values += PRIV to it }
        }

    // --- User defined ---
    var userText: UserDefinedText? = null
        set(value) {
            field = value
            value?.let { values += TXXX to it }
        }

    // -- Web URLs --
    var artistWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WOAR to TextFrame(it) }
        }
    var fileWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WOAF to TextFrame(it) }
        }

    var sourceWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WOAS to TextFrame(it) }
        }
    var radioWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WORS to TextFrame(it) }
        }
    var payWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WPAY to TextFrame(it) }
        }

    var publisherWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WPUB to TextFrame(it) }
        }

    var commercialWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WCOM to TextFrame(it) }
        }
    var copyrightWeb: String? = null
        set(value) {
            field = value
            value?.let { values += WCOP to TextFrame(it) }
        }

    // ---- Builder style ----

    // --- Strings ---
    fun title(title: String) = apply { values += TIT2 to TextFrame(title) }
    fun album(album: String) = apply { values += TALB to TextFrame(album) }
    fun subtitle(subtitle: String) = apply { values += TIT3 to TextFrame(subtitle) }
    fun albumArtist(albumArtist: String) = apply { values += TPE2 to TextFrame(albumArtist) }
    fun mediaType(mediaType: String) = apply { values += TMED to TextFrame(mediaType) }
    fun label(label: String) = apply { values += TPUB to TextFrame(label) }
    fun copyright(copyright: String) = apply { values += TCOP to TextFrame(copyright) }
    fun isrc(isrc: String) = apply { values += TSRC to TextFrame(isrc) }
    fun key(key: String) = apply { values += TKEY to TextFrame(key) }
    fun language(language: String) = apply { values += TLAN to TextFrame(language) }

    // --- Integers ---
    fun bpm(bpm: Int) = apply { values += TBPM to IntegerFrame(bpm) }
    fun length(length: Int) = apply { values += TLEN to IntegerFrame(length) }
    fun year(year: Int) = apply { values += TYER to IntegerFrame(year) }

    // -- List of strings --
    fun artist(name: String, vararg names: String) = apply {
        values += TPE1 to StringListFrame(listOf(name, *names))
    }

    fun genre(genre: String, vararg genres: String) = apply { values += TCON to StringListFrame(genre, *genres) }
    fun composer(composer: String, vararg composers: String) = apply {
        values +=
            TCOM to StringListFrame(composer, *composers)
    }

    // --- Pairs ---
    fun involvedPeople(people: Map<String, String>) = apply { values += IPLS to PairedTextFrame(people) }
    fun involvedPeople(people: Pair<String, String>, vararg peoples: Pair<String, String>) = apply {
        values += IPLS to PairedTextFrame(people, *peoples)
    }

    // --- Lyrics ---

    /** Need [UnsynchronisedLyricsBuilder.lyrics] */
    fun lyrics(block: UnsynchronisedLyricsBuilder.() -> Unit) = apply {
        val builder = UnsynchronisedLyricsBuilder().apply(block)
        values += USLT to builder.build()
    }

    /** Need [UnsynchronisedLyricsBuilder.lyrics] */
    fun lyrics(builder: UnsynchronisedLyricsBuilder) = apply { values += USLT to builder.build() }

    fun syncLyrics(block: SynchronizedLyricsBuilder.() -> Unit) = apply {
        val builder = SynchronizedLyricsBuilder().apply(block)
        values += SYLT to builder.build()
    }

    fun syncLyrics(builder: SynchronizedLyricsBuilder) = apply { values += SYLT to builder.build() }

    // --- Picture ---
    fun picture(block: AttachedPictureBuilder.() -> Unit) = apply {
        val builder = AttachedPictureBuilder().apply(block)
        values += APIC to builder.build()
    }

    fun picture(builder: AttachedPictureBuilder) = apply { values += APIC to builder.build() }

    // --- Comment ---
    @JvmOverloads
    fun comment(text: String, lang: String = "eng", description: String = "") = apply {
        values += COMM to CommentFrame(language = lang, description = description, text = text)
    }

    // --- Private ---
    fun privateFrame(id: String, data: ByteArray) = apply {
        values += PRIV to PrivateFrame(id, data)
    }

    // --- User defined ---
    fun userText(description: String, value: String) = apply {
        values += TXXX to UserDefinedText(description, value)
    }

    // --- Web URLs ---
    fun artistWeb(url: String) = apply { values += WOAR to TextFrame(url) }
    fun fileWeb(url: String) = apply { values += WOAF to TextFrame(url) }
    fun sourceWeb(url: String) = apply { values += WOAS to TextFrame(url) }
    fun radioWeb(url: String) = apply { values += WORS to TextFrame(url) }
    fun payWeb(url: String) = apply { values += WPAY to TextFrame(url) }
    fun publisherWeb(url: String) = apply { values += WPUB to TextFrame(url) }
    fun commercialWeb(url: String) = apply { values += WCOM to TextFrame(url) }
    fun copyrightWeb(url: String) = apply { values += WCOP to TextFrame(url) }

    /**
     * Build the [Id3AudioWriter] instance and set all the tags.
     *
     * You need to call [Id3AudioWriter.addTag] manually.
     * @return [Id3AudioWriter] instance
     * @see Id3AudioWriter
     */
    fun build() = Id3AudioWriter().apply {
        val types = this@Id3WriterBuilder.values.map { it.first }
        this[types] = this@Id3WriterBuilder.values.map { it.second }
    }

    /**
     * Shortcut for [build] and [Id3AudioWriter.toByteArray]
     * @param padding Padding to add to the end of the [ByteArray].
     */
    @JvmOverloads
    fun toByteArray(padding: Int = 4096) = build().apply {
        this.padding = padding
    }.toByteArray()

    companion object {
        /** Retrieve the [Id3WriterBuilder] instance, can be used for builder style u others. */
        @JvmStatic
        fun id3Writer() = Id3WriterBuilder()

        /** Create a new [Id3AudioWriter] using a DSL of [Id3WriterBuilder]. */
        @JvmStatic
        fun id3Writer(block: Id3WriterBuilder.() -> Unit) = Id3WriterBuilder().apply(block).build()
    }

    @KoverIgnore
    override fun toString(): String = "Id3WriterBuilder()"

    /**
     * Generate a copy of this builder.
     *
     * _The new instance doesn't have values on properties, only copy the value frames._
     */
    fun copy() = Id3WriterBuilder().apply {
        this.values.addAll(this@Id3WriterBuilder.values)
    }

    @Suppress("kotlin:S3776")
    @KoverIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Id3WriterBuilder

        if (bpm != other.bpm) return false
        if (length != other.length) return false
        if (year != other.year) return false
        if (values != other.values) return false
        if (title != other.title) return false
        if (album != other.album) return false
        if (subtitle != other.subtitle) return false
        if (albumArtist != other.albumArtist) return false
        if (mediaType != other.mediaType) return false
        if (label != other.label) return false
        if (copyright != other.copyright) return false
        if (isrc != other.isrc) return false
        if (key != other.key) return false
        if (language != other.language) return false
        if (artists != other.artists) return false
        if (genres != other.genres) return false
        if (composer != other.composer) return false
        if (involvedPeople != other.involvedPeople) return false
        if (lyrics != other.lyrics) return false
        if (syncLyrics != other.syncLyrics) return false
        if (picture != other.picture) return false
        if (comment != other.comment) return false
        if (privateFrame != other.privateFrame) return false
        if (userText != other.userText) return false
        if (artistWeb != other.artistWeb) return false
        if (fileWeb != other.fileWeb) return false
        if (sourceWeb != other.sourceWeb) return false
        if (radioWeb != other.radioWeb) return false
        if (payWeb != other.payWeb) return false
        if (publisherWeb != other.publisherWeb) return false
        if (commercialWeb != other.commercialWeb) return false
        if (copyrightWeb != other.copyrightWeb) return false

        return true
    }

    @KoverIgnore
    override fun hashCode(): Int {
        var result = bpm ?: 0
        result = 31 * result + (length ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + values.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (album?.hashCode() ?: 0)
        result = 31 * result + (subtitle?.hashCode() ?: 0)
        result = 31 * result + (albumArtist?.hashCode() ?: 0)
        result = 31 * result + (mediaType?.hashCode() ?: 0)
        result = 31 * result + (label?.hashCode() ?: 0)
        result = 31 * result + (copyright?.hashCode() ?: 0)
        result = 31 * result + (isrc?.hashCode() ?: 0)
        result = 31 * result + (key?.hashCode() ?: 0)
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + (artists?.hashCode() ?: 0)
        result = 31 * result + (genres?.hashCode() ?: 0)
        result = 31 * result + (composer?.hashCode() ?: 0)
        result = 31 * result + (involvedPeople?.hashCode() ?: 0)
        result = 31 * result + (lyrics?.hashCode() ?: 0)
        result = 31 * result + (syncLyrics?.hashCode() ?: 0)
        result = 31 * result + (picture?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (privateFrame?.hashCode() ?: 0)
        result = 31 * result + (userText?.hashCode() ?: 0)
        result = 31 * result + (artistWeb?.hashCode() ?: 0)
        result = 31 * result + (fileWeb?.hashCode() ?: 0)
        result = 31 * result + (sourceWeb?.hashCode() ?: 0)
        result = 31 * result + (radioWeb?.hashCode() ?: 0)
        result = 31 * result + (payWeb?.hashCode() ?: 0)
        result = 31 * result + (publisherWeb?.hashCode() ?: 0)
        result = 31 * result + (commercialWeb?.hashCode() ?: 0)
        result = 31 * result + (copyrightWeb?.hashCode() ?: 0)
        return result
    }
}
