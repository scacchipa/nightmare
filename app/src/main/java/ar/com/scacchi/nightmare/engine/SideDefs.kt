package ar.com.scacchi.nightmare.engine

class SideDefs(
    val content: Array<SideDef>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySideDefs(): SideDefs = SideDefs(emptyArray())
    }
}

fun SideDefsLump.toSideDefs(sectors: Sectors): SideDefs = SideDefs(
    content = Array(this.content.size) { idx ->
        this.content[idx].toSideDef(sectors)
    }
)