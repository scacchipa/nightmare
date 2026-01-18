package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class SideDef(
    val xOffset: Short,
    val yOffset: Short,
    val upperTextureName: ByteArray,
    val lowerTextureName: ByteArray,
    val middleTextureName: ByteArray,
    val sectorId: UShort,
    val sector: Sector
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, sectors: Sectors): SideDef {
            val xOffset = buffer.readLittleEndianShort()
            val yOffset = buffer.readLittleEndianShort()
            val upperTextureName = buffer.readByteArray(8)
            val lowerTextureName = buffer.readByteArray(8)
            val middleTextureName = buffer.readByteArray(8)
            val sectorId = buffer.readLittleEndianUShort()

            return SideDef(
                xOffset = xOffset,
                yOffset = yOffset,
                upperTextureName = upperTextureName,
                lowerTextureName = lowerTextureName,
                middleTextureName = middleTextureName,
                sectorId = sectorId,
                sector = sectors[sectorId.toInt()],

            )
        }
    }
}

