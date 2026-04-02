package ar.com.scacchi.nightmare.source.wad.lump.data.map

import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class SectorLump(
    val floorHeight: Short,
    val ceilingHeight: Short,
    val floorTextureName: ByteArray,
    val ceilingTextureName: ByteArray,
    val lightLevel: UShort,
    val type: UShort,
    val tag: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): SectorLump {
            return SectorLump(
                floorHeight = buffer.readLittleEndianShort(),
                ceilingHeight = buffer.readLittleEndianShort(),
                floorTextureName = buffer.readByteArray(8),
                ceilingTextureName =buffer.readByteArray(8),
                lightLevel = buffer.readLittleEndianUShort(),
                type = buffer.readLittleEndianUShort(),
                tag = buffer.readLittleEndianUShort(),
            )
        }
    }
}