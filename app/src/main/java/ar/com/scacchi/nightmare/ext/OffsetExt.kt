package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadius = angle * PI / 180
    val cos = cos(angleInRadius)
    val sin = sin(angleInRadius)
    return Offset(
        x = (x * cos - y * sin).toFloat(),
        y = (x * sin + y * cos).toFloat(),
    )
}

fun Offset.Companion.scalar(rad: Float): Offset {
    return Offset(
        x = cos(rad),
        y = sin(rad),
    )
}
