package ar.com.scacchi.nightmare.source.wad.lump.data

import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class PNamesLump(
    val numMapPatches: Int,
    val pName: Array<ByteArray>
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, pNameFileLump: FileLump): PNamesLump {
            buffer.position(pNameFileLump.filePos)

            val numMapPatches = buffer.readLittleEndianInt()
            val pName = Array(numMapPatches) {
                buffer.readByteArray(8)
            }

            return PNamesLump(
                numMapPatches = numMapPatches,
                pName = pName,
            )
        }
    }
}