package io.github.kingg22.vibrion.id3

import io.github.kingg22.vibrion.id3.model.AttachedPicture
import io.github.kingg22.vibrion.id3.model.AttachedPictureType
import kotlin.jvm.JvmSynthetic

/** Builder for [AttachedPicture] */
@Id3Dsl
class AttachedPictureBuilder {
    var type: AttachedPictureType = AttachedPictureType.CoverFront
    var data: ByteArray = byteArrayOf()
    var description: String = ""
    var useUnicodeEncoding: Boolean = true
    var mimeType: String? = null

    fun type(type: AttachedPictureType) = apply { this.type = type }
    fun data(data: ByteArray) = apply { this.data = data }
    fun description(description: String) = apply { this.description = description }
    fun useUnicodeEncoding(useUnicodeEncoding: Boolean) = apply { this.useUnicodeEncoding = useUnicodeEncoding }
    fun mimeType(type: String?) = apply { this.mimeType = type }

    fun build() = mimeType?.let { mime ->
        AttachedPicture(type, data, description, useUnicodeEncoding, mime)
    } ?: AttachedPicture(type, data, description, useUnicodeEncoding)

    companion object {
        /** DSL builder for [AttachedPicture] */
        @JvmSynthetic
        fun builder(block: AttachedPictureBuilder.() -> Unit) = AttachedPictureBuilder().apply(block).build()
    }
}
