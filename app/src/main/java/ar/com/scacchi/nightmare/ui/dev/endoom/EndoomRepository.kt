package ar.com.scacchi.nightmare.ui.dev.endoom

import ar.com.scacchi.nightmare.source.wad.WadManager
import javax.inject.Inject

class EndoomRepository @Inject constructor(
    private val wadManager: WadManager
) {
    fun getEndoom() = wadManager.getEndoom()
}