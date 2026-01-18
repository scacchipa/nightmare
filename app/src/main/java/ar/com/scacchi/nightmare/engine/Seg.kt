package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import ar.com.scacchi.nightmare.ext.bamToRadian
import java.nio.ByteBuffer

class Seg(
    val startVertexId: Short,
    val endVertexId: Short,
    val angle: UShort, // full circle is -32768 to 32767
    val lineDefId: Short,
    val direction: Short, // 0 as linedef, 1 as opposite of linedef
    val offset: Short, // distance along linedef to start of seg.
    val startVertex : Vertex,
    val endVertex : Vertex,
    val lineDef: LineDef,
    val frontSector: Sector?,
    val backSector: Sector?,
    val radAngle: Float,
) {
    companion object {
        fun createFrom(
            buffer: ByteBuffer, vertexes: Vertexes, lineDefs: LineDefs, sectors: Sectors
        ): Seg {
            val startVertexId = buffer.readLittleEndianShort()
            val endVertexId= buffer.readLittleEndianShort()
            val angle = buffer.readLittleEndianUShort()
            val lineDefId = buffer.readLittleEndianShort()
            val direction = buffer.readLittleEndianShort()
            val offset = buffer.readLittleEndianShort()

            val lineDef = lineDefs[lineDefId.toInt()]

            val fontSideDef: SideDef? =
                if (direction.toInt() != 0) lineDef.backSideDef
                else lineDef.frontSideDef

            val backSideDef: SideDef? =
                if (direction.toInt() != 0) lineDef.frontSideDef
                else lineDef.backSideDef

            val rads = angle.bamToRadian()

            return Seg(
                startVertexId = startVertexId,
                endVertexId = endVertexId,
                angle = angle,
                lineDefId = lineDefId,
                direction = direction,
                offset = offset,
                startVertex = vertexes[startVertexId.toInt()],
                endVertex = vertexes[endVertexId.toInt()],
                lineDef = lineDef,
                frontSector = fontSideDef?.sector,
                backSector =
                    if (lineDef.flags and (LINEDEF_FLAGS["TWO_SIDED"] ?: 0u) != 0.toUShort())
                        backSideDef?.sector
                    else null,
                radAngle = rads
            )
        }
    }
}