package ar.com.scacchi.nightmare.ui.dev.patch

import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PatchWidget(
    viewModel: PatchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Button(
        onClick = { viewModel.updateBitmap()  }
    ) {
        Text(text = "Update Bitmap")
    }

    Image(
        bitmap = state.doomImage.toBitmap().asImageBitmap(),
        contentDescription = null,
    )
}