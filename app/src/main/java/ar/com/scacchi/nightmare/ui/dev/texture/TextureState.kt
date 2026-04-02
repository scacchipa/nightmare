package ar.com.scacchi.nightmare.ui.dev.texture

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette

data class TextureState(
    val palette: Palette,
    val spinnerPosition: Int,
    val itemCount: Int,
    val spinnerValues: List<String>,
    val doomBitmap: DoomBitmap
)