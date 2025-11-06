package ar.com.scacchi.nightmare.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    Text("Hello World!")
}