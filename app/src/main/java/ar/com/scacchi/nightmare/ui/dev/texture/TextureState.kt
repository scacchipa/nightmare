package ar.com.scacchi.nightmare.ui.dev.texture

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.NmPalette

data class TextureState(
    val palette: NmPalette,
    val spinnerPosition: Int,
    val itemCount: Int,
    val spinnerValues: List<String>,
    val doomBitmap: DoomBitmap
)