package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class VertexesLump(
    val content: Array<VertexLump>
) {
    operator fun get(idx: Int): VertexLump = content[idx]

    fun toOffsetList(): List<Offset> = this.content.map {
        Offset(
            x = it.x.toFloat(),
            y = it.y.toFloat(),
        )
    }

    companion object {
        fun emptyVertexes(): VertexesLump = VertexesLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): VertexesLump {
            buffer.position(lump.filePos)
            return VertexesLump(
                content = Array(lump.size / 4) {
                    VertexLump.createFrom(buffer)
                }
            )
        }
    }
}

