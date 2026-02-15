package ar.com.scacchi.nightmare.data.asset

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

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

    fun toBitmap(): Bitmap {
        val bitmap = createBitmap(width, height)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = pixels[x][y]
                val pixelColor = color?.toArgb() ?: android.graphics.Color.TRANSPARENT
                bitmap[x, y] = pixelColor
            }
        }
        return bitmap
    }
}