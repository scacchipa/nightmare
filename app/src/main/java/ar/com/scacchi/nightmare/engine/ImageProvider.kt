package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.WadManager
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import javax.inject.Inject

class ImageProvider @Inject constructor(
    val wadManager: WadManager,
) {
    val patchImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val flatImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val pictureImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val textureMapMap: MutableMap<Int, DoomBitmap> = hashMapOf()

    fun getPatchDoomBitmap(name: String): DoomBitmap =
        patchImageMap.getOrPut(name) {
            wadManager.readPatch(name).buildDoomImage()
        }

    fun getFlatDoomBitmap(name: String): DoomBitmap =
        flatImageMap.getOrPut(name) {
            wadManager.readFlat(name).buildDoomImage()
        }

    fun getPictureDoomBitmap(name: String): DoomBitmap =
        pictureImageMap.getOrPut(name) {
            wadManager.readPicture(name).buildDoomImage()
        }

    fun getPatchMap(textureMapId: Int): DoomBitmap {
        return textureMapMap.getOrPut(textureMapId) {
            wadManager.getTextureList()[textureMapId].buildDoomImage(this, wadManager)
        }
    }
}