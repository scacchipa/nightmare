package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Vertexes(
    val vertexes: Array<Vertex>
) {
    operator fun get(idx: Int): Vertex = vertexes[idx]

    fun toOffsetList(): List<Offset> = this.vertexes.map {
        Offset(
            x = it.x.toFloat(),
            y = it.y.toFloat(),
        )
    }

    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Vertexes {
            buffer.position(lump.filePos)
            return Vertexes(
                vertexes = Array(lump.size / 4) {
                    Vertex.createFrom(buffer)
                }
            )
        }
    }
}
