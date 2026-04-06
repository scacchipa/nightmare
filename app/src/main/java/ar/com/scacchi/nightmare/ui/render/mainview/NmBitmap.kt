package ar.com.scacchi.nightmare.ui.render.mainview

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.settings.SCREEN_WIDTH
import ar.com.scacchi.nightmare.source.wad.NmColor
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs

class NmBitmap(val width: Int, val height: Int) {
    private val byteBuffer = ByteBuffer.allocateDirect(SCREEN_WIDTH.toInt() * SCREEN_HEIGHT.toInt() * 4)
        .order(ByteOrder.LITTLE_ENDIAN)
    private val intBuffer = byteBuffer.asIntBuffer()

    fun getBitmap(): Bitmap {
        byteBuffer.rewind()
        return createBitmap(SCREEN_WIDTH.toInt(), SCREEN_HEIGHT.toInt(), Bitmap.Config.ARGB_8888).also {
            it.copyPixelsFromBuffer(byteBuffer)
        }
    }

    fun drawPixel(x: Int, y: Int, color: NmColor) {
        intBuffer.put(x + y * width, color.argb)
    }

    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: NmColor) {
        var curX = x1
        var curY = y1

        val dx = abs(x2 - x1)
        val dy = abs(y2 - y1)

        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1

        var err = dx - dy

        while (true) {
            intBuffer.put(curY * width + curX, color.argb)

            if (curX == x2 && curY == y2) break

            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                curX += sx
            }
            if (e2 < dx) {
                err += dx
                curY += sy
            }
        }
    }

    fun drawVerticalLine(x: Int, y1: Int, y2: Int, color: NmColor) {
        if (y1 > y2) return
        var offset = y1 * width + x

        repeat(y2 - y1) {
            intBuffer.put(offset, color.argb)
            offset += width
        }
    }
}