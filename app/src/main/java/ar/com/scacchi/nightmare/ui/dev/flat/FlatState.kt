package ar.com.scacchi.nightmare.ui.dev.flat

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette

data class FlatState(
    val palette: Palette,
    val spinnerPosition: Int,
    val flatNameList: List<String>,
    val doomBitmap: DoomBitmap,
)