package ar.com.scacchi.nightmare.ui.dev.sprite

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette

data class SpriteState(
    val palette: Palette,
    val spinnerPosition: Int,
    val spriteNameList: List<String>,
    val doomBitmap: DoomBitmap
)