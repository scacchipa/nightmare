package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.source.wad.lump.data.map.VertexesLump

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
    }
}

fun VertexesLump.toVertexes(): Vertexes = Vertexes(
    content = Array(content.size) { idx ->
        content[idx].toVertex()
    }
)