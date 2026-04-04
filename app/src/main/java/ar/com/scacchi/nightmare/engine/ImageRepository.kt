package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.WadManager
import javax.inject.Inject

class ImageRepository @Inject constructor(
    val wadManager: WadManager,
) {
    val patchImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val spriteImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val pictureImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val flatImageMap: MutableMap<String, DoomBitmap> = hashMapOf()
    val textureMapMap: MutableMap<Int, DoomBitmap> = hashMapOf()

    fun getPatchDoomBitmap(name: String): DoomBitmap = patchImageMap.getOrPut(name) {
            wadManager.getPatch(name).buildDoomImage()
        }

    fun getSpriteDoomBitmap(name: String): DoomBitmap = spriteImageMap.getOrPut(name) {
            wadManager.getSprite(name).buildDoomImage()
        }

    fun getPictureDoomBitmap(name: String): DoomBitmap = pictureImageMap.getOrPut(name) {
            wadManager.getPicture(name).buildDoomImage()
        }

    fun getFlatDoomBitmap(name: String): DoomBitmap = flatImageMap.getOrPut(name) {
            wadManager.getFlat(name).buildDoomImage()
        }


    fun getTextureDoomBitmap(textureMapId: Int): DoomBitmap {
        return textureMapMap.getOrPut(textureMapId) {
            wadManager.getTextureMap(textureMapId).buildDoomImage(this, wadManager)
        }
    }
}