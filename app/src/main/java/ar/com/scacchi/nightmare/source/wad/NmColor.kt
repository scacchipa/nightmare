package ar.com.scacchi.nightmare.source.wad

import androidx.compose.ui.graphics.Color

class NmColor(
    val alpha: UByte,
    val red: UByte,
    val green: UByte,
    val blue: UByte,
) {
    val argb: Int = (alpha.toInt() shl 24) or
            (blue.toInt() shl 16) or
            (green.toInt() shl 8) or
            (red.toInt())
    val color: Color = Color(
        color = (alpha.toInt() shl 24) or
                (red.toInt() shl 16) or
                (green.toInt() shl 8) or
                (blue.toInt())
    )

    companion object {
        val Transparent = NmColor(0u, 0u, 0u, 0u)
        val White = NmColor(0xFFu, 0xFFu, 0xFFu, 0xFFu)
    }
}