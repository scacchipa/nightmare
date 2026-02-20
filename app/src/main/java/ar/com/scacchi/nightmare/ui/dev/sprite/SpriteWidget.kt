package ar.com.scacchi.nightmare.ui.dev.sprite

import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.widget.StringSpinner

@Composable
fun SpriteWidget(
    viewModel: SpriteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Button(
        onClick = { viewModel.updateBitmap()  }
    ) {
        Text(text = "Update Bitmap")
    }

    StringSpinner(
        position = state.spinnerPosition,
        values = state.spriteNameList,
        onValueChange = { viewModel.updateSpinnerPosition(it) }
    )

    Image(
        bitmap = state.doomImage.toBitmap().asImageBitmap(),
        contentDescription = null,
    )
}

