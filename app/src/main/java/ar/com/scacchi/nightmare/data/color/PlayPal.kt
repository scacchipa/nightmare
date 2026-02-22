package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class PlayPal {
    val content: Array<Palette>

    constructor(array: Array<Palette>) {
        this.content = array
    }

    operator fun get(idx: Int) = content[idx]

    val paletteCount: Int
        get() = content.size

    companion object {
        fun emptyPlayPal(): PlayPal = PlayPal(emptyArray())
        fun createFromLump(lump: FileLump, buffer: ByteBuffer): PlayPal {

            buffer.position(lump.filePos)
            return PlayPal(
                array = Array(lump.size / 768) {
                    Palette.createFrom(buffer)
                }
            )
        }
    }
}

