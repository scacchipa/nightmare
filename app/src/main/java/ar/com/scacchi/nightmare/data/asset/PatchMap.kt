package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class PatchMap(
    val xOffset: Short,
    val yOffset: Short,
    val pNameIndex: UShort,
    val stepDir: UShort,
    val colorMap: UShort,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, offset: Int): PatchMap {
            buffer.position(offset)

            return PatchMap(
                xOffset = buffer.readLittleEndianShort(),
                yOffset = buffer.readLittleEndianShort(),
                pNameIndex = buffer.readLittleEndianUShort(),
                stepDir = buffer.readLittleEndianUShort(),
                colorMap = buffer.readLittleEndianUShort(),
            )
        }
    }
}