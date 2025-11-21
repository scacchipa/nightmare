package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadius = angle * PI / 180
    return Offset(
        x = (x * cos(angleInRadius) - y * sin(angleInRadius)).toFloat(),
        y = (x * sin(angleInRadius) + y * cos(angleInRadius)).toFloat(),
    )
}