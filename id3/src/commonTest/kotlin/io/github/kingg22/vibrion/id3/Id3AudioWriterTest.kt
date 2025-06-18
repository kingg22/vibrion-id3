package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertFails

class Id3AudioWriterTest {
    @Test
    fun `test Unknow Frame Throws Exception`() {
        assertFails {
            val writer = Id3AudioWriter(ByteArray(0))
            writer[ID3FrameType.UNKNOWN] = FrameValue.TextFrame("test")
        }.message?.contains("Unsupported frame")
    }
}
