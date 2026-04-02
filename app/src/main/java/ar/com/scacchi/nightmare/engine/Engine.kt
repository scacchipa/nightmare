package ar.com.scacchi.nightmare.engine;

import android.content.Context
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.di.ApplicationScope
import ar.com.scacchi.nightmare.di.IoDispatcher
import ar.com.scacchi.nightmare.source.wad.WadManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Engine @Inject constructor(
    val wadManager: WadManager,
    @param:ApplicationScope private val externalScope: CoroutineScope,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:ApplicationContext private val context: Context,
) {
    val episodeName = "E1M1"
    var player = wadManager.getInitialPlayer(episodeName)

    private val _gameState = MutableStateFlow(GameState.getEmpty())
    val gameStateFlow: StateFlow<GameState> = _gameState

    suspend fun update() {
        _gameState.emit(GameState(
            playPal = wadManager.playPal,
            colorMap = wadManager.colorMap,
            episodeMap = wadManager.getEpisodeMap(episodeName),
            skyTex = "SKY1",
            player = player,
        ))
    }

    suspend fun updatePlayer(newPlayer: Player) {
        player = newPlayer
        _gameState.emit(
            GameState(
                playPal = wadManager.playPal,
                colorMap = wadManager.colorMap,
                episodeMap = wadManager.getEpisodeMap(episodeName),
                skyTex = "SKY1",
                player = player,
            )
        )
    }

    suspend fun emitState() {
        _gameState.emit(GameState(
            playPal = wadManager.playPal,
            colorMap = wadManager.colorMap,
            episodeMap = wadManager.getEpisodeMap(episodeName),
            skyTex = "SKY1",
            player = player,
        ))
    }

//    fun getEpisodeMap(name: String): EpisodeMap = wadManager.getEpisodeMap(name)

    fun getPlayPal(): PlayPal = wadManager.playPal
    fun getColorMap(): ColorMap = wadManager.colorMap
    fun getPatchNameList(): List<String> = wadManager.readPatchNameList()
    fun getFlatNameList(): List<String> = wadManager.readFlatNameList()
    fun getSpriteNameList(): List<String> = wadManager.readSpriteNameList()

//    fun getPlayer(): Player = player
}
