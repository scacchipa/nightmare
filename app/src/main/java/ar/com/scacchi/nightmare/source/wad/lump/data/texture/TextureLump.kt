package ar.com.scacchi.nightmare.source.wad.lump.data.texture

import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class TextureLump(
    val header: TextureHeader,
    val mapTextureList: List<TextureMap>
) {
    fun getPatchMap(textureId: Int, patchMapId: Int): PatchMap {
        return mapTextureList[textureId].patchMapList[patchMapId]
    }

    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): TextureLump {
            val offset = lump.filePos

            buffer.position(offset)

            val header = TextureHeader.createFrom(buffer)

            val mapTextureMap = header.textureDataOffset.map { mapOffset ->
                TextureMap.readTextureMap(buffer, offset + mapOffset.toInt())
            }

            return TextureLump(
                header = header,
                mapTextureList = mapTextureMap
            )
        }
    }
}