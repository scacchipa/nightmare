package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class Vertex(
    val x: Short,
    val y: Short,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): Vertex {
            return Vertex(
                x = buffer.readLittleEndianShort(),
                y = buffer.readLittleEndianShort(),
            )
        }
    }
}
