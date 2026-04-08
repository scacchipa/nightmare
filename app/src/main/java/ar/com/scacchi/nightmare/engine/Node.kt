package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.angleTo
import ar.com.scacchi.nightmare.ext.normalizeAngle
import ar.com.scacchi.nightmare.settings.FOV
import ar.com.scacchi.nightmare.settings.H_FOV
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

        fun checkBBox(offset: Offset, angle: Float): Boolean {
            val px = offset.x
            val py = offset.y

            when {
                px < left -> {
                    if (checkSide(offset, angle, leftTop, leftBottom)) return true
                    if (py > top && checkSide(offset, angle, rightTop, leftTop)) return true
                    if (py < bottom && checkSide(offset, angle, leftBottom, rightBottom))
                        return true
                }
                px > right -> {
                    if (checkSide(offset, angle, rightTop, rightBottom)) return true
                    if (py > top && checkSide(offset, angle, rightTop, leftTop)) return true
                    if (py < bottom && checkSide(offset, angle, leftBottom, rightBottom))
                        return true
                    return false
                }
                py > top -> if (checkSide(offset, angle, rightTop, leftTop)) return true
                px > right -> if (checkSide(offset, angle, leftBottom, rightBottom)) return true
                else -> return true
            }
            return false
        }

        private fun checkSide(
            offset: Offset, angle: Float, vertex1: Offset, vertex2: Offset
        ): Boolean {
            val angle1 = offset.angleTo(vertex1)
            val angle2 = offset.angleTo(vertex2)

            val span = (angle1 - angle2).normalizeAngle()

            val angle1RelativeToPlayer = (angle1 - angle).normalizeAngle()
            val span1 = (angle1RelativeToPlayer + H_FOV).normalizeAngle()

            return !(span1 > FOV && span1 >= span + FOV)
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