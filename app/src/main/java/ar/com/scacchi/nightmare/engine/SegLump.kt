package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class SegLump(
    val startVertexId: Short,
    val endVertexId: Short,
    val angle: UShort, // full circle is -32768 to 32767
    val lineDefId: Short,
    val direction: Short, // 0 as linedef, 1 as opposite of linedef
    val offset: Short, // distance along linedef to start of seg.
) {
    companion object {
        fun createFrom(
            buffer: ByteBuffer
        ): SegLump {
            return SegLump(
                startVertexId = buffer.readLittleEndianShort(),
                endVertexId = buffer.readLittleEndianShort(),
                angle = buffer.readLittleEndianUShort(),
                lineDefId = buffer.readLittleEndianShort(),
                direction = buffer.readLittleEndianShort(),
                offset = buffer.readLittleEndianShort(),
            )
        }
    }
}

