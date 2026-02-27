package ar.com.scacchi.nightmare.data.asset.texture

import ar.com.scacchi.nightmare.data.asset.texture.TextureHeader
import ar.com.scacchi.nightmare.data.asset.texture.TextureMap
import java.nio.ByteBuffer

class Texture(
    val header: TextureHeader,
    val mapTextureList: List<TextureMap>
) {
    fun createFrom(buffer: ByteBuffer, offset: Int): Texture {
        buffer.position(offset)
        val header = TextureHeader.Companion.createFrom(buffer)

        val mapTextureList = List(header.textureCount.toInt()) {
            TextureMap.Companion.createFrom(buffer, offset + header.textureOffset.toInt())
        }

        return Texture(
            header = header,
            mapTextureList = mapTextureList
        )
    }
}