package ar.com.scacchi.nightmare.data.asset

data class Flat(
    val content: Array<Array<UByte>>,
) {
    fun buildDoomImage(): DoomBitmap =
        DoomBitmap(64, 64).also { image ->
            for (idx in 0 until 64) {
                for (idy in 0 until 64) {
                    image[idx, idy] = this.content[idx][idy]
                }
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Flat

        return content.contentDeepEquals(other.content)
    }

    override fun hashCode(): Int {
        return content.contentDeepHashCode()
    }
}