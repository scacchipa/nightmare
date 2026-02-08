package ar.com.scacchi.nightmare.data.asset

import androidx.compose.ui.graphics.Color

class DoomImage(
    val width: Int,
    val height: Int,
) {
    val pixels = Array(width) {
        arrayOfNulls<Color>(height)
    }

    fun setAt(x: Int, y: Int, color: Color) {
        pixels[x][y] = color
    }
}