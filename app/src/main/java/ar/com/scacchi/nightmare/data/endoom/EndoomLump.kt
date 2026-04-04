package ar.com.scacchi.nightmare.data.endoom

import androidx.compose.ui.graphics.Color
import ar.com.scacchi.nightmare.data.readUByte
import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class EndoomLump {
    val content: Array<Letter>

    constructor(content: Array<Letter>) {
        this.content = content
    }

    operator fun get(x: Int, y: Int) = content[y * 80 + x]

    companion object {
        fun createFromLump( buffer: ByteBuffer, lump: FileLump): EndoomLump {
            buffer.position(lump.filePos)

            return EndoomLump(Array(80 * 25) {
                    val x = buffer.readUByte()
                    val y = buffer.readUByte()
                    Letter(x, y)
            })
        }
    }
}

val DOS_COLORS: List<Color>
    get() = listOf(
        Color(0xFF000000),// 0. Negro
        Color(0xFF0000AA),// 1. Azul Oscuro
        Color(0xFF00AA00),// 2. Verde Oscuro
        Color(0xFF00AAAA),// 3. Cian Oscuro
        Color(0xFFAA0000),// 4. Rojo Oscuro
        Color(0xFFAA00AA), // 5. Magenta Oscuro
        Color(0xFFAAA500), // 6. Marrón / Amarillo Oscuro
        Color(0xFFAAAAAA),// 7. Gris Claro
        // --- Colores Brillantes (High Intensity) ---
        Color(0xFF555555),// 8. Gris Oscuro (se usa como el color de baja intensidad más oscuro)
        Color(0xFF5555FF),// 9. Azul Brillante
        Color(0xFF55FF55),// 10. Verde Brillante
        Color(0xFF55FFFF),// 11. Cian Brillante
        Color(0xFFFF5555),// 12. Rojo Brillante
        Color(0xFFFF55FF),// 13. Magenta Brillante
        Color(0xFFFFFF55),// 14. Amarillo Brillante
        Color(0xFFFFFFFF),// 15. Blanco Brillante
    )