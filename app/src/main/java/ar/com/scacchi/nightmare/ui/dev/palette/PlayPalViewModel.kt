package ar.com.scacchi.nightmare.ui.dev.palette

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayPalViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _currentPaletteStateFlow = MutableStateFlow(1)
    private val _uiState = combine(
        flow = engine.gameStateFlow,
        flow2 = _currentPaletteStateFlow,
    ) { gameState, currentPaletteSelected ->
        PlayPalModel(
            playPal = gameState.playPal,
            currentPaletteSelected = currentPaletteSelected,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PlayPalModel(PlayPal(emptyArray()), 0)
    )

    val uiState: StateFlow<PlayPalModel> = _uiState

    fun setPaletteSelected(value: Int) {
        viewModelScope.launch(defaultDispatcher) {
            _currentPaletteStateFlow.emit(value)
        }
    }
}