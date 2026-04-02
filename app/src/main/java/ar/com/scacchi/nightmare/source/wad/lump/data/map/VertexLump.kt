package ar.com.scacchi.nightmare.source.wad.lump.data.map

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class VertexLump(
    val x: Short,
    val y: Short,
) {
    fun toOffset(): Offset = Offset(
        x = this.x.toFloat(),
        y = this.y.toFloat(),
    )

    companion object {
        fun createFrom(buffer: ByteBuffer): VertexLump {
            return VertexLump(
                x = buffer.readLittleEndianShort(),
                y = buffer.readLittleEndianShort(),
            )
        }
    }
}