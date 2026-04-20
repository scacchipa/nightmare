package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.bamToRadian
import ar.com.scacchi.nightmare.source.wad.lump.LINEDEF_FLAGS
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SegLump

class Seg(
    val idx: Int,
    val startVertexId: Int,
    val endVertexId: Int,
    val angle: UShort, // full circle is -32768 to 32767
    val lineDefId: Int,
    val direction: Int, // 0 as linedef, 1 as opposite of linedef
    val offset: Int, // distance along linedef to start of seg.
    val startVertex: Vertex,
    val endVertex: Vertex,
    val lineDef: LineDef,
    val frontSector: Sector?,
    val backSector: Sector?,
    val radAngle: Float,
    val vector: Offset,
)

fun SegLump.toSeg(idx: Int, vertexes: Vertexes, lineDefs: LineDefs): Seg {

    val direction = direction.toInt()
    val lineDef = lineDefs[lineDefId.toInt()]
    val frontSideDef: SideDef? =
        if (direction != 0) lineDef.backSideDef
        else lineDef.frontSideDef
    val backSideDef: SideDef? =
        if (direction != 0) lineDef.frontSideDef
        else lineDef.backSideDef
    val startVertex = vertexes[startVertexId.toInt()]
    val endVertex = vertexes[endVertexId.toInt()]

    return Seg(
        idx = idx,
        startVertexId = startVertexId.toInt(),
        endVertexId = endVertexId.toInt(),
        angle = angle,
        lineDefId = lineDefId.toInt(),
        direction = direction,
        offset = offset.toInt(),
        startVertex = startVertex,
        endVertex = endVertex,
        lineDef = lineDef,
        frontSector = frontSideDef?.sector,
        backSector =
            if (lineDef.flags and (LINEDEF_FLAGS["TWO_SIDED"] ?: 0u) != 0.toUShort())
                backSideDef?.sector
            else null,
        radAngle = angle.bamToRadian(),
        vector = endVertex.pos - startVertex.pos
    )
}