package ar.com.scacchi.nightmare.ui.dev.colormap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorMapViewModel @Inject constructor(
    engine: Engine,
) : ViewModel() {
    private val _paletteIdxSF = MutableStateFlow(0)

    val uiStateFlow = combine(
        flow = engine.gameStateFlow,
        flow2 = _paletteIdxSF,
    ) { gameState, paletteIdx ->
        ColorMapModel(gameState.playPal, gameState.colorMap, paletteIdx)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ColorMapModel(PlayPal(emptyArray()), ColorMap(emptyArray()), 0)
    )

    fun setPaletteSelected(id: Int) {
        viewModelScope.launch {
            _paletteIdxSF.emit(id)
        }
    }

}