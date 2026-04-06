package ar.com.scacchi.nightmare.ui.dev.flat

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.NmPalette

data class FlatState(
    val palette: NmPalette,
    val spinnerPosition: Int,
    val flatNameList: List<String>,
    val doomBitmap: DoomBitmap,
)