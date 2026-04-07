package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.asString
import ar.com.scacchi.nightmare.source.wad.TextureMapContainer
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SideDefLump

class SideDef(
    val offset: Offset,
    val upperTextureName: String,
    val middleTextureName: String,
    val lowerTextureName: String,
    val lowerTextureIdx: Int,
    val middleTextureIdx: Int,
    val upperTextureIdx: Int,
    val sectorId: Int,
    val sector: Sector,
)

fun SideDefLump.toSideDef(sectors: Sectors, textureMapContainer: TextureMapContainer): SideDef {
    val upperTextureName = upperTextureName.asString()
    val middleTextureName = middleTextureName.asString()
    val lowerTextureName = lowerTextureName.asString()

    return SideDef(
        offset = Offset(xOffset.toFloat(), yOffset.toFloat()),
        upperTextureName = upperTextureName,
        middleTextureName = middleTextureName,
        lowerTextureName = lowerTextureName,
        upperTextureIdx = textureMapContainer.indexOf(upperTextureName),
        middleTextureIdx = textureMapContainer.indexOf(middleTextureName),
        lowerTextureIdx = textureMapContainer.indexOf(lowerTextureName),
        sectorId  = sectorId.toInt(),
        sector = sectors[sectorId.toInt()],
    )
}