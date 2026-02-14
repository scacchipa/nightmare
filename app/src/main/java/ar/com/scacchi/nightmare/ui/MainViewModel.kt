package ar.com.scacchi.nightmare.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.ui.pad.PlayerAction
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class MainViewModel @Inject constructor(
    val engine: Engine,
    @param:ApplicationContext private val context: Context,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val gameState = engine.gameStateFlow

    private var _pressedKeySet = emptySet<PlayerAction>()

    val tickerJob: Job = viewModelScope.launch(defaultDispatcher) {
        while (true) {
            val newPlayer = with(_pressedKeySet) {
                val player = gameState.value.player
                when {
                    contains(PlayerAction.MOVE_FORWARD) -> player.advance()
                    contains(PlayerAction.MOVE_BACKWARD) -> player.reverse()
                    contains(PlayerAction.MOVE_LEFT) -> player.moveLeft()
                    contains(PlayerAction.MOVE_RIGHT) -> player.moveRight()
                    contains(PlayerAction.MOVE_LEFT_FORWARD) -> player.moveLeftForward()
                    contains(PlayerAction.MOVE_RIGHT_FORWARD) -> player.moveRightForward()
                    contains(PlayerAction.MOVE_LEFT_BACKWARD) -> player.moveLeftBackward()
                    contains(PlayerAction.MOVE_RIGHT_BACKWARD) -> player.moveRightBackward()
                    contains(PlayerAction.TURN_LEFT) -> player.turnLeft()
                    contains(PlayerAction.TURN_RIGHT) -> player.turnRight()
                    else -> player
                }
            }
            engine.updatePlayer(newPlayer)
            delay(35.toDuration(DurationUnit.MILLISECONDS))
        }
    }

    fun onNewKeySet(newKeySet: Set<PlayerAction>) {
        _pressedKeySet = newKeySet
    }
}
