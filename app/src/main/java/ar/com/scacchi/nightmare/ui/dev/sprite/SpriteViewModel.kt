package ar.com.scacchi.nightmare.ui.dev.sprite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.source.wad.WadManager
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpriteViewModel @Inject constructor(
    val wadManager: WadManager,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SpriteState(Palette(emptyArray()) ,0, listOf(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<SpriteState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val playPal = wadManager.playPal
            if (playPal.paletteCount == 0) return@launch
            val palette = playPal[0]
            val sName = wadManager.readSpriteNameList()[0]
            val sprite = wadManager.readSprite(sName)
            _uiState.emit(
                SpriteState(
                    palette = palette,
                    spinnerPosition = 0,
                    spriteNameList = wadManager.readSpriteNameList(),
                    doomBitmap = sprite.buildDoomImage()
                )
            )

            println(wadManager.readSpriteNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val sName = wadManager.readSpriteNameList()[position]
            val sprite = wadManager.readSprite(sName)
            _uiState.emit(
                SpriteState(
                    palette = palette,
                    spinnerPosition = position,
                    spriteNameList = wadManager.readSpriteNameList(),
                    doomBitmap = sprite.buildDoomImage()
                )
            )
        }
    }
}