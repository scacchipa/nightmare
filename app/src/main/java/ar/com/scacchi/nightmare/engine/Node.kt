package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.data.map.NodeLump

class Node(
    val xPartition: Int,
    val yPartition: Int,
    val dxPartition: Int,
    val dyPartition: Int,
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
    xPartition = xPartition.toInt(),
    yPartition = yPartition.toInt(),
    dxPartition = dxPartition.toInt(),
    dyPartition = dyPartition.toInt(),
    frontBoundBox = frontBoundBox.toNodeBoundBox(),
    backBoundBox = backBoundBox.toNodeBoundBox(),
    frondChildId = frondChildId.toInt(),
    backChildId = backChildId.toInt(),
)