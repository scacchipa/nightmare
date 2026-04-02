package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.ext.asString
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SideDefLump

class SideDef(
    val xOffset: Int,
    val yOffset: Int,
    val upperTextureName: String,
    val lowerTextureName: String,
    val middleTextureName: String,
    val sectorId: Int,
    val sector: Sector
)

fun SideDefLump.toSideDef(sectors: Sectors): SideDef = SideDef(
    xOffset = xOffset.toInt(),
    yOffset = yOffset.toInt(),
    upperTextureName = upperTextureName.asString(),
    lowerTextureName = lowerTextureName.asString(),
    middleTextureName = middleTextureName.asString(),
    sectorId = sectorId.toInt(),
    sector = sectors[sectorId.toInt()],
)