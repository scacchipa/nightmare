package ar.com.scacchi.nightmare.ui.dev.palette

import ar.com.scacchi.nightmare.engine.Engine

data class PlayPalModel(
    val engine: Engine,
    val currentPaletteSelected: Int
)