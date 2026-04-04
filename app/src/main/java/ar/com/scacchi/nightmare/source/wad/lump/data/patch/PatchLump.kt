package ar.com.scacchi.nightmare.source.wad.lump.data.patch

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

typealias SpriteLump = PatchLump
typealias PictureLump = PatchLump

class PatchLump(
    val header: PatchHeader,
    val posts: Posts,
) {
    val width get() = header.width
    val height get() = header.height

    val bitmap by lazy { buildDoomImage() }

    operator fun get(x: Int, y: Int): UByte? = bitmap[x, y]
    operator fun set(x: Int, y: Int, value: UByte) = bitmap.set(x, y, value)

    companion object {
        fun emptyPicture(): PictureLump = PictureLump(PatchHeader.emptyHeader(),
            Posts(emptyArray())
        )

        fun createFrom(buffer: ByteBuffer, fileLump: FileLump): PatchLump {
            buffer.position(fileLump.filePos)

            val header = PatchHeader.createFrom(buffer)
            val posts = Posts.createFrom(buffer, header.width.toInt())

            return PictureLump(header, posts)
        }
    }

    fun buildDoomImage(): DoomBitmap =
        DoomBitmap(header.width.toInt(), header.height.toInt()).also { bitmap ->
            var ix = 0
            var iy: Int
            for (post in posts.content) {
                if (post.topDelta == 0xFF.toUByte()) {
                    ix += 1
                    continue
                }

                iy = post.topDelta.toInt()
                for (postY in 0 until post.length.toInt()) {
                    bitmap[ix, iy] = post.data[postY]
                    iy += 1
                }
            }
        }
}