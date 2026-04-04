package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.TextureMapContainer
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SideDefsLump

class SideDefs(
    val content: Array<SideDef>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySideDefs(): SideDefs = SideDefs(emptyArray())
    }
}

fun SideDefsLump.toSideDefs(
    sectors: Sectors, textureMapContainer: TextureMapContainer
): SideDefs = SideDefs(
    content = Array(this.content.size) { idx ->
        this.content[idx].toSideDef(sectors, textureMapContainer)
    }
)