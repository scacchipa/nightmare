package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class Seg(
    val startVertexId: Short,
    val endVertexId: Short,
    val angle: Short, // full circle is -32768 to 32767
    val lineDefId: Short,
    val direction: Short, // 0 as linedef, 1 as opposite of linedef
    val offset: Short, // distance along linedef to start of seg.
    val startVertex : Vertex,
    val endVertex : Vertex,
    val lineDef: LineDef,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, vertexes: Vertexes, lineDefs: LineDefs): Seg {
            val startVertexId = buffer.readLittleEndianShort()
            val endVertexId= buffer.readLittleEndianShort()
            val angle = buffer.readLittleEndianShort()
            val lineDefId = buffer.readLittleEndianShort()
            val direction = buffer.readLittleEndianShort()
            val offset = buffer.readLittleEndianShort()


            return Seg(
                startVertexId = startVertexId,
                endVertexId = endVertexId,
                angle = angle,
                lineDefId = lineDefId,
                direction = direction,
                offset = offset,
                startVertex = vertexes[startVertexId.toInt()],
                endVertex = vertexes[endVertexId.toInt()],
                lineDef = lineDefs[lineDefId.toInt()],
            )
        }
    }
}