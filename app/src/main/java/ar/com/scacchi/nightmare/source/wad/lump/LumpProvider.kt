package ar.com.scacchi.nightmare.source.wad.lump

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.source.wad.lump.NodesLump
import ar.com.scacchi.nightmare.source.wad.lump.SectorsLump
import ar.com.scacchi.nightmare.source.wad.lump.SegsLump
import ar.com.scacchi.nightmare.source.wad.lump.SideDefsLump
import ar.com.scacchi.nightmare.source.wad.lump.SubSectorsLump
import ar.com.scacchi.nightmare.source.wad.lump.ThingsLump
import ar.com.scacchi.nightmare.source.wad.lump.VertexesLump
import ar.com.scacchi.nightmare.source.wad.WadHeader
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import javax.inject.Inject

class LumpProvider @Inject constructor(
    @param:ApplicationContext val context: Context
) {
    val wadPath = R.raw.doom
    val buffer: ByteBuffer = ByteBuffer.wrap(context.resources.openRawResource(wadPath).readBytes())
    val wadHeader: WadHeader = WadHeader.Companion.createFrom(buffer)
    val lumpDirectory: LumpDirectory = LumpDirectory.createFrom(buffer, wadHeader)


    var episodeIdx: Int? = null
    var episodeName: String = ""
        set(name) {
            field = name
            episodeIdx = lumpDirectory.getIdxForName(name)
        }

    fun fetchVertexesLump(): VertexesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return VertexesLump.Companion.emptyVertexes()
        val lumpIdx = LUMP_INDICES["VERTEXES"] ?: return VertexesLump.Companion.emptyVertexes()

        val vertexesLump = lumpDirectory[episodeIdx + lumpIdx]
        return  VertexesLump.Companion.createFrom(buffer.duplicate(), vertexesLump)
    }

    fun fetchLineDefsLump(): LineDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return LineDefsLump.emptyLineDefs()
        val lineDefsIdx = LUMP_INDICES["LINEDEFS"] ?: return LineDefsLump.emptyLineDefs()

        val lineDefsLump = lumpDirectory[episodeIdx + lineDefsIdx]
        return  LineDefsLump.createFrom(buffer.duplicate(), lineDefsLump)
    }

    fun fetchNodesLump(): NodesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return NodesLump.Companion.emptyNodes()
        val nodesIdx = LUMP_INDICES["NODES"] ?: return NodesLump.Companion.emptyNodes()

        val nodesLump = lumpDirectory[episodeIdx + nodesIdx]
        return  NodesLump.Companion.createFrom(buffer.duplicate(), nodesLump)
    }

    fun fetchSegsLump(): SegsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SegsLump.Companion.emptySegs()
        val segsIdx = LUMP_INDICES["SEGS"] ?: return SegsLump.Companion.emptySegs()

        val segsLump = lumpDirectory[episodeIdx + segsIdx]
        return SegsLump.Companion.createFrom(buffer.duplicate(), segsLump)
    }

    fun fetchSubSectorsLump(): SubSectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SubSectorsLump.Companion.emptySubSectors()
        val subSectorsIdx = LUMP_INDICES["SSECTORS"] ?: return SubSectorsLump.Companion.emptySubSectors()

        val subSectorsLump = lumpDirectory[episodeIdx + subSectorsIdx]
        return SubSectorsLump.Companion.createFrom(buffer.duplicate(), subSectorsLump)
    }

    fun fetchSectorsLump(): SectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SectorsLump.Companion.emptySectors()
        val sectorsIdx = LUMP_INDICES["SECTORS"] ?: return SectorsLump.Companion.emptySectors()

        val sectorsLump = lumpDirectory[episodeIdx + sectorsIdx]
        return SectorsLump.Companion.createFrom(buffer.duplicate(), sectorsLump)
    }

    fun fetchSideDefLump(): SideDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SideDefsLump.Companion.emptySideDefs()
        val sideDefsIdx = LUMP_INDICES["SIDEDEFS"] ?: return SideDefsLump.Companion.emptySideDefs()

        val sideDefLump = lumpDirectory[episodeIdx + sideDefsIdx]
        return SideDefsLump.Companion.createFrom(buffer.duplicate(), sideDefLump)
    }

    fun fetchThingsLump(): ThingsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return ThingsLump.Companion.emptyThings()
        val thingsIdx = LUMP_INDICES["THINGS"] ?: return ThingsLump.Companion.emptyThings()

        val thingsLump = lumpDirectory[episodeIdx + thingsIdx]
        return ThingsLump.Companion.createFrom(buffer.duplicate(), thingsLump)
    }
}