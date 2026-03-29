package ar.com.scacchi.nightmare.data

import android.content.Context
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.data.asset.Flat
import ar.com.scacchi.nightmare.data.asset.patch.Patch
import ar.com.scacchi.nightmare.data.asset.patch.PatchHeader
import ar.com.scacchi.nightmare.data.asset.patch.Picture
import ar.com.scacchi.nightmare.data.asset.patch.Post
import ar.com.scacchi.nightmare.data.asset.patch.Sprite
import ar.com.scacchi.nightmare.data.asset.texture.PatchMap
import ar.com.scacchi.nightmare.data.asset.texture.Texture
import ar.com.scacchi.nightmare.data.asset.texture.TextureHeader
import ar.com.scacchi.nightmare.data.asset.texture.TextureMap
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
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
import ar.com.scacchi.nightmare.engine.toLineDefs
import ar.com.scacchi.nightmare.engine.toNodes
import ar.com.scacchi.nightmare.engine.toSectors
import ar.com.scacchi.nightmare.engine.toSegs
import ar.com.scacchi.nightmare.engine.toSideDefs
import ar.com.scacchi.nightmare.engine.toSubSectors
import ar.com.scacchi.nightmare.engine.toThings
import ar.com.scacchi.nightmare.engine.toVertexes
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
    val pName: PName = readPName()

    private val episodeMapMap: ConcurrentHashMap<String, EpisodeMap> = ConcurrentHashMap()
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

    fun readPatch(name: String): Patch {
        buffer.position(lumpDirectory[name].filePos)

        val header = readPatchHeader()
        val posts = readPosts(header.width.toInt())

        return Picture(header, posts)
    }

    private fun readPatchHeader(): PatchHeader {
        val width = buffer.readLittleEndianUShort()
        val height = buffer.readLittleEndianUShort()
        val leftOffset = buffer.readLittleEndianShort()
        val topOffset = buffer.readLittleEndianShort()

        val columnOffset = Array(width.toInt()) {
            buffer.readLittleEndianUInt()
        }
        return PatchHeader(
            width = width,
            height = height,
            leftOffset = leftOffset,
            topOffset = topOffset,
            columnOffset = columnOffset
        )
    }

    private fun readPosts(width: Int): Array<Post> {
        val posts = ArrayList<Post>()

        repeat(width) {
            var post = Post(0u, 0u, 0, arrayOf(), 0)
            while (post.topDelta != 0xFF.toUByte()) {
                post = readPost()
                posts.add(post)
            }
        }
        return posts.toTypedArray()
    }

    private fun readPost(): Post {

        val topDelta = buffer.readUByte()
        if (topDelta != 0xFF.toUByte()) {
            val length = buffer.readUByte()
            val paddingPre = buffer.readByte()  // unused
            val data = Array(length.toInt()) { buffer.readUByte() }
            val paddingPost = buffer.readByte()  // unused

            return Post(
                topDelta = topDelta,
                length = length,
                paddingPre = paddingPre,
                data = data,
                paddingPost = paddingPost
            )
        }
        return Post(
            topDelta = topDelta,
            length = 0u,
            paddingPre = 0,
            data = arrayOf(),
            paddingPost = 0,
        )
    }

    fun readFlat(name: String): Flat {
        buffer.position(lumpDirectory[name].filePos)

        return Flat(
            content = Array(64) {
                Array(64) {
                    buffer.readUByte()
                }
            },
        )
    }
    fun readSprite(name: String): Sprite = readPatch(name)
    fun readPicture(name: String): Picture = readPatch(name)
    fun readGeneralPicture(name: String): Picture = readPatch(name)

    fun readPName(): PName {
        buffer.position(lumpDirectory["PNAMES"].filePos)

        val numMapPatches = buffer.readLittleEndianInt()
        val pName = Array(numMapPatches) {
            buffer.readByteArrayAsString(8)
        }

        return PName(
            numMapPatches = buffer.readLittleEndianInt(),
            pName = pName,
        )
    }

    fun getTextureList(): List<TextureMap> =
        readTexture(1).mapTextureList + readTexture(1).mapTextureList

    fun readTexture(num: Int): Texture {

        val offset = lumpDirectory["TEXTURE$num"].filePos

        buffer.position(offset)

        val header = readTextureHeader()

        val mapTextureMap = header.textureDataOffset.map { mapOffset ->
            readTextureMap(offset + mapOffset.toInt())
        }

        return Texture(
            header = header,
            mapTextureList = mapTextureMap
        )
    }

    private fun readTextureHeader(): TextureHeader {
        val textureCount = buffer.readLittleEndianUInt()
        val textureDataOffset = Array(textureCount.toInt()) {
            buffer.readLittleEndianUInt()
        }

        return TextureHeader(
            textureCount = textureCount,
            textureDataOffset = textureDataOffset
        )
    }

    private fun readTextureMap(offset: Int): TextureMap {
        buffer.position(offset)

        val name = buffer.readByteArrayAsString(8)
        val flags = buffer.readLittleEndianInt()
        val width = buffer.readLittleEndianUShort()
        val height = buffer.readLittleEndianUShort()
        val columnDir = buffer.readLittleEndianInt()
        val patchCount = buffer.readLittleEndianUShort()
        val patchMapList = Array(patchCount.toInt()) {
            readPatchMap()
        }

        return TextureMap(
            name = name,
            flags = flags,
            width = width,
            height = height,
            columnDir = columnDir,
            patchCount = patchCount,
            patchMapList = patchMapList,
        )
    }

    private fun readPatchMap(): PatchMap {
        return PatchMap(
            xOffset = buffer.readLittleEndianShort(),
            yOffset = buffer.readLittleEndianShort(),
            pNameIndex = buffer.readLittleEndianUShort(),
            stepDir = buffer.readLittleEndianUShort(),
            colorMap = buffer.readLittleEndianUShort(),
        )
    }

    fun readPatchNameList(): List<String> = this.lumpDirectory.patchListName
    fun readFlatNameList(): List<String> = this.lumpDirectory.flatListName
    fun readSpriteNameList(): List<String> = this.lumpDirectory.spriteListName
}