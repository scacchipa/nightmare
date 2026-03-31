package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.source.wad.lump.NodesLump

class Nodes(
    val content: Array<Node>
) {
    operator fun get(idx: Int) = content[idx]
    fun count() = content.size
    fun isEmpty() = content.isEmpty()

    companion object {
        fun emptyNodes(): Nodes = Nodes(emptyArray())
    }
}

fun NodesLump.toNodes(): Nodes = Nodes(
    content = Array(content.size) {
        content[it].toNode()
    }
)