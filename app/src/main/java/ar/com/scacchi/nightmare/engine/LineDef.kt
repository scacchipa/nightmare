package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class LineDef(
    val startVertexId: UShort,
    val endVertexId: UShort,
    val flags: UShort,
    val specialType: UShort,
    val sectorTag: UShort,
    val frontSideDefId: UShort, // Doom2
    val backSideDefId: UShort, // Doom2
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): LineDef {
            return LineDef(
                startVertexId = buffer.readLittleEndianUShort(),
                endVertexId = buffer.readLittleEndianUShort(),
                flags = buffer.readLittleEndianUShort(),
                specialType = buffer.readLittleEndianUShort(),
                sectorTag = buffer.readLittleEndianUShort(),
                frontSideDefId = buffer.readLittleEndianUShort(),
                backSideDefId = buffer.readLittleEndianUShort(),
            )
        }
    }
}