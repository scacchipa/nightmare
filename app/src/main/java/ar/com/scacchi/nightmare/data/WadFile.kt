package ar.com.scacchi.nightmare.data

import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.engine.LUMP_INDICES
import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.LumpDirectory
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Sectors
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SideDefs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.engine.Things
import ar.com.scacchi.nightmare.engine.Vertexes
import java.nio.ByteBuffer

data class WadFile(
    val wadHeader: WadHeader,
    val lumpDirectory: LumpDirectory,
    val playPal: PlayPal,
    val colorMap: ColorMap,
    val episodeMap: EpisodeMap,
    val things: Things,
    val player: Player,
    val rootNodeId: Int
) {
    companion object {
        fun create(
            buffer: ByteBuffer,
            nameMap: String = "E1M1"
        ): WadFile {

            val wadHeader = WadHeader.createFrom(buffer)
            val lumpDirectory = LumpDirectory.createFrom(buffer, wadHeader)
            val idx = lumpDirectory.getIdxForName(nameMap)
            val vertexesLump = lumpDirectory[idx + (LUMP_INDICES["VERTEXES"] ?: 0)]
            val lineDefsLump = lumpDirectory[idx + (LUMP_INDICES["LINEDEFS"] ?: 0)]
            val nodesLump = lumpDirectory[idx + (LUMP_INDICES["NODES"] ?: 0)]
            val subSectorsLump = lumpDirectory[idx + (LUMP_INDICES["SSECTORS"] ?: 0)]
            val segsLump = lumpDirectory[idx + (LUMP_INDICES["SEGS"] ?: 0)]
            val thingsLump = lumpDirectory[idx + (LUMP_INDICES["THINGS"] ?: 0)]
            val sectorsLump = lumpDirectory[idx + (LUMP_INDICES["SECTORS"] ?: 0)]
            val sideDefLump = lumpDirectory[idx + (LUMP_INDICES["SIDEDEFS"] ?: 0)]
            val playPalLump = lumpDirectory[lumpDirectory.getIdxForName("PLAYPAL")]
            val colorMapLump = lumpDirectory[lumpDirectory.getIdxForName("COLORMAP")]

            val playPal = PlayPal.createFromLump(playPalLump, buffer)
            val colorMap = ColorMap.createFromLump(colorMapLump, buffer)
            val sectors = Sectors.createFrom(buffer, sectorsLump)
            val sideDefs = SideDefs.createFrom(buffer, sideDefLump, sectors)
            val vertexes = Vertexes.createFrom(buffer, vertexesLump)
            val lineDefs = LineDefs.createFrom(buffer, lineDefsLump, sideDefs)
            val nodes = Nodes.createFrom(buffer, nodesLump)
            val subSectors = SubSectors.createFrom(buffer, subSectorsLump)
            val segs = Segs.createFrom(buffer, segsLump, vertexes, lineDefs, sectors)
            val thingsWithPlayer = Things.createFrom(buffer, thingsLump)
            val rootNodeId: Int = nodes.count() - 1

            return WadFile(
                wadHeader = WadHeader.createFrom(buffer),
                lumpDirectory = lumpDirectory,
                playPal = playPal,
                colorMap = colorMap,
                episodeMap = EpisodeMap(
                    vertexes = vertexes,
                    lineDefs = lineDefs,
                    nodes = nodes,
                    subSectors = subSectors,
                    segs = segs,
                    sectors = sectors,
                ),
                things = Things.createWithoutPlayer(thingsWithPlayer),
                player = Player(thingsWithPlayer[0]),
                rootNodeId = rootNodeId,
            )
        }
    }
}