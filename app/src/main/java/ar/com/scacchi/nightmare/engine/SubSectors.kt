package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.data.map.SubSectorsLump

class SubSectors(
    val content: Array<SubSector>,
) {
    operator fun get(idx: Int) = content[idx]
    fun count(): Int = content.size

    companion object {
        fun emptySubSectors(): SubSectors = SubSectors(emptyArray())
    }
}

fun SubSectorsLump.toSubSectors(): SubSectors = SubSectors(
    content = Array(content.size) { idx ->
        content[idx].toSubSector()
    }
)