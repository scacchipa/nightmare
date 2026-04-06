package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class PlayPalLump(
    val content: Array<PaletteLump>
) {
    operator fun get(idx: Int) = content[idx]

    val paletteCount: Int
        get() = content.size

    companion object {
        fun emptyPlayPal(): PlayPalLump = PlayPalLump(emptyArray())
        fun createFrom(lump: FileLump, buffer: ByteBuffer): PlayPalLump {

            buffer.position(lump.filePos)
            return PlayPalLump(
                content = Array(lump.size / 768) {
                    PaletteLump.createFrom(buffer)
                }
            )
        }
    }
}
