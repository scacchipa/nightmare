package ar.com.scacchi.nightmare.ui.dev.colormap

import androidx.lifecycle.ViewModel
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ColorMapViewModel @Inject constructor(
    engine: Engine,
) : ViewModel() {
    val uiStateFlow = engine.gameStateFlow
}