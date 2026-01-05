package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.WadInfo
import java.nio.ByteBuffer

class WadData(
    val buffer: ByteBuffer,
    val wadInfo: WadInfo,
    val lumpDirectory: LumpDirectory,
    val vertexes: Vertexes,
    val lineDefs: LineDefs,
    val nodes: Nodes,
    val subSectors: SubSectors,
    val segs: Segs,
    val things: Things,
    val player: Player
) {

    companion object {
        fun create(
            buffer: ByteBuffer,
            nameMap: String = "E1M1"
        ): WadData {

            val wadInfo = WadInfo.Companion.createFrom(buffer)
            val lumpDirectory = LumpDirectory.createFrom(buffer, wadInfo)
            val idx = lumpDirectory.getIdxForName(nameMap)
            val vertexesLump = lumpDirectory[idx + (LUMP_INDICES["VERTEXES"] ?: 0)]
            val lineDefsLump = lumpDirectory[idx + (LUMP_INDICES["LINEDEFS"] ?: 0)]
            val nodesLump = lumpDirectory[idx + (LUMP_INDICES["NODES"] ?: 0)]
            val subSectorsLump = lumpDirectory[idx + (LUMP_INDICES["SSECTORS"] ?: 0)]
            val segsLump = lumpDirectory[idx + (LUMP_INDICES["SEGS"] ?: 0)]
            val thingsLump = lumpDirectory[idx + (LUMP_INDICES["THINGS"] ?: 0)]
            val vertexes = Vertexes.createFrom(buffer, vertexesLump)
            val lineDefs = LineDefs.createFrom(buffer, lineDefsLump)
            val nodes = Nodes.createFrom(buffer, nodesLump)
            val subSectors = SubSectors.createFrom(buffer, subSectorsLump)
            val segs = Segs.createFrom(buffer, segsLump, vertexes, lineDefs)
            val thingsWithPlayer = Things.createFrom(buffer, thingsLump)

            return WadData(
                buffer = buffer,
                wadInfo = WadInfo.Companion.createFrom(buffer),
                lumpDirectory = lumpDirectory,
                vertexes = vertexes,
                lineDefs = lineDefs,
                nodes = nodes,
                subSectors = subSectors,
                segs = segs,
                things = Things.createWithoutPlayer(thingsWithPlayer),
                player = Player(thingsWithPlayer[0])
            )
        }
    }
}

