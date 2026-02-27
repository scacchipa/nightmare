package ar.com.scacchi.nightmare.data.asset.texture

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
        fun createFrom(buffer: ByteBuffer): PatchMap {
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