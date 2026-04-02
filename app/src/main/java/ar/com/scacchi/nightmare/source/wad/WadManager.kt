package ar.com.scacchi.nightmare.source.wad

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.data.EpisodeMap
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Sectors
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SideDefs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.engine.Things
import ar.com.scacchi.nightmare.engine.Vertexes
import ar.com.scacchi.nightmare.engine.toLineDefs
import ar.com.scacchi.nightmare.engine.toNodes
import ar.com.scacchi.nightmare.engine.toSectors
import ar.com.scacchi.nightmare.engine.toSegs
import ar.com.scacchi.nightmare.engine.toSideDefs
import ar.com.scacchi.nightmare.engine.toSubSectors
import ar.com.scacchi.nightmare.engine.toThings
import ar.com.scacchi.nightmare.engine.toVertexes
import ar.com.scacchi.nightmare.source.wad.lump.LumpDirectory
import ar.com.scacchi.nightmare.source.wad.lump.LumpProvider
import ar.com.scacchi.nightmare.source.wad.lump.data.FlatLump
import ar.com.scacchi.nightmare.source.wad.lump.data.patch.PatchLump
import ar.com.scacchi.nightmare.source.wad.lump.data.patch.PictureLump
import ar.com.scacchi.nightmare.source.wad.lump.data.patch.SpriteLump
import ar.com.scacchi.nightmare.source.wad.lump.data.texture.TextureMap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WadManager @Inject constructor(
    @param:ApplicationContext val context: Context,
    private val lumpProvider: LumpProvider,
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
    val pNameLump by lazy { lumpProvider.fetchPNameLump() }
    val textureMapList by lazy {
        lumpProvider.fetchTextureLump(1).mapTextureList +
                lumpProvider.fetchTextureLump(2).mapTextureList
    }

    private val episodeMapMap: ConcurrentHashMap<String, EpisodeMap> = ConcurrentHashMap()
    private val patchMap: ConcurrentHashMap<String, PatchLump> = ConcurrentHashMap()
    private val flatMap: ConcurrentHashMap<String, FlatLump> = ConcurrentHashMap()
    private val spriteMap: ConcurrentHashMap<String, SpriteLump> = ConcurrentHashMap()
    private val pictureMap: ConcurrentHashMap<String, PictureLump> = ConcurrentHashMap()


    fun getEpisodeMap(name: String): EpisodeMap =
        episodeMapMap.computeIfAbsent(name) {

            println("Creating Episode: $name")

            lumpProvider.episodeName = name
            val sectors: Sectors = lumpProvider.fetchSectorsLump().toSectors()
            val sideDefs: SideDefs = lumpProvider.fetchSideDefLump().toSideDefs(sectors)
            val vertexes: Vertexes = lumpProvider.fetchVertexesLump().toVertexes()
            val lineDefs: LineDefs = lumpProvider.fetchLineDefsLump().toLineDefs(sideDefs)
            val nodes: Nodes = lumpProvider.fetchNodesLump().toNodes()
            val subSectors: SubSectors = lumpProvider.fetchSubSectorsLump().toSubSectors()
            val segs: Segs = lumpProvider.fetchSegsLump().toSegs(vertexes, lineDefs)
            val thingsWithPlayer: Things = lumpProvider.fetchThingsLump().toThings()
            val rootNodeId: Int = nodes.count() - 1

            return@computeIfAbsent EpisodeMap(
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

    fun getInitialPlayer(episodeName: String): Player =
        getEpisodeMap(episodeName).things[0].toPlayer()

    fun getPatch(name: String): PatchLump = patchMap.computeIfAbsent(name) {
        lumpProvider.fetchPatch(name)
    }

    fun getFlat(name: String): FlatLump = flatMap.computeIfAbsent(name) {
        lumpProvider.fetchFlat(name)
    }

    fun getSprite(name: String): SpriteLump = spriteMap.computeIfAbsent(name) {
        lumpProvider.fetchPatch(name)
    }

    fun getPicture(name: String): PictureLump = pictureMap.computeIfAbsent(name) {
        lumpProvider.fetchPatch(name)
    }

    fun getTextureMap(textureMapId: Int): TextureMap = textureMapList[textureMapId]


    fun readPatchNameList(): List<String> = this.lumpDirectory.patchListName
    fun readFlatNameList(): List<String> = this.lumpDirectory.flatListName
    fun readSpriteNameList(): List<String> = this.lumpDirectory.spriteListName
}