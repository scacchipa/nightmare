package ar.com.scacchi.nightmare.data

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.data.asset.Flat
import ar.com.scacchi.nightmare.data.asset.patch.Patch
import ar.com.scacchi.nightmare.data.asset.patch.Picture
import ar.com.scacchi.nightmare.data.asset.patch.Sprite
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
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WadManager @Inject constructor(
    @param:ApplicationContext val context: Context
) {
    val wadPath = R.raw.doom
    val buffer: ByteBuffer = ByteBuffer.wrap(context.resources.openRawResource(wadPath).readBytes())
    val wadHeader: WadHeader = WadHeader.createFrom(buffer)
    val lumpDirectory: LumpDirectory = LumpDirectory.createFrom(buffer, wadHeader)
    val playPal: PlayPal = PlayPal.createFromLump(
        lumpDirectory[lumpDirectory.getIdxForName("PLAYPAL")], buffer
    )
    val colorMap: ColorMap = ColorMap.createFromLump(
        lumpDirectory[lumpDirectory.getIdxForName("COLORMAP")], buffer
    )

    private val episodeMapMap = mutableMapOf<String, EpisodeMap>()
    fun getEpisodeMap(name: String): EpisodeMap =
        episodeMapMap.getOrPut(name) {

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
    fun getInitialPlayer(episodeName: String): Player = Player(getEpisodeMap(episodeName).things[0])
    fun getPatch(name: String): Patch = Patch.createFrom(buffer, lumpDirectory[name])
    fun getFlat(name: String): Flat = Flat.createFrom(buffer, lumpDirectory[name])
    fun getSprite(name: String): Sprite = Sprite.createFrom(buffer, lumpDirectory[name])
    fun getPicture(name: String): Picture = Picture.createFrom(buffer, lumpDirectory[name])
    fun getGeneralPicture(name: String): Picture = Picture.createFrom(buffer, lumpDirectory[name])

    fun getPatchNameList(): List<String> = this.lumpDirectory.patchListName
    fun getFlatNameList(): List<String> = this.lumpDirectory.flatListName
    fun getSpriteNameList(): List<String> = this.lumpDirectory.spriteListName
}