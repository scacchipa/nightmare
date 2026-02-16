package ar.com.scacchi.nightmare.ui.dev.colormap

import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal

data class  ColorMapState(
    val playPal: PlayPal,
    val colorMap: ColorMap,
    val paletteIdx: Int,
)