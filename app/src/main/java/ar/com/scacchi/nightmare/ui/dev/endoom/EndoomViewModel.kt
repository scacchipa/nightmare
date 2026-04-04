package ar.com.scacchi.nightmare.ui.dev.endoom

import androidx.lifecycle.ViewModel
import ar.com.scacchi.nightmare.data.endoom.EndoomLump
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EndoomViewModel @Inject constructor(
    private val endoomRepository: EndoomRepository
) : ViewModel() {
    fun getText(): String {
        val endoom: EndoomLump = endoomRepository.getEndoom()
        val stringBuilder = StringBuilder()
        for (y in 0 until 25) {
            for (x in 0 until 80) {
                stringBuilder.append(endoom[x, y].letter)
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}