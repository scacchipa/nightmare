package ar.com.scacchi.nightmare.data.asset.texture

class Texture(
    val header: TextureHeader,
    val mapTextureList: List<TextureMap>
) {
    fun getPatchMap(textureId: Int, patchMapId: Int): PatchMap {
        return mapTextureList[textureId].patchMapList[patchMapId]
    }

}