package ar.com.scacchi.nightmare.data.asset

import android.graphics.Bitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import ar.com.scacchi.nightmare.data.color.Palette

class DoomBitmap(
    val width: Int,
    val height: Int,
) {
    val pixels = Array(width) {
        arrayOfNulls<UByte>(height)
    }

    operator fun set(x: Int, y: Int, palettePos: UByte) {
        pixels[x][y] = palettePos
    }

    operator fun get(x: Int, y: Int): UByte? = pixels[x][y]

    fun toBitmap(palette: Palette): Bitmap =
        createBitmap(width, height).also { bitmap ->
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val palettePos = pixels[x][y]
                    bitmap[x, y] =
                        if (palettePos == null) android.graphics.Color.TRANSPARENT
                        else palette[palettePos.toInt()].toArgb()
                }
            }
        }
}