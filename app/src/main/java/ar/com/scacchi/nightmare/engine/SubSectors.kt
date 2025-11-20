package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class SubSectors(
    val content: Array<SubSector>,
) {
    operator fun get(idx: Int) = content[idx]

    fun count(): Int = content.size

    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): SubSectors {
            buffer.position(lump.filePos)
            return SubSectors(
                content = Array(lump.size / 4) {
                    SubSector.createFrom(buffer)
                }
            )
        }
    }
}
