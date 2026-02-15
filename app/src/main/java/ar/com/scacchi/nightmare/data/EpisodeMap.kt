package ar.com.scacchi.nightmare.data

import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Sectors
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.engine.Vertexes

data class EpisodeMap(
    val vertexes: Vertexes,
    val lineDefs: LineDefs,
    val nodes: Nodes,
    val subSectors: SubSectors,
    val segs: Segs,
    val sectors: Sectors,
)