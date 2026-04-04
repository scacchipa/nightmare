package ar.com.scacchi.nightmare.ui.dev.patch

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette

data class PatchState(
    val palette: Palette,
    val spinnerPosition: Int,
    val patchNameList: List<String>,
    val doomBitmap: DoomBitmap
)