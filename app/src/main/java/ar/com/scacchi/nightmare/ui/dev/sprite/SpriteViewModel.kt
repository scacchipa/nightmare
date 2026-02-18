package ar.com.scacchi.nightmare.ui.dev.sprite

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
class SpriteViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SpriteState(0, listOf(), DoomImage(100, 100))
    )
    val uiState = _uiState as StateFlow<SpriteState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val playPal = engine.gameStateFlow.value.playPal
            if (playPal.paletteCount == 0) return@launch
            val palette = playPal[0]
            val sName = engine.getSpriteNameList()[0]
            val sprite = engine.getSprite(sName)
            _uiState.emit(
                SpriteState(
                    spinnerPosition = 0,
                    spriteNameList = engine.getSpriteNameList(),
                    doomImage = sprite.buildDoomImage(palette)
                )
            )

            println(engine.getSpriteNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = engine.gameStateFlow.value.playPal[0]
            val sName = engine.getSpriteNameList()[position]
            val sprite = engine.getSprite(sName)
            _uiState.emit(
                SpriteState(
                    spinnerPosition = position,
                    spriteNameList = engine.getSpriteNameList(),
                    doomImage = sprite.buildDoomImage(palette)
                )
            )
        }
    }
}