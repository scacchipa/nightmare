package ar.com.scacchi.nightmare.engine

class SubSectors(
    val content: Array<SubSector>,
) {
    operator fun get(idx: Int) = content[idx]
    fun count(): Int = content.size

    companion object {
        fun emptySubSectors(): SubSectors = SubSectors(emptyArray())
    }
}

fun SubSectorsLump.toSubSectors(): SubSectors = SubSectors(
    content = Array(content.size) { idx ->
        content[idx].toSubSector()
    }
)