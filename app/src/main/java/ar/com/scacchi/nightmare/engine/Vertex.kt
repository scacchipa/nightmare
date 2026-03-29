package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset

class Vertex(
    val x: Int,
    val y: Int,
) {
    fun toOffset(): Offset = Offset(
        x = this.x.toFloat(),
        y = this.y.toFloat(),
    )
}

fun VertexLump.toVertex(): Vertex = Vertex(
    x = x.toInt(),
    y = y.toInt(),
)