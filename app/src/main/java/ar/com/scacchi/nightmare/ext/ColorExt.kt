package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.graphics.Color

fun Color.light(lightLevel: Float): Color {
    return Color(
        red = this.red * lightLevel,
        green = this.green * lightLevel,
        blue = this.blue * lightLevel,
        alpha = this.alpha,
        colorSpace = this.colorSpace
    )
}