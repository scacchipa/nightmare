package ar.com.scacchi.nightmare.engine

class Things(
    val content: Array<Thing>,
) {
    operator fun get(idx: Int) = content[idx]

    fun dropFirstThing() = Things(content.drop(1).toTypedArray())

    companion object {
        fun emptyThings(): Things = Things(emptyArray())
    }
}

fun ThingsLump.toThings(): Things = Things(
    content = Array(content.size) { idx ->
        content[idx].toThing()
    }
)

fun ThingsLump.toThingsWithoutPlayer(): Things = this.toThings().dropFirstThing()