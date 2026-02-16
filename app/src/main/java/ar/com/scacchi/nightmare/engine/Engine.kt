package ar.com.scacchi.nightmare.engine;

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.data.asset.Patch
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.di.ApplicationScope
import ar.com.scacchi.nightmare.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Engine @Inject constructor(
    @param:ApplicationScope private val externalScope: CoroutineScope,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:ApplicationContext private val context: Context,
) {
    var episodeName = "E1M1"
    val wadPath = R.raw.doom
    val wadManager: WadManager by lazy {
        val file = context.resources.openRawResource(wadPath)
        val buffer = ByteBuffer.wrap(file.readBytes())
        WadManager.create(buffer)
    }

    private val _gameState = MutableStateFlow(
        GameState.getEmpty(),
    )
    val gameStateFlow: StateFlow<GameState> = _gameState

    init {
        externalScope.launch(ioDispatcher) {
            initEpisode("E1M1")
        }
    }

    suspend fun update(
        newGameState: GameState
    ) {
        _gameState.emit(newGameState)
    }

    suspend fun updatePlayer(newPlayer: Player) {
        _gameState.emit(
            _gameState.value.copy(
                player = newPlayer
            )
        )
    }

    suspend fun initEpisode(episodeName: String) {
        this.episodeName = episodeName
        val episodeMap = wadManager.getEpisodeMap(episodeName)
        _gameState.emit(
            GameState(
                playPal = wadManager.playPal,
                colorMap = wadManager.colorMap,
                episodeMap = episodeMap,
                player = Player(episodeMap.things[0])
            )
        )
    }

    fun getPlayPal(): PlayPal = wadManager.playPal
    fun getColorMap(): ColorMap = wadManager.colorMap
    fun getPatch(name: String): Patch = wadManager.getPatch(name)
}
