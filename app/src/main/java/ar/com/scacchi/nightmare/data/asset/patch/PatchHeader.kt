package ar.com.scacchi.nightmare.data.asset.patch

data class PatchHeader(
    val width: UShort, // H
    val height: UShort,  // H
    val leftOffset: Short, // h
    val topOffset: Short,  // h
    val columnOffset: Array<UInt>, // wi = dth x I
) {
    companion object {
        fun emptyHeader() = PatchHeader(0u, 0u, 0, 0, emptyArray())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PatchHeader

        if (leftOffset != other.leftOffset) return false
        if (topOffset != other.topOffset) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (!columnOffset.contentEquals(other.columnOffset)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leftOffset.toInt()
        result = 31 * result + topOffset
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + columnOffset.contentHashCode()
        return result
    }
}