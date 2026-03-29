package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class SectorsLump(
    val content: Array<SectorLump>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySectors(): SectorsLump = SectorsLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): SectorsLump {
            buffer.position(lump.filePos)
            return SectorsLump(
                content = Array(lump.size / 26) {
                    SectorLump.createFrom(buffer)
                }
            )
        }
    }
}

