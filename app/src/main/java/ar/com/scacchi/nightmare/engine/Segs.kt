package ar.com.scacchi.nightmare.engine

class Segs(
    val content: Array<Seg>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySegs(): Segs = Segs(emptyArray())
    }
}

fun SegsLump.toSegs(vertexes: Vertexes, lineDefs: LineDefs): Segs = Segs(
    content = Array(content.size) { idx ->
        content[idx].toSeg(idx, vertexes, lineDefs)
    }
)