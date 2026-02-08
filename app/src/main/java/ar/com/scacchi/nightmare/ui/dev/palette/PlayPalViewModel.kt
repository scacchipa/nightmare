package ar.com.scacchi.nightmare.ui.dev.palette

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayPalViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PlayPalModel(
            engine = Engine.createFrom(context, R.raw.doom),
            currentPaletteSelected = 1,
        )
    )
    val uiState: StateFlow<PlayPalModel> = _uiState

    fun setPaletteSelected(value: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(currentPaletteSelected = value)
        }
    }
}