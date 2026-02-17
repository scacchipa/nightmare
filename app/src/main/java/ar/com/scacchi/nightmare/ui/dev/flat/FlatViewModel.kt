package ar.com.scacchi.nightmare.ui.dev.flat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomImage
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlatViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FlatState(0, listOf(), DoomImage(100, 100))
    )
    val uiState = _uiState as StateFlow<FlatState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val palette = engine.gameStateFlow.value.playPal[0]
            val fName = engine.getFlatNameList()[0]
            val flat = engine.getFlat(fName)
            _uiState.emit(
                FlatState(
                    spinnerPosition = 0,
                    flatNameList = engine.getFlatNameList() ,
                    doomImage = flat.buildDoomImage(palette)
                )
            )
            println(engine.getFlatNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = engine.gameStateFlow.value.playPal[0]
            val pName = engine.getFlatNameList()[position]
            val patch = engine.getFlat(pName)
            _uiState.emit(
                FlatState(
                    spinnerPosition = position,
                    flatNameList = engine.getFlatNameList(),
                    doomImage = patch.buildDoomImage(palette)
                )
            )
        }
    }
}