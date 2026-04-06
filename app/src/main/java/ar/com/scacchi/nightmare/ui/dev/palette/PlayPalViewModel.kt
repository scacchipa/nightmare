package ar.com.scacchi.nightmare.ui.dev.palette

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.source.wad.NmColor
import ar.com.scacchi.nightmare.source.wad.NmPalette
import ar.com.scacchi.nightmare.source.wad.NmPlayPal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayPalViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlayPalState(NmPlayPal(arrayOf(NmPalette(Array(256){ NmColor.White }))), 0)
    )
    val uiState = _uiState as StateFlow<PlayPalState>

    fun initState() {
        viewModelScope.launch(defaultDispatcher) {
            val playPal = engine.gameStateFlow.value.playPal

            if (playPal.paletteCount >= 0) {
                PlayPalState(playPal, 1)
            }
        }
    }

    fun setPaletteSelected(value: Int) {
        viewModelScope.launch(defaultDispatcher) {
            _uiState.emit(
                PlayPalState(engine.getPlayPal(), value)
            )
        }
    }
}