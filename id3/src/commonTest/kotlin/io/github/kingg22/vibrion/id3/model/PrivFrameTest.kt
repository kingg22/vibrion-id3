package io.github.kingg22.vibrion.id3.model

import io.github.kingg22.vibrion.id3.Id3AudioWriter
import io.github.kingg22.vibrion.id3.getEmptyBuffer
import io.github.kingg22.vibrion.id3.id3Header
import io.github.kingg22.vibrion.id3.internal.encodeSynchsafeInt
import io.github.kingg22.vibrion.id3.internal.encodeWindows1252
import io.github.kingg22.vibrion.id3.internal.uint32ToUint8Array
import kotlin.test.Test
import kotlin.test.assertContentEquals

class PrivFrameTest {
    @Test
    fun `should correctly write a private frame into ID3`() {
        val data = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val ownerId = "site.com"

        // 2. Instantiate and configure the ID3Writer
        val writer = Id3AudioWriter(getEmptyBuffer())
        writer.padding = 0
        writer["PRIV"] = PrivateFrame(id = ownerId, data = data)
        val actual = writer.addTag()

        // Content of the PRIV frame: Owner ID (Windows-1252) + Null terminator + PrivateTagFrame Data
        val ownerIdBytes = encodeWindows1252(ownerId)
        val frameContent = ownerIdBytes + 0 + data // 8 bytes + 1 byte + 9 bytes = 18 bytes
        val frameContentSize = frameContent.size // 18 bytes

        // The full PRIV frame (including its own header):
        // Frame ID ("PRIV", 4 bytes) + Content Size (18, 4 bytes) + Flags (0, 0, 2 bytes) + Frame Content (18 bytes)
        // Total PRIV frame bytes = 4 + 4 + 2 + 18 = 28 bytes
        val totalPrivFrameBytes = encodeWindows1252("PRIV") +
            uint32ToUint8Array(frameContentSize) +
            byteArrayOf(0, 0) +
            frameContent

        // The complete expected ID3 tag:
        // Main ID3 Header Prefix (ID3Utils.ID3HeaderPrefix, 6 bytes)
        // Synchsafe Tag Size (totalPrivFrameBytes.size, 4 bytes)
        // Followed by the full PRIV frame bytes (28 bytes)
        val expected = id3Header + // 6 bytes: "ID3", ver, flags
            encodeSynchsafeInt(totalPrivFrameBytes.size) + // 4 bytes: synchsafe size (28)
            totalPrivFrameBytes // 28 bytes: PRIV frame header + content
        val preComputedExpected = byteArrayOf(
            73, 68, 51, 3, 0, 0, 0, 0, 0, 28,
            80, 82, 73, 86, 0, 0, 0, 18, 0, 0,
            115, 105, 116, 101, 46, 99, 111, 109, 0, 1,
            2, 3, 4, 5, 6, 7, 8, 9,
        )

        assertContentEquals(preComputedExpected, actual)
        assertContentEquals(expected, actual)
    }
}
