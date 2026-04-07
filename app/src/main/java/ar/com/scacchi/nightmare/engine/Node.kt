package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.source.wad.lump.data.map.NodeLump

class Node(
    val partition: Offset,
    val dPartition: Offset,
    val frontBoundBox: BoundBox = BoundBox(0, 0, 0, 0),
    val backBoundBox: BoundBox = BoundBox(0, 0, 0, 0),
    val frondChildId: Int,
    val backChildId: Int,
) {
    class BoundBox(
        val top: Int,
        val bottom: Int,
        val left: Int,
        val right: Int,
    )
}

fun NodeLump.BoundBox.toNodeBoundBox(): Node.BoundBox = Node.BoundBox(
    top = top.toInt(),
    bottom = bottom.toInt(),
    left = left.toInt(),
    right = right.toInt(),
)

fun NodeLump.toNode(): Node = Node(
    partition = Offset(xPartition.toFloat(), yPartition.toFloat()),
    dPartition = Offset(dxPartition.toFloat(), dyPartition.toFloat()),
    frontBoundBox = frontBoundBox.toNodeBoundBox(),
    backBoundBox = backBoundBox.toNodeBoundBox(),
    frondChildId = frondChildId.toInt(),
    backChildId = backChildId.toInt(),
)