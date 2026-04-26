package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

import kotlin.math.max
import kotlin.math.min

class ScreenRangeLeaf(
    override val start: Int,
    override val endInclusive: Int,
): IScreenRange {
    
    override fun isInside(item: Int): Boolean = item >= start && item <= endInclusive

    override fun intersection(range: IntRange): IntersectionType {
        if (this.start > range.endInclusive) return IntersectionType.NoIntersection
        if (this.endInclusive < range.start) return IntersectionType.NoIntersection

        val init = max(this.start, range.start)
        val finish = min(this.endInclusive, range.endInclusive)

        return if (this.start == init)  {
            if (this.endInclusive == finish) IntersectionType.CoverAll
            else IntersectionType.SameStart(finish)
        } else {
            if (this.endInclusive == finish) IntersectionType.SomeEnd(init)
            else IntersectionType.Inside(init, finish)
        }
    }

    override fun clipIntersection(range: IntRange): List<IntRange> {
        TODO("Not yet implemented")
    }

    override fun toString(): String =
        "ScreenRangeLeaf(start = $start, end = $endInclusive)"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return (other as ScreenRangeLeaf).start == start &&
                other.endInclusive == endInclusive
    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + endInclusive
        return result
    }
}