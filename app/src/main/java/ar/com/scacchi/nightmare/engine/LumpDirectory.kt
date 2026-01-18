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

val LINEDEF_FLAGS = mapOf(
    "BLOCKING" to 1.toUShort(),
    "BLOCK_MONSTERS" to 2.toUShort(),
    "TWO_SIDED" to 4.toUShort(),
    "DONT_PEG_TOP" to 8.toUShort(),
    "DONT_PEG_BOTTOM" to 16.toUShort(),
    "SECRET" to 32.toUShort(),
    "SOUND_BLOCK" to 64.toUShort(),
    "DONT_DRAW" to 128.toUShort(),
    "MAPPED" to 256.toUShort(),
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
