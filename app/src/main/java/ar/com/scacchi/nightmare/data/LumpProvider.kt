package ar.com.scacchi.nightmare.data

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.LUMP_INDICES
import ar.com.scacchi.nightmare.engine.LineDefsLump
import ar.com.scacchi.nightmare.engine.LumpDirectory
import ar.com.scacchi.nightmare.engine.NodesLump
import ar.com.scacchi.nightmare.engine.SectorsLump
import ar.com.scacchi.nightmare.engine.SegsLump
import ar.com.scacchi.nightmare.engine.SideDefsLump
import ar.com.scacchi.nightmare.engine.SubSectorsLump
import ar.com.scacchi.nightmare.engine.ThingsLump
import ar.com.scacchi.nightmare.engine.VertexesLump
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

    fun fetchVertexesLump(): VertexesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return VertexesLump.emptyVertexes()
        val lumpIdx = LUMP_INDICES["VERTEXES"] ?: return VertexesLump.emptyVertexes()

        val vertexesLump = lumpDirectory[episodeIdx + lumpIdx]
        return  VertexesLump.createFrom(buffer.duplicate(), vertexesLump)
    }

    fun fetchLineDefsLump(): LineDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return LineDefsLump.emptyLineDefs()
        val lineDefsIdx = LUMP_INDICES["LINEDEFS"] ?: return LineDefsLump.emptyLineDefs()

        val lineDefsLump = lumpDirectory[episodeIdx + lineDefsIdx]
        return  LineDefsLump.createFrom(buffer.duplicate(), lineDefsLump)
    }

    fun fetchNodesLump(): NodesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return NodesLump.emptyNodes()
        val nodesIdx = LUMP_INDICES["NODES"] ?: return NodesLump.emptyNodes()

        val nodesLump = lumpDirectory[episodeIdx + nodesIdx]
        return  NodesLump.createFrom(buffer.duplicate(), nodesLump)
    }

    fun fetchSegsLump(): SegsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SegsLump.emptySegs()
        val segsIdx = LUMP_INDICES["SEGS"] ?: return SegsLump.emptySegs()

        val segsLump = lumpDirectory[episodeIdx + segsIdx]
        return SegsLump.createFrom(buffer.duplicate(), segsLump)
    }

    fun fetchSubSectorsLump(): SubSectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SubSectorsLump.emptySubSectors()
        val subSectorsIdx = LUMP_INDICES["SSECTORS"] ?: return SubSectorsLump.emptySubSectors()

        val subSectorsLump = lumpDirectory[episodeIdx + subSectorsIdx]
        return SubSectorsLump.createFrom(buffer.duplicate(), subSectorsLump)
    }

    fun fetchSectorsLump(): SectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SectorsLump.emptySectors()
        val sectorsIdx = LUMP_INDICES["SECTORS"] ?: return SectorsLump.emptySectors()

        val sectorsLump = lumpDirectory[episodeIdx + sectorsIdx]
        return SectorsLump.createFrom(buffer.duplicate(), sectorsLump)
    }

    fun fetchSideDefLump(): SideDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SideDefsLump.emptySideDefs()
        val sideDefsIdx = LUMP_INDICES["SIDEDEFS"] ?: return SideDefsLump.emptySideDefs()

        val sideDefLump = lumpDirectory[episodeIdx + sideDefsIdx]
        return SideDefsLump.createFrom(buffer.duplicate(), sideDefLump)
    }

    fun fetchThingsLump(): ThingsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return ThingsLump.emptyThings()
        val thingsIdx =LUMP_INDICES["THINGS"] ?: return ThingsLump.emptyThings()

        val thingsLump = lumpDirectory[episodeIdx + thingsIdx]
        return ThingsLump.createFrom(buffer.duplicate(), thingsLump)
    }
}