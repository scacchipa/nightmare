package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

val Vector0Degree: Offset = Offset(1f, 0f)
val Vector180Degree: Offset = Offset(-1f, 0f)
val Vector90Degree: Offset = Offset(0f, 1f)
val Vector270Degree: Offset = Offset(0f, -1f)
val Vector45Degree: Offset = Offset.fromAngle(PI.toFloat() * 0.25f)
val Vector135Degree: Offset = Offset.fromAngle(PI.toFloat() * 0.75f)
val Vector225Degree: Offset = Offset.fromAngle(PI.toFloat() * (-0.25f))
val Vector315Degree: Offset = Offset.fromAngle(PI.toFloat() * (-0.75f))

val rotSpeedVector: Offset = Offset.fromAngle(0.12f)

fun Offset.rotateBy(angle: Float): Offset {
    val cos = cos(angle)
    val sin = sin(angle)
    return Offset(
        x = x * cos - y * sin,
        y = x * sin + y * cos,
    )
}

fun Offset.Companion.fromAngle(rad: Float): Offset = Offset(cos(rad), sin(rad))

fun Offset.hypotenuse(): Float = hypot(x, y)
fun Offset.atan2(): Float = atan2(y, x)
fun Offset.angleToX(point: Offset): Float = (point - this).atan2()
fun Offset.angleTo(point: Offset): Float = (point - this).atan2()
fun Offset.cartesianProduct(v: Offset) = Offset(
    x = this.x * v.x - this.y * v.y,
    y = this.x * v.y + this.y * v.x
)
