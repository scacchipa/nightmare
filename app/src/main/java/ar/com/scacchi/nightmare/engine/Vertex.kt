package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class Vertex(
    val x: Short,
    val y: Short,
) {
    fun toOffset(): Offset = Offset(
            x = this.x.toFloat(),
            y = this.y.toFloat(),
        )

    companion object {
        fun createFrom(buffer: ByteBuffer): Vertex {
            return Vertex(
                x = buffer.readLittleEndianShort(),
                y = buffer.readLittleEndianShort(),
            )
        }
    }
}
