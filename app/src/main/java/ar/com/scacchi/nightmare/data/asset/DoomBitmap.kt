package ar.com.scacchi.nightmare.data.asset

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import ar.com.scacchi.nightmare.data.color.Palette
import kotlin.math.max
import kotlin.math.min

class DoomBitmap(
    val width: Int,
    val height: Int,
) {
    val pixels: Array<Array<UByte?>> = Array(width) {
        arrayOfNulls(height)
    }

    operator fun set(x: Int, y: Int, palettePos: UByte?) {
        pixels[x][y] = palettePos
    }

    operator fun get(x: Int, y: Int): UByte? = pixels[x][y]

    fun toBitmap(palette: Palette): Bitmap =
        createBitmap(width, height).also { bitmap ->
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val palettePos = pixels[x][y]
                    bitmap[x, y] =
                        if (palettePos == null) Color.TRANSPARENT
                        else palette[palettePos.toInt()].toArgb()
                }
            }
        }

    fun print(xOffset: Int, yOffset: Int, bitmap: DoomBitmap) {

        val xStart = max(xOffset, 0)
        val yStart = max(yOffset, 0)

        val xEnd = min(xOffset + bitmap.width, width)
        val yEnd = min(yOffset + bitmap.height, height)

        for (x in xStart until xEnd)
            for(y in yStart until yEnd) {
                this[x, y] = bitmap[x - xOffset, y - yOffset]
            }
    }
}