package ar.com.scacchi.nightmare.data.asset

import android.graphics.Bitmap
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
    val pixels: ShortArray = ShortArray(width * height) { -1 }

    operator fun set(x: Int, y: Int, palettePos: Short) {
        pixels[x * height + y] = palettePos
    }
    operator fun set(x: Int, y: Int, palettePos: Byte) {
        pixels[x * height + y] = palettePos.toShort()
    }

    operator fun get(x: Int, y: Int): Short = pixels[x * height + y]

    fun toBitmap(palette: Palette): Bitmap =
        createBitmap(width, height).also { bitmap ->
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val index = pixels[x * height + y]

                    if (index != (-1).toShort()) {
                        bitmap[x, y] = palette[index.toInt()].toArgb()
                    }
                }
            }
        }

    fun print(xOffset: Int, yOffset: Int, bitmap: DoomBitmap) {

        val xStart = max(xOffset, 0)
        val yStart = max(yOffset, 0)

        val xEnd = min(xOffset + bitmap.width, width)
        val yEnd = min(yOffset + bitmap.height, height)

        for (x in xStart until xEnd)
            for (y in yStart until yEnd) {
                val pixel = bitmap[x - xOffset, y - yOffset]
                if (pixel != (-1).toShort())
                    this[x, y] = pixel
            }
    }
}