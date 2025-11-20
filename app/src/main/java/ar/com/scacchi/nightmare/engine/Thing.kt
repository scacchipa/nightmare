package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class Thing(
    val xPos: Short,
    val yPos: Short,
    val angle: UShort,
    val type: UShort,
    val flags: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): Thing {
            return Thing(
                xPos = buffer.readLittleEndianShort(),
                yPos = buffer.readLittleEndianShort(),
                angle = buffer.readLittleEndianUShort(),
                type = buffer.readLittleEndianUShort(),
                flags = buffer.readLittleEndianUShort(),
            )
        }
    }
}