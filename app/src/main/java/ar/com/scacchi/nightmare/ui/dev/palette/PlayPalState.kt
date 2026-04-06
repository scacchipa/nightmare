package ar.com.scacchi.nightmare.ui.dev.palette

import ar.com.scacchi.nightmare.source.wad.NmPlayPal

data class PlayPalState(
    val playPal: NmPlayPal,
    val currentPaletteSelected: Int
)