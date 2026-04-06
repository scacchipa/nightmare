package ar.com.scacchi.nightmare.source.wad

import ar.com.scacchi.nightmare.data.color.PlayPalLump

class NmPlayPal(
    val content: Array<NmPalette>
) {
    operator fun get(idx: Int) = content[idx]

    val paletteCount: Int
        get() = content.size
}

fun PlayPalLump.toPlayPal(): NmPlayPal = NmPlayPal(
    content = this.content.map { it.toPalette() }.toTypedArray()
)