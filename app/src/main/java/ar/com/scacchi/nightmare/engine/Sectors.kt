package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.data.map.SectorsLump

class Sectors(
    val content: Array<Sector>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySectors() = Sectors(emptyArray())
    }
}

fun SectorsLump.toSectors(): Sectors = Sectors(
    content = Array(content.size) { idx ->
        this.content[idx].toSector()
    }
)