package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertEquals

class ID3FrameTypeTest {
    @Test
    fun nameEqualsString() {
        Id3v2v3TagFrame.allFrames.forEach {
            assertEquals(it.toString(), it.name, "Frame name ${it.name} does not match string representation")
        }
    }

    @Test
    fun tagsFramesSupported() {
        assertEquals(41, Id3v2v3TagFrame.allFrames.size)
    }
}
