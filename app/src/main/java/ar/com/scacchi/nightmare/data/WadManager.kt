package ar.com.scacchi.nightmare.data

import ar.com.scacchi.nightmare.data.asset.Flat
import ar.com.scacchi.nightmare.data.asset.Patch
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
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
import java.nio.ByteBuffer

data class WadManager(
    val buffer: ByteBuffer,
    val wadHeader: WadHeader,
    val lumpDirectory: LumpDirectory,
    val playPal: PlayPal,
    val colorMap: ColorMap,
) {
    companion object {
        fun create(
            buffer: ByteBuffer,
        ): WadManager {
            val wadHeader = WadHeader.createFrom(buffer)
            val lumpDirectory = LumpDirectory.createFrom(buffer, wadHeader)
            val playPalLump = lumpDirectory[lumpDirectory.getIdxForName("PLAYPAL")]
            val colorMapLump = lumpDirectory[lumpDirectory.getIdxForName("COLORMAP")]

            val playPal = PlayPal.createFromLump(playPalLump, buffer)
            val colorMap = ColorMap.createFromLump(colorMapLump, buffer)

            return WadManager(
                buffer = buffer,
                wadHeader = WadHeader.createFrom(buffer),
                lumpDirectory = lumpDirectory,
                playPal = playPal,
                colorMap = colorMap,
            )
        }
    }

    fun getEpisodeMap(name: String): EpisodeMap {
        val idx = lumpDirectory.getIdxForName(name)

        val vertexesLump = lumpDirectory[idx + (LUMP_INDICES["VERTEXES"] ?: 0)]
        val lineDefsLump = lumpDirectory[idx + (LUMP_INDICES["LINEDEFS"] ?: 0)]
        val nodesLump = lumpDirectory[idx + (LUMP_INDICES["NODES"] ?: 0)]
        val subSectorsLump = lumpDirectory[idx + (LUMP_INDICES["SSECTORS"] ?: 0)]
        val segsLump = lumpDirectory[idx + (LUMP_INDICES["SEGS"] ?: 0)]
        val sectorsLump = lumpDirectory[idx + (LUMP_INDICES["SECTORS"] ?: 0)]
        val sideDefLump = lumpDirectory[idx + (LUMP_INDICES["SIDEDEFS"] ?: 0)]
        val thingsLump = lumpDirectory[idx + (LUMP_INDICES["THINGS"] ?: 0)]

        val sectors = Sectors.createFrom(buffer, sectorsLump)
        val sideDefs = SideDefs.createFrom(buffer, sideDefLump, sectors)
        val vertexes = Vertexes.createFrom(buffer, vertexesLump)
        val lineDefs = LineDefs.createFrom(buffer, lineDefsLump, sideDefs)
        val nodes = Nodes.createFrom(buffer, nodesLump)
        val subSectors = SubSectors.createFrom(buffer, subSectorsLump)
        val segs = Segs.createFrom(buffer, segsLump, vertexes, lineDefs, sectors)
        val thingsWithPlayer = Things.createFrom(buffer, thingsLump)
        val rootNodeId: Int = nodes.count() - 1

        return EpisodeMap(
            vertexes = vertexes,
            lineDefs = lineDefs,
            nodes = nodes,
            subSectors = subSectors,
            segs = segs,
            sectors = sectors,
            things = thingsWithPlayer,
            rootNodeId = rootNodeId,
        )
    }

    fun getPatch(name: String): Patch {
        return Patch.createFrom(buffer, lumpDirectory[name])
    }

    fun getFlat(name: String): Flat {
        return Flat.createFrom(buffer, lumpDirectory[name])
    }

    fun getPatchNameList(): List<String> = this.lumpDirectory.patchListName
    fun getFlatNameList(): List<String> = this.lumpDirectory.flatListName
    fun getSpriteNameList(): List<String> = this.lumpDirectory.spriteListName
}