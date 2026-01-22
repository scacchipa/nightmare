package ar.com.scacchi.nightmare.ui

import android.content.Context
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainState(
            engine = Engine.createFrom(context, R.raw.doom),
        )
    )
    val uiState: StateFlow<MainState> = _uiState


    fun onEventKey(eventKey: KeyEvent) {
        viewModelScope.launch {

            val newPlayer = with(_uiState.value.engine.player) {
                when (eventKey.type) {
                    KeyEventType.KeyDown -> when (eventKey.key) {
                        Key.W -> advance()
                        Key.S -> reverse()
                        Key.A -> moveLeft()
                        Key.D -> moveRight()
                        Key.Q -> turnLeft()
                        Key.E -> turnRight()
                        else -> this
                    }

                    else -> this
                }
            }

            println(newPlayer)

            _uiState.emit(
                _uiState.value.copy(
                    engine = _uiState.value.engine.copy(
                        player = newPlayer
                    )
                )
            )
        }
    }
}
