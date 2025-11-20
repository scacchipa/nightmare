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
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): Seg {
            return Seg(
                startVertexId = buffer.readLittleEndianShort(),
                endVertexId= buffer.readLittleEndianShort(),
                angle = buffer.readLittleEndianShort(),
                lineDefId = buffer.readLittleEndianShort(),
                direction = buffer.readLittleEndianShort(),
                offset = buffer.readLittleEndianShort(),
            )
        }
    }
}