package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.data.readUByte
import ar.com.scacchi.nightmare.source.wad.NmColor
import java.nio.ByteBuffer

class PalColor(
    val red: UByte,
    val green: UByte,
    val blue: UByte,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): PalColor {
            return PalColor(
                red = buffer.readUByte(),
                green = buffer.readUByte(),
                blue = buffer.readUByte(),
            )
        }
    }
}

fun PalColor.toNmColor(): NmColor = NmColor(
    alpha = 0xFF.toUByte(),
    red = this.red,
    green = this.green,
    blue = this.blue,
)