package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.EpisodeMap
import ar.com.scacchi.nightmare.data.WadHeader
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import java.nio.ByteBuffer

data class GameState(
    val playPal: PlayPal,
    val colorMap: ColorMap,
    val episodeMap: EpisodeMap,
    val player: Player,
) {
    companion object {
        fun create(
            buffer: ByteBuffer,
            nameMap: String = "E1M1"
        ): GameState {
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

            return GameState(
                playPal = playPal,
                colorMap = colorMap,
                episodeMap = EpisodeMap(
                    vertexes = vertexes,
                    lineDefs = lineDefs,
                    nodes = nodes,
                    subSectors = subSectors,
                    segs = segs,
                    sectors = sectors,
                    things = thingsWithPlayer,
                    rootNodeId = rootNodeId,
                ),
                player = Player(thingsWithPlayer[0]),
            )
        }

        fun getEmpty(): GameState = GameState(
            playPal = PlayPal(emptyArray()),
            colorMap = ColorMap(emptyArray()),
            EpisodeMap(
                vertexes = Vertexes(emptyArray()),
                lineDefs = LineDefs(emptyArray()),
                nodes = Nodes(emptyArray()),
                subSectors = SubSectors(emptyArray()),
                segs = Segs(emptyArray()),
                sectors = Sectors(emptyArray()),
                things = Things(emptyArray()),
                rootNodeId = 0,
            ),
            player = Player(0f, 0f, 0f, 0u, 0u, 0f),
        )
    }
}

