package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.source.wad.lump.data.map.NodeLump

class Node(
    val partition: Offset,
    val dPartition: Offset,
    val frontBoundBox: BoundBox = BoundBox.empty,
    val backBoundBox: BoundBox = BoundBox.empty,
    val frondChildId: Int,
    val backChildId: Int,
) {
    class BoundBox(
        val top: Float,
        val bottom: Float,
        val left: Float,
        val right: Float,
    ) {
        companion object {
            val empty = BoundBox(0f, 0f, 0f, 0f)
        }
    }
}

fun NodeLump.BoundBox.toNodeBoundBox(): Node.BoundBox = Node.BoundBox(
    top = top.toFloat(),
    bottom = bottom.toFloat(),
    left = left.toFloat(),
    right = right.toFloat(),
)

fun NodeLump.toNode(): Node = Node(
    partition = Offset(xPartition.toFloat(), yPartition.toFloat()),
    dPartition = Offset(dxPartition.toFloat(), dyPartition.toFloat()),
    frontBoundBox = frontBoundBox.toNodeBoundBox(),
    backBoundBox = backBoundBox.toNodeBoundBox(),
    frondChildId = frondChildId.toInt(),
    backChildId = backChildId.toInt(),
)