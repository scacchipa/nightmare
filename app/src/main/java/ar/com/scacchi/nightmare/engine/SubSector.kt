package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.data.map.SubSectorLump

class SubSector(
    val segCount: Int,
    val firstSegId: Int,
)

fun SubSectorLump.toSubSector(): SubSector = SubSector(
    segCount = segCount.toInt(),
    firstSegId = firstSegId.toInt(),
)