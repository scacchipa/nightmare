package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.WadInfo
import java.nio.ByteBuffer


val LUMP_INDICES = mapOf(
    "THINGS" to 1,
    "LINEDEFS" to 2,
    "SIDEDEFS" to 3,
    "VERTEXES" to 4,
    "SEGS" to 5,
    "SSECTORS" to 6,
    "NODES" to 7,
    "SECTORS" to 8,
    "REJECT" to 9,
    "BLOCKMAP" to 10
)

class LumpDirectory(
    val lumpEntries: Array<FileLump>
) {
    operator fun get(idx: Int) = lumpEntries[idx]

    fun getIdxForName(name: String): Int = lumpEntries.indexOfFirst { it.name == name }

    companion object {
        fun createFrom(
            buffer: ByteBuffer,
            wadInfo: WadInfo
        ): LumpDirectory {
            buffer.position(wadInfo.infoTableOfs.toInt())
            return LumpDirectory(
                Array(wadInfo.numLumps.toInt()) {
                    FileLump.createFrom(buffer)
                }
            )
        }
    }
}
