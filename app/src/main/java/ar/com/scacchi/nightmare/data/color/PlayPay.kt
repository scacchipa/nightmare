package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class PlayPay {
    val content: Array<Palette>

    constructor(array: Array<Palette>) {
        this.content = array
    }

    operator fun get(idx: Int) = content[idx]

    companion object {
        fun createFromLump(lump: FileLump, buffer: ByteBuffer): PlayPay? {

            buffer.position(lump.filePos.toInt())
            return PlayPay(
                array = Array(lump.size / 768) {
                    Palette.createFrom(buffer) ?: return null
                }
            )
        }
    }
}

