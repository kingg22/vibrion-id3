package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertEquals

class ID3FrameTypeTest {
    @Test
    fun nameEqualsString() {
        assertEquals("TDAT", Id3v2v3TagFrame.TDAT.name)
    }

    @Test
    fun tagsFramesSupported() {
        assertEquals(41, Id3v2v3TagFrame.allFrames.size)
    }
}
