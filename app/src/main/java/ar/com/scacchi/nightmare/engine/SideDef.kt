package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.ext.asString
import ar.com.scacchi.nightmare.source.wad.TextureMapContainer
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SideDefLump

class SideDef(
    val xOffset: Int,
    val yOffset: Int,
    val upperTextureName: String,
    val middleTextureName: String,
    val lowerTextureName: String,
    val lowerTextureId: Int,
    val middleTextureId: Int,
    val upperTextureId: Int,
    val sectorId: Int,
    val sector: Sector,
)

fun SideDefLump.toSideDef(sectors: Sectors, textureMapContainer: TextureMapContainer): SideDef {
    val upperTextureName = upperTextureName.asString()
    val middleTextureName = middleTextureName.asString()
    val lowerTextureName = lowerTextureName.asString()

    return SideDef(
        xOffset = xOffset.toInt(),
        yOffset = yOffset.toInt(),
        upperTextureName = upperTextureName,
        middleTextureName = middleTextureName,
        lowerTextureName = lowerTextureName,
        upperTextureId = textureMapContainer.indexOf(upperTextureName),
        middleTextureId = textureMapContainer.indexOf(middleTextureName),
        lowerTextureId = textureMapContainer.indexOf(lowerTextureName),
        sectorId  = sectorId.toInt(),
        sector = sectors[sectorId.toInt()],
    )
}