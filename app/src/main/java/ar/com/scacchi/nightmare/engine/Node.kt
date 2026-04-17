package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.rotatedBy
import ar.com.scacchi.nightmare.settings.leftLimitFOVVersor
import ar.com.scacchi.nightmare.settings.rightLimitFOVVersor
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
        val leftBottom = Offset(left, bottom)
        val leftTop = Offset(left, top)
        val rightTop = Offset(right, top)
        val rightBottom = Offset(right, bottom)

        fun checkBBox(offset: Offset, dirVector: Offset): Boolean {
            val px = offset.x
            val py = offset.y

            when {
                px < left -> {
                    if (checkSide(offset, dirVector, leftTop, leftBottom)) return true
                    if (py > top && checkSide(offset, dirVector, rightTop, leftTop)) return true
                    if (py < bottom && checkSide(offset, dirVector, leftBottom, rightBottom))
                        return true
                }

                px > right -> {
                    if (checkSide(offset, dirVector, rightTop, rightBottom)) return true
                    if (py > top && checkSide(offset, dirVector, rightTop, leftTop)) return true
                    if (py < bottom && checkSide(offset, dirVector, leftBottom, rightBottom))
                        return true
                    return false
                }

                py > top -> if (checkSide(offset, dirVector, rightTop, leftTop)) return true
                px > right -> if (checkSide(offset, dirVector, leftBottom, rightBottom)) return true
                else -> return true
            }
            return false
        }

        private fun checkSide(
            offset: Offset, dirVector: Offset, vertex1: Offset, vertex2: Offset
        ): Boolean {

            val leftLimitVector = dirVector.rotatedBy(leftLimitFOVVersor)
            val rightLimitVector = dirVector.rotatedBy(rightLimitFOVVersor)

            val leftD1 =
                        -leftLimitVector.y * (vertex1.x - offset.x) +
                         leftLimitVector.x * (vertex1.y - offset.y)
            val leftD2 =
                        -leftLimitVector.y * (vertex2.x - offset.x) +
                         leftLimitVector.x * (vertex2.y - offset.y)

            if (leftD1 > 0f && leftD2 > 0f) return false

            val rightD1 =
                       -rightLimitVector.y * (vertex1.x - offset.x) +
                        rightLimitVector.x * (vertex1.y - offset.y)
            val rightD2 =
                       -rightLimitVector.y * (vertex2.x - offset.x) +
                        rightLimitVector.x * (vertex2.y - offset.y)

            if (rightD1 < 0f && rightD2 < 0f) return false

            return true
        }

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