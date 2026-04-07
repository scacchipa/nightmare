package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.source.wad.lump.data.map.VertexLump

class Vertex(
    val pos: Offset,
)

fun VertexLump.toVertex(): Vertex = Vertex(
    pos = Offset(x.toFloat(), y.toFloat()),
)