package ar.com.scacchi.nightmare.ui.dev.patch

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.NmPalette

data class PatchState(
    val palette: NmPalette,
    val spinnerPosition: Int,
    val patchNameList: List<String>,
    val doomBitmap: DoomBitmap
)