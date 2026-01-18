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
    val frontSideDef: SideDef?,
    val backSideDef: SideDef?,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, sideDefs: SideDefs): LineDef {
            val startVertexId = buffer.readLittleEndianUShort()
            val endVertexId = buffer.readLittleEndianUShort()
            val flags = buffer.readLittleEndianUShort()
            val specialType = buffer.readLittleEndianUShort()
            val sectorTag = buffer.readLittleEndianUShort()
            val frontSideDefId = buffer.readLittleEndianUShort()
            val backSideDefId = buffer.readLittleEndianUShort()

            return LineDef(
                startVertexId = startVertexId,
                endVertexId = endVertexId,
                flags = flags,
                specialType = specialType,
                sectorTag = sectorTag,
                frontSideDefId = frontSideDefId,
                backSideDefId = backSideDefId,
                frontSideDef =
                    if (frontSideDefId != 0xFFFFu.toUShort()) sideDefs[frontSideDefId.toInt()]
                    else null,
                backSideDef =
                    if (backSideDefId != 0xFFFFu.toUShort()) sideDefs[backSideDefId.toInt()]
                    else null,
            )
        }
    }
}