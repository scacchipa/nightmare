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
        fun checkBBox(offset: Offset, angle: Float): Boolean {

            val a = Offset(left, bottom)
            val b = Offset(left, top)
            val c = Offset(right, top)
            val d = Offset(right, bottom)

            val px = offset.x
            val py = offset.y

            val bBoxSides: List<Pair<Offset, Offset>> = when {
                px < left -> when {
                    py > top -> listOf(b to a, c to b)
                    py < bottom -> listOf(b to a, a to d)
                    else -> listOf(b to a)
                }
                px > right -> when {
                    py > top -> listOf(c to b, d to c)
                    py < bottom -> listOf(a to d, d to c)
                    else -> listOf(d to c)
                }
                else -> when {
                    py > top -> listOf(c to b)
                    py < bottom -> listOf(a to  d)
                    else -> return true
                }
            }

            for ((v1, v2) in bBoxSides) {
                val angle1 = offset.angleTo(v1)
                val angle2 = offset.angleTo(v2)

                val span = (angle1 - angle2).normalizeAngle()

                val angle1RelativeToPlayer = (angle1 - angle).normalizeAngle()
                val span1 = (angle1RelativeToPlayer + H_FOV).normalizeAngle()

                if (span1 > FOV) {
                    if (span1 >= span + FOV) {
                        continue
                    }
                }
                return true
            }
            return false
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