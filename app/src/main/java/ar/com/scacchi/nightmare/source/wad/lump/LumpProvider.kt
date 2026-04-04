package ar.com.scacchi.nightmare.source.wad.lump

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.source.wad.WadHeader
import ar.com.scacchi.nightmare.source.wad.lump.data.FlatLump
import ar.com.scacchi.nightmare.source.wad.lump.data.PNamesLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.LineDefsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.NodesLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SectorsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SegsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SideDefsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.SubSectorsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.ThingsLump
import ar.com.scacchi.nightmare.source.wad.lump.data.map.VertexesLump
import ar.com.scacchi.nightmare.source.wad.lump.data.patch.PatchLump
import ar.com.scacchi.nightmare.source.wad.lump.data.texture.TextureLump
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

        val vertexesLump = lumpDirectory[episodeIdx + Lump.vertexesIndex]
        return  VertexesLump.createFrom(buffer.duplicate(), vertexesLump)
    }

    fun fetchLineDefsLump(): LineDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return LineDefsLump.emptyLineDefs()

        val lineDefsLump = lumpDirectory[episodeIdx + Lump.lineDefsIndex]
        return  LineDefsLump.createFrom(buffer.duplicate(), lineDefsLump)
    }

    fun fetchNodesLump(): NodesLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return NodesLump.emptyNodes()

        val nodesLump = lumpDirectory[episodeIdx + Lump.nodesIndex]
        return  NodesLump.createFrom(buffer.duplicate(), nodesLump)
    }

    fun fetchSegsLump(): SegsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SegsLump.emptySegs()

        val segsLump = lumpDirectory[episodeIdx + Lump.segsIndex]
        return SegsLump.createFrom(buffer.duplicate(), segsLump)
    }

    fun fetchSubSectorsLump(): SubSectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SubSectorsLump.emptySubSectors()

        val subSectorsLump = lumpDirectory[episodeIdx + Lump.subsectorsIndex]
        return SubSectorsLump.createFrom(buffer.duplicate(), subSectorsLump)
    }

    fun fetchSectorsLump(): SectorsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SectorsLump.emptySectors()

        val sectorsLump = lumpDirectory[episodeIdx + Lump.sectorsIndex]
        return SectorsLump.createFrom(buffer.duplicate(), sectorsLump)
    }

    fun fetchSideDefLump(): SideDefsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return SideDefsLump.emptySideDefs()

        val sideDefLump = lumpDirectory[episodeIdx + Lump.sideDefsIndex]
        return SideDefsLump.createFrom(buffer.duplicate(), sideDefLump)
    }

    fun fetchThingsLump(): ThingsLump {
        val episodeIdx = this@LumpProvider.episodeIdx ?: return ThingsLump.emptyThings()

        val thingsLump = lumpDirectory[episodeIdx + Lump.thingsIndex]
        return ThingsLump.createFrom(buffer.duplicate(), thingsLump)
    }

    fun fetchTextureLump(num: Int): TextureLump {
        val textureLump = lumpDirectory["TEXTURE$num"]
        return TextureLump.createFrom(buffer.duplicate(), textureLump)
    }

    fun fetchPNameLump(): PNamesLump {
        val pNameLump = lumpDirectory["PNAMES"]
        return PNamesLump.createFrom(buffer.duplicate(), pNameLump)
    }

    fun fetchPatch(name: String): PatchLump = PatchLump.createFrom(buffer.duplicate(), lumpDirectory[name])

    fun fetchFlat(name: String): FlatLump = FlatLump.createFrom(buffer.duplicate(), lumpDirectory[name])
}