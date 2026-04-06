package ar.com.scacchi.nightmare.ui.dev.sprite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.source.wad.NmPalette
import ar.com.scacchi.nightmare.source.wad.WadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpriteViewModel @Inject constructor(
    private val wadManager: WadManager,
    private val imageRepository: ImageRepository,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SpriteState(NmPalette(emptyArray()) ,0, listOf(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<SpriteState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val playPal = wadManager.playPal
            if (playPal.paletteCount == 0) return@launch
            val palette = playPal[0]
            val sName = wadManager.readSpriteNameList()[0]
            val sprite = imageRepository.getSpriteDoomBitmap(sName)
            _uiState.emit(
                SpriteState(
                    palette = palette,
                    spinnerPosition = 0,
                    spriteNameList = wadManager.readSpriteNameList(),
                    doomBitmap = sprite
                )
            )

            println(wadManager.readSpriteNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val sName = wadManager.readSpriteNameList()[position]
            val sprite = wadManager.getSprite(sName)
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