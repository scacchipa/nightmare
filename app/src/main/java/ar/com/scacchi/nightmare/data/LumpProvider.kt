package ar.com.scacchi.nightmare.data

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.LUMP_INDICES
import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.LumpDirectory
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Sectors
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SideDefs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.engine.Things
import ar.com.scacchi.nightmare.engine.Vertexes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import javax.inject.Inject

class LumpProvider @Inject constructor(
    @param:ApplicationContext val context: Context
) {
    val wadPath = R.raw.doom
    val buffer: ByteBuffer = ByteBuffer.wrap(context.resources.openRawResource(wadPath).readBytes())
    val wadHeader: WadHeader = WadHeader.createFrom(buffer)
    val lumpDirectory: LumpDirectory = LumpDirectory.createFrom(buffer, wadHeader)


    var episodeIdx: Int? = null
    var episodeName: String = ""
        set(name) {
            field = name
            episodeIdx = lumpDirectory.getIdxForName(name)
        }

    fun fetchVertexes(): Vertexes {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return Vertexes.emptyVertexes()
        val lumpIdx = LUMP_INDICES["VERTEXES"] ?: return Vertexes.emptyVertexes()

        val vertexesLump = lumpDirectory[episodeIdx + lumpIdx]
        return  Vertexes.createFrom(buffer.duplicate(), vertexesLump)
    }

    fun fetchLineDefs(sideDefs: SideDefs): LineDefs {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return LineDefs.emptyLineDefs()
        val lineDefsIdx = LUMP_INDICES["LINEDEFS"] ?: return LineDefs.emptyLineDefs()

        val lineDefsLump = lumpDirectory[episodeIdx + lineDefsIdx]
        return  LineDefs.createFrom(buffer.duplicate(), lineDefsLump, sideDefs)
    }

    fun fetchNodesLump(): Nodes {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return Nodes.emptyNodes()
        val nodesIdx = LUMP_INDICES["NODES"] ?: return Nodes.emptyNodes()

        val nodesLump = lumpDirectory[episodeIdx + nodesIdx]
        return  Nodes.createFrom(buffer.duplicate(), nodesLump)
    }

    fun fetchSegs(vertexes: Vertexes, lineDefs: LineDefs, sectors: Sectors): Segs {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return Segs.emptySegs()
        val segsIdx = LUMP_INDICES["SEGS"] ?: return Segs.emptySegs()

        val segsLump = lumpDirectory[episodeIdx + segsIdx]
        return Segs.createFrom(buffer.duplicate(), segsLump, vertexes, lineDefs, sectors)
    }

    fun fetchSubSectors(): SubSectors {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SubSectors.emptySubSectors()
        val subSectorsIdx = LUMP_INDICES["SSECTORS"] ?: return SubSectors.emptySubSectors()

        val subSectorsLump = lumpDirectory[episodeIdx + subSectorsIdx]
        return SubSectors.createFrom(buffer.duplicate(), subSectorsLump)
    }

    fun fetchSectors(): Sectors {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return Sectors.emptySectors()
        val sectorsIdx = LUMP_INDICES["SECTORS"] ?: return Sectors.emptySectors()

        val sectorsLump = lumpDirectory[episodeIdx + sectorsIdx]
        return Sectors.createFrom(buffer.duplicate(), sectorsLump)
    }

    fun fetchSideDef(sectors: Sectors): SideDefs {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SideDefs.emptySideDefs()
        val sideDefsIdx = LUMP_INDICES["SIDEDEFS"] ?: return SideDefs.emptySideDefs()

        val sideDefLump = lumpDirectory[episodeIdx + sideDefsIdx]
        return SideDefs.createFrom(buffer.duplicate(), sideDefLump, sectors)
    }

    fun fetchThings(): Things {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return Things.emptyThings()
        val thingsIdx =LUMP_INDICES["THINGS"] ?: return Things.emptyThings()

        val thingsLump = lumpDirectory[episodeIdx + thingsIdx]
        return Things.createFrom(buffer.duplicate(), thingsLump)
    }
}