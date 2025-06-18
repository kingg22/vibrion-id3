package io.github.kingg22.vibrion.id3

import kotlin.test.Test
import kotlin.test.assertEquals

class ID3FrameTypeTest {
    @Test
    fun nameEqualsString() {
        assertEquals("TDAT", ID3FrameType.TDAT.name)
    }
}
