package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class LineDef(
    val startVertexId: Short,
    val endVertexId: Short,
    val flags: Short,
    val specialType: Short,
    val sectorTag: Short,
    val frontSideDefId: Short, // Doom2
    val backSideDefId: Short, // Doom2
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): LineDef {
            return LineDef(
                startVertexId = buffer.readLittleEndianShort(),
                endVertexId = buffer.readLittleEndianShort(),
                flags = buffer.readLittleEndianShort(),
                specialType = buffer.readLittleEndianShort(),
                sectorTag = buffer.readLittleEndianShort(),
                frontSideDefId = buffer.readLittleEndianShort(),
                backSideDefId = buffer.readLittleEndianShort(),
            )
        }
    }
}