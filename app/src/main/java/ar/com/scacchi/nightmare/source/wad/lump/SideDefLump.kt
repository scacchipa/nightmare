package ar.com.scacchi.nightmare.source.wad.lump

import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class SideDefLump(
    val xOffset: Short,
    val yOffset: Short,
    val upperTextureName: ByteArray,
    val lowerTextureName: ByteArray,
    val middleTextureName: ByteArray,
    val sectorId: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): SideDefLump = SideDefLump(
            xOffset = buffer.readLittleEndianShort(),
            yOffset = buffer.readLittleEndianShort(),
            upperTextureName = buffer.readByteArray(8),
            lowerTextureName = buffer.readByteArray(8),
            middleTextureName = buffer.readByteArray(8),
            sectorId = buffer.readLittleEndianUShort(),
        )
    }
}