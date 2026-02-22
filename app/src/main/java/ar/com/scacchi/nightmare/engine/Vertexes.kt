package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Vertexes(
    val content: Array<Vertex>
) {
    operator fun get(idx: Int): Vertex = content[idx]

    fun toOffsetList(): List<Offset> = this.content.map {
        Offset(
            x = it.x.toFloat(),
            y = it.y.toFloat(),
        )
    }

    companion object {
        fun emptyVertexes(): Vertexes = Vertexes(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Vertexes {
            buffer.position(lump.filePos)
            return Vertexes(
                content = Array(lump.size / 4) {
                    Vertex.createFrom(buffer)
                }
            )
        }
    }
}
