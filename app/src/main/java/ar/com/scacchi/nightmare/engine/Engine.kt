package ar.com.scacchi.nightmare.engine;

import android.content.Context
import androidx.annotation.RawRes
import ar.com.scacchi.nightmare.R
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
    private val _gameState = MutableStateFlow(
        GameState.getEmpty(),
    )
    val gameStateFlow: StateFlow<GameState> = _gameState

    init {
        externalScope.launch(ioDispatcher) {
            initWithRawId(R.raw.doom)
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

    suspend fun initWithRawId(@RawRes wadPath: Int) {
            val file = context.resources.openRawResource(wadPath)
            val buffer = ByteBuffer.wrap(file.readBytes())

            _gameState.emit(GameState.create(buffer, "E1M1"))
    }
}
