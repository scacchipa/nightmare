package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.data.map.LineDefLump

class LineDef(
    val startVertexId: Int,
    val endVertexId: Int,
    val flags: UShort,
    val specialType: Int,
    val sectorTag: Int,
    val frontSideDefId: Int, // Doom2
    val backSideDefId: Int, // Doom2
    val frontSideDef: SideDef?,
    val backSideDef: SideDef?,
)

fun LineDefLump.toLineDef(sideDefs: SideDefs): LineDef = LineDef(
    startVertexId = startVertexId.toInt(),
    endVertexId = endVertexId.toInt(),
    flags = flags,
    specialType = specialType.toInt(),
    sectorTag = sectorTag.toInt(),
    frontSideDefId = frontSideDefId.toInt(),
    backSideDefId = backSideDefId.toInt(),
    frontSideDef =
        if (frontSideDefId != 0xFFFFu.toUShort()) sideDefs[frontSideDefId.toInt()]
        else null,
    backSideDef =
        if (backSideDefId != 0xFFFFu.toUShort()) sideDefs[backSideDefId.toInt()]
        else null,
)