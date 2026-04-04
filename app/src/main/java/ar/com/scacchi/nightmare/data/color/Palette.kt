package ar.com.scacchi.nightmare.data.color

import androidx.compose.ui.graphics.Color
import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

class Palette {
    val content: Array<Color>

    constructor(array: Array<Color>) {
        this.content = array
    }

    operator fun get(idx: Int): Color = content[idx]

    companion object {
        fun createFrom(buffer: ByteBuffer): Palette {
            return Palette(Array(256) {
                Color(
                    red = buffer.readUByte().toInt(),
                    green = buffer.readUByte().toInt(),
                    blue = buffer.readUByte().toInt(),
                    alpha = 255,
                )
            })
        }
    }
}
