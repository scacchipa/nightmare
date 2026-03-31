package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.LineDefsLump

class LineDefs(
    val content: Array<LineDef>
) {
    operator fun get(idx: Int): LineDef = content[idx]

    companion object {
        fun emptyLineDefs(): LineDefs = LineDefs(emptyArray())
    }
}

fun LineDefsLump.toLineDefs(sideDefs: SideDefs): LineDefs = LineDefs(
    content = Array(content.size) { idx ->
        content[idx].toLineDef(sideDefs)
    }
)