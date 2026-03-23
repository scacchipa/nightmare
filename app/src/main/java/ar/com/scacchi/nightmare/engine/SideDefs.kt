package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class SideDefs(
    val content: Array<SideDef>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySideDefs(): SideDefs = SideDefs(emptyArray())
        fun createFrom(
            buffer: ByteBuffer,
            lump: FileLump,
            sectors: Sectors,
        ): SideDefs {
            buffer.position(lump.filePos)
            return SideDefs(
                content = Array(lump.size / 30) {
                    SideDef.createFrom(buffer, sectors)
                }
            )
        }
    }
}