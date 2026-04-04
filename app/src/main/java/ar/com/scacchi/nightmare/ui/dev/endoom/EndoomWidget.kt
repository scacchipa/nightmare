package ar.com.scacchi.nightmare.ui.dev.endoom

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun EndoomWidget(
    viewModel: EndoomViewModel = hiltViewModel()
) {
    Text(text = viewModel.getText())
}

