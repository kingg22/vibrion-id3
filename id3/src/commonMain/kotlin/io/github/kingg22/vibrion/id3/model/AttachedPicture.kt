package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.internal.KoverIgnore
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * Attached picture.
 *
 * _Requirements:_
 * - [type] **must be between 0 and 20**. See [AttachedPictureType]
 *
 * @see io.github.kingg22.vibrion.id3.Id3v2v3TagFrame.APIC
 */
class AttachedPicture @JvmOverloads constructor(
    val type: Int,
    val data: ByteArray,
    val description: String = "",
    val useUnicodeEncoding: Boolean = true,
    val mimeType: String = getMimeType(data),
) : FrameValue {
    @JvmOverloads
    constructor(
        type: AttachedPictureType,
        data: ByteArray,
        description: String = "",
        useUnicodeEncoding: Boolean = true,
        mimeType: String = getMimeType(data),
    ) : this(
        type = type.value,
        data = data,
        description = description,
        useUnicodeEncoding = useUnicodeEncoding,
        mimeType = mimeType,
    )

    init {
        require(type in 0..20) { "Incorrect picture type" }
    }

    @KoverIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AttachedPicture

        if (type != other.type) return false
        if (useUnicodeEncoding != other.useUnicodeEncoding) return false
        if (!data.contentEquals(other.data)) return false
        if (description != other.description) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    @KoverIgnore
    override fun hashCode(): Int {
        var result = type
        result = 31 * result + useUnicodeEncoding.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }

    @KoverIgnore
    override fun toString() =
        "AttachedPicture(type=$type, description='$description', useUnicodeEncoding=$useUnicodeEncoding, mimeType='$mimeType', data=${data.contentToString()})"

    private companion object {
        @JvmSynthetic
        @JvmStatic
        private fun getMimeType(bytes: ByteArray): String {
            if (bytes.isEmpty()) return "image/"

            fun b(i: Int): Int = bytes.getOrNull(i)?.toInt()?.and(0xFF) ?: -1

            return when {
                b(0) == 0xFF && b(1) == 0xD8 && b(2) == 0xFF -> "image/jpeg"

                b(0) == 0x89 && b(1) == 0x50 && b(2) == 0x4E && b(3) == 0x47 -> "image/png"

                b(0) == 0x47 && b(1) == 0x49 && b(2) == 0x46 -> "image/gif"

                b(8) == 0x57 && b(9) == 0x45 && b(10) == 0x42 && b(11) == 0x50 -> "image/webp"

                (b(0) == 0x49 && b(1) == 0x49 && b(2) == 0x2A && b(3) == 0x00) ||
                    (b(0) == 0x4D && b(1) == 0x4D && b(2) == 0x00 && b(3) == 0x2A) -> "image/tiff"

                b(0) == 0x42 && b(1) == 0x4D -> "image/bmp"

                b(0) == 0x00 && b(1) == 0x00 && b(2) == 0x01 && b(3) == 0x00 -> "image/x-icon"

                else -> "image/"
            }
        }
    }
}
