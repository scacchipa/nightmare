package ar.com.scacchi.nightmare.ui.dev.colormap

import android.content.Context
import androidx.lifecycle.ViewModel
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ColorMapViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ColorMapModel(
            engine = Engine.Companion.createFrom(context, R.raw.doom),
        )
    )
    val uiState: StateFlow<ColorMapModel> = _uiState
}