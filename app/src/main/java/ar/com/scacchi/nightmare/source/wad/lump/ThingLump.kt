package ar.com.scacchi.nightmare.source.wad.lump

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class ThingLump(
    val xPos: Short,
    val yPos: Short,
    val angle: UShort,
    val type: UShort,
    val flags: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): ThingLump {
            return ThingLump(
                xPos = buffer.readLittleEndianShort(),
                yPos = buffer.readLittleEndianShort(),
                angle = buffer.readLittleEndianUShort(),
                type = buffer.readLittleEndianUShort(),
                flags = buffer.readLittleEndianUShort(),
            )
        }
    }
}