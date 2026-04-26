package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

interface IScreenRange {
    val start: Int
    val endInclusive: Int

    fun isInside(item: Int): Boolean
    fun intersection(range: IntRange): IntersectionType
    fun clipIntersection(range: IntRange): List<IntRange>
    override fun toString(): String
    override operator fun equals(other: Any?): Boolean
}

