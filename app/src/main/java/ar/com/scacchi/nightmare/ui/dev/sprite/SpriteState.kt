package ar.com.scacchi.nightmare.ui.dev.sprite

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.NmPalette

data class SpriteState(
    val palette: NmPalette,
    val spinnerPosition: Int,
    val spriteNameList: List<String>,
    val doomBitmap: DoomBitmap
)