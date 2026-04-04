package ar.com.scacchi.nightmare.source.wad

import ar.com.scacchi.nightmare.source.wad.lump.data.texture.TextureLump
import ar.com.scacchi.nightmare.source.wad.lump.data.texture.TextureMap

class TextureMapContainer(
    val content: Array<TextureMap>,
) {
    val size: Int = content.size

    operator fun plus(other: TextureMapContainer): TextureMapContainer = TextureMapContainer(content + other.content)
    operator fun get(idx: Int): TextureMap = content[idx]
    fun indexOf(pName: String): Int = content.indexOfFirst { it.name == pName }
}

fun TextureLump.toTextureMapList(): TextureMapContainer = TextureMapContainer(
    content = mapTextureList.toTypedArray()
)
