package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

object ScreenRangeNull : IScreenRange {
    override val start: Int get() = throw NoSuchElementException("No start in NullRange")
    override val endInclusive: Int get() = throw NoSuchElementException("No end in NullRange")

    override fun isInside(item: Int): Boolean = false
    override fun intersection(range: IntRange): IntersectionType =
        IntersectionType.NoIntersection
    override fun clipIntersection(range: IntRange): List<IntRange> = listOf()

    override fun toString(): String = "ScreenRangeMull"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass != other?.javaClass
    }
}