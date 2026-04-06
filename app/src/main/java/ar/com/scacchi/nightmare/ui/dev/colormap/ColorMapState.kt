package ar.com.scacchi.nightmare.ui.dev.colormap

import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.source.wad.NmPlayPal

data class  ColorMapState(
    val playPal: NmPlayPal,
    val colorMap: ColorMap,
    val paletteIdx: Int,
)