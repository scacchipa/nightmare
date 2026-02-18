package ar.com.scacchi.nightmare.ui.dev.sprite

import ar.com.scacchi.nightmare.data.asset.DoomImage

data class SpriteState(
    val spinnerPosition: Int,
    val spriteNameList: List<String>,
    val doomImage: DoomImage
)