package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.EpisodeMap
import ar.com.scacchi.nightmare.data.asset.Picture
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal

data class GameState(
    val playPal: PlayPal,
    val colorMap: ColorMap,
    val episodeMap: EpisodeMap,
    val skyTex: Picture,
    val player: Player,
) {
    companion object {
        fun getEmpty(): GameState = GameState(
            playPal = PlayPal(emptyArray()),
            colorMap = ColorMap(emptyArray()),
            episodeMap = EpisodeMap.emptyEpisodeMap(),
            skyTex = Picture.emptyPicture(),
            player = Player.emptyPlayer(),
        )
    }
}