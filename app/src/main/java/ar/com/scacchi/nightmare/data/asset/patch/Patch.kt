package ar.com.scacchi.nightmare.data.asset.patch

import ar.com.scacchi.nightmare.data.asset.DoomBitmap

typealias Sprite = Patch
typealias Picture = Patch

data class Patch(
    val header: PatchHeader,
    val posts: Array<Post>,
) {
    val width get() = header.width
    val height get() = header.height

    val bitmap by lazy { buildDoomImage() }

    operator fun get(x: Int, y: Int): UByte? = bitmap[x, y]
    operator fun set(x: Int, y: Int, value: UByte) = bitmap.set(x, y, value)

    companion object {
        fun emptyPicture(): Picture = Picture(PatchHeader.Companion.emptyHeader(), emptyArray())
    }

    fun buildDoomImage(): DoomBitmap =
        DoomBitmap(header.width.toInt(), header.height.toInt()).also { bitmap ->
            var ix = 0
            var iy: Int
            for (post in posts) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Picture

        if (header != other.header) return false
        if (!posts.contentEquals(other.posts)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + posts.contentHashCode()
        return result
    }
}