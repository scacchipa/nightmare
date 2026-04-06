package ar.com.scacchi.nightmare.source.wad

import ar.com.scacchi.nightmare.data.color.PaletteLump
import ar.com.scacchi.nightmare.data.color.toNmColor

class NmPalette(
    private val content: Array<NmColor>
) {
    operator fun get(idx: Int): NmColor = content[idx]
}

fun PaletteLump.toPalette(): NmPalette = NmPalette(
    this.content.map { it.toNmColor() }.toTypedArray()
)