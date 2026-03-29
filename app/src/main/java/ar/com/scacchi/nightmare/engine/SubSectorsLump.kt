package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class SubSectorsLump(
    val content: Array<SubSectorLump>,
) {
    operator fun get(idx: Int) = content[idx]

    fun count(): Int = content.size

    companion object {
        fun emptySubSectors(): SubSectorsLump = SubSectorsLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): SubSectorsLump {
            buffer.position(lump.filePos)
            return SubSectorsLump(
                content = Array(lump.size / 4) {
                    SubSectorLump.createFrom(buffer)
                }
            )
        }
    }
}


