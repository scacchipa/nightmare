package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.ext.asString

class Sector(
    val floorHeight: Int,
    val ceilingHeight: Int,
    val floorTextureName: String,
    val ceilingTextureName: String,
    val lightLevel: Float,
    val type: Int,
    val tag: Int,
)

fun SectorLump.toSector(): Sector = Sector(
    floorHeight = floorHeight.toInt(),
    ceilingHeight = ceilingHeight.toInt(),
    floorTextureName = floorTextureName.asString(),
    ceilingTextureName = ceilingTextureName.asString(),
    lightLevel = lightLevel.toFloat() / 256f,
    type = type.toInt(),
    tag = tag.toInt(),
)
