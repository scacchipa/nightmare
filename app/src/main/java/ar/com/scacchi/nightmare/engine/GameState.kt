package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.EpisodeMap
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.source.wad.NmPlayPal

data class GameState(
    val playPal: NmPlayPal,
    val colorMap: ColorMap,
    val episodeMap: EpisodeMap,
    val skyTex: String?,
    val player: Player,
) {
    companion object {
        fun getEmpty(): GameState = GameState(
            playPal = NmPlayPal(emptyArray()),
            colorMap = ColorMap(emptyArray()),
            episodeMap = EpisodeMap.emptyEpisodeMap(),
            skyTex = null,
            player = Player.emptyPlayer(),
        )
    }
}