package ar.com.scacchi.nightmare.source.wad.lump.data

import ar.com.scacchi.nightmare.data.readByteArrayAsString
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class PNameLump(
    private val numMapPatches: Int,
    private val pName: Array<String>

) {
    operator fun get(idx: Int) = pName[idx]

    companion object {
        fun createFrom(buffer: ByteBuffer, pNameFileLump: FileLump): PNameLump {
            buffer.position(pNameFileLump.filePos)

            val numMapPatches = buffer.readLittleEndianInt()
            val pName = Array(numMapPatches) {
                buffer.readByteArrayAsString(8)
            }

            return PNameLump(
                numMapPatches = numMapPatches,
                pName = pName,
            )
        }
    }
}