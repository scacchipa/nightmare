package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.SegHandler
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

val Vector0Degree: Offset = Offset(1f, 0f)
val Vector180Degree: Offset = Offset(-1f, 0f)
val Vector90Degree: Offset = Offset(0f, 1f)
val Vector270Degree: Offset = Offset(0f, -1f)
val Vector45Degree: Offset = Offset.versorWithAngle(PI.toFloat() * 0.25f)
val Vector135Degree: Offset = Offset.versorWithAngle(PI.toFloat() * 0.75f)
val Vector225Degree: Offset = Offset.versorWithAngle(PI.toFloat() * (-0.25f))
val Vector315Degree: Offset = Offset.versorWithAngle(PI.toFloat() * (-0.75f))

val rotSpeedVector: Offset = Offset.versorWithAngle(0.12f)

enum class AxisIntersection { POSITIVE, NEGATIVE, NONE }

inline fun Offset.rotateBy(angle: Float): Offset {
    val cos = cos(angle)
    val sin = sin(angle)
    return Offset(
        x = x * cos - y * sin,
        y = x * sin + y * cos,
    )
}

fun Offset.Companion.versorWithAngle(rad: Float): Offset = Offset(cos(rad), sin(rad))
fun Offset.Companion.checkXAxisIntersection(a: Offset, b: Offset): AxisIntersection {
    val dy = b.y - a.y

    if ((a.y >= 0f && b.y >= 0f) || (a.y < 0f &&  b.y < 0f) || dy == 0f)
        return AxisIntersection.NONE

    val numerator: Float = a.x * b.y - a.y * b.x

    return when {
        (numerator > 0f && dy > 0) || (numerator < 0f && dy < 0f)-> AxisIntersection.POSITIVE
        (numerator < 0f && dy > 0) || (numerator > 0f && dy < 0f) -> AxisIntersection.NEGATIVE
        else -> AxisIntersection.NONE
    }
}
inline fun Offset.hypotenuse(): Float = hypot(x, y)
inline fun Offset.tan(): Float = y / x
inline fun Offset.atan2(): Float = atan2(y, x)
inline fun Offset.angleToPoint(point: Offset): Float = (point - this).atan2()
inline fun Offset.vectorToPoint(point: Offset): Offset = (point - this)
inline fun Offset.versorToPoint(point: Offset): Offset {
    val vector = point -this
    return vector / vector.hypotenuse()
}
inline fun Offset.normalize(): Offset = this / this.hypotenuse()
inline fun Offset.rotatedBy(dir: Offset): Offset = Offset(
    x = this.x * dir.x - this.y * dir.y,
    y = this.x * dir.y + this.y * dir.x
)
inline fun Offset.deRotateBY(dir: Offset): Offset = Offset(
    x = this.x * dir.x + this.y * dir.y,
    y = - this.x * dir.y + this.y * dir.x
)
inline fun Offset.flipX(): Offset = Offset(-x, y)
inline fun Offset.flipY(): Offset = Offset(x, -y)
inline fun Offset.fastAtan2(): Float {

    if (x == 0f) return if (y >= 0f) HALF_PI else THREE_HALF_PI
    if (y == 0f) return if (x >= 0f) 0f else PI.toFloat()

    val absX = x.absoluteValue
    val absY = y.absoluteValue

    val angle =
        if (absX > absY) SegHandler.atan2Table[(absY / absX * 1024f).toInt()]
        else HALF_PI - SegHandler.atan2Table[(absX / absY * 1024f).toInt()]

    if (x > 0) return if (y > 0) angle else -angle
    return if (y > 0) PI.toFloat() - angle else angle - PI.toFloat()
}
inline operator fun Offset.compareTo(other: Offset): Int {
    val thisUpper = this.y > 0 || (this.y == 0f && this.x > 0)
    val otherUpper = other.y > 0 || (other.y == 0f && other.x > 0)

    if (thisUpper != otherUpper) return if (thisUpper) 1 else -1

    val crossProduct = this.x * other.y - this.y * other.x

    return when {
        crossProduct > 0 -> -1
        crossProduct < 0 -> 1
        else -> 0
    }
}

