package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class Sector(
    val floorHeight: UShort,
    val ceilingHeight: UShort,
    val floorTextureName: ByteArray,
    val ceilingTextureName: ByteArray,
    val lightLevel: UShort,
    val type: UShort,
    val tag: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): Sector {
            return Sector(
                floorHeight = buffer.readLittleEndianUShort(),
                ceilingHeight = buffer.readLittleEndianUShort(),
                floorTextureName = buffer.readByteArray(8),
                ceilingTextureName =buffer.readByteArray(8),
                lightLevel = buffer.readLittleEndianUShort(),
                type = buffer.readLittleEndianUShort(),
                tag = buffer.readLittleEndianUShort(),
            )
        }
    }
}
