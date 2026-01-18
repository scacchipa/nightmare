package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Sectors(
    val content: Array<Sector>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun createFrom(
            buffer: ByteBuffer,
            lump: FileLump,
        ): Sectors {
            buffer.position(lump.filePos)
            return Sectors(
                content = Array(lump.size / 26) {
                    Sector.createFrom(buffer)
                }
            )
        }
    }
}