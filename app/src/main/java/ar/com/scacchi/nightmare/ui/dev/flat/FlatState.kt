package ar.com.scacchi.nightmare.ui.dev.flat

import ar.com.scacchi.nightmare.data.asset.DoomImage

data class FlatState(
    val spinnerPosition: Int,
    val flatNameList: List<String>,
    val doomImage: DoomImage
)