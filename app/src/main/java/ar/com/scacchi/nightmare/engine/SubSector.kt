package ar.com.scacchi.nightmare.engine

class SubSector(
    val segCount: Int,
    val firstSegId: Int,
)

fun SubSectorLump.toSubSector(): SubSector = SubSector(
    segCount = segCount.toInt(),
    firstSegId = firstSegId.toInt(),
)