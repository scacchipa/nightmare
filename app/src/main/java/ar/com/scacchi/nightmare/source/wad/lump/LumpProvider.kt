package ar.com.scacchi.nightmare.source.wad.lump

import android.content.Context
import ar.com.scacchi.nightmare.R
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

        val vertexesLump = lumpDirectory[episodeIdx + Lump.vertexesIndex]
        return  VertexesLump.Companion.createFrom(buffer.duplicate(), vertexesLump)
    }

    fun fetchLineDefsLump(): LineDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return LineDefsLump.emptyLineDefs()

        val lineDefsLump = lumpDirectory[episodeIdx + Lump.lineDefsIndex]
        return  LineDefsLump.createFrom(buffer.duplicate(), lineDefsLump)
    }

    fun fetchNodesLump(): NodesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return NodesLump.Companion.emptyNodes()

        val nodesLump = lumpDirectory[episodeIdx + Lump.nodesIndex]
        return  NodesLump.Companion.createFrom(buffer.duplicate(), nodesLump)
    }

    fun fetchSegsLump(): SegsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SegsLump.Companion.emptySegs()

        val segsLump = lumpDirectory[episodeIdx + Lump.segsIndex]
        return SegsLump.Companion.createFrom(buffer.duplicate(), segsLump)
    }

    fun fetchSubSectorsLump(): SubSectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SubSectorsLump.Companion.emptySubSectors()

        val subSectorsLump = lumpDirectory[episodeIdx + Lump.subsectorsIndex]
        return SubSectorsLump.Companion.createFrom(buffer.duplicate(), subSectorsLump)
    }

    fun fetchSectorsLump(): SectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SectorsLump.Companion.emptySectors()

        val sectorsLump = lumpDirectory[episodeIdx + Lump.sectorsIndex]
        return SectorsLump.Companion.createFrom(buffer.duplicate(), sectorsLump)
    }

    fun fetchSideDefLump(): SideDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SideDefsLump.Companion.emptySideDefs()

        val sideDefLump = lumpDirectory[episodeIdx + Lump.sideDefsIndex]
        return SideDefsLump.Companion.createFrom(buffer.duplicate(), sideDefLump)
    }

    fun fetchThingsLump(): ThingsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return ThingsLump.Companion.emptyThings()

        val thingsLump = lumpDirectory[episodeIdx + Lump.thingsIndex]
        return ThingsLump.Companion.createFrom(buffer.duplicate(), thingsLump)
    }
}