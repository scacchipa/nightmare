package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

class OcclusionMask(
    var firstScreenRange: IScreenRange = ScreenRangeNull,
    var secondScreenRange: IScreenRange = ScreenRangeNull,
) : IScreenRange {

    var resultFirstScreenRange: IScreenRange = ScreenRangeNull
    var resultSecondScreenRange: IScreenRange = ScreenRangeNull

    override val start: Int get() = firstScreenRange.start
    override val endInclusive: Int
        get() =
            if (secondScreenRange == ScreenRangeNull) firstScreenRange.endInclusive
            else secondScreenRange.endInclusive

    override fun isInside(item: Int): Boolean = item >= start && item <= endInclusive
    override fun intersection(range: IntRange): IntersectionType {
        return firstScreenRange.intersection(range)
    }

    override fun clipIntersection(range: IntRange): List<IntRange> {
        resultFirstScreenRange = firstScreenRange
        resultSecondScreenRange = secondScreenRange
        val firstRangeList = when (firstScreenRange) {
            is ScreenRangeLeaf -> clipFirstRange(range)
            is OcclusionMask -> firstScreenRange.clipIntersection(range)
            else -> return listOf()
        }
        val secondRangeList = secondScreenRange.let { screenRange ->
            return@let when (secondScreenRange) {
                is ScreenRangeLeaf -> clipSecondRange(range)
                is OcclusionMask -> secondScreenRange.clipIntersection(range)
                else -> listOf()
            }
        }

        if (resultFirstScreenRange is OcclusionMask &&
            (resultFirstScreenRange as OcclusionMask).firstScreenRange == ScreenRangeNull &&
            (resultFirstScreenRange as OcclusionMask).secondScreenRange == ScreenRangeNull
        )
            resultFirstScreenRange = ScreenRangeNull

        if (resultSecondScreenRange is OcclusionMask &&
            (resultSecondScreenRange as OcclusionMask).firstScreenRange == ScreenRangeNull &&
            (resultSecondScreenRange as OcclusionMask).secondScreenRange == ScreenRangeNull
        )
            resultSecondScreenRange = ScreenRangeNull

        val staticFirstRange = resultFirstScreenRange
        val staticSecondRange = resultSecondScreenRange

        if (resultSecondScreenRange == ScreenRangeNull) {
            firstScreenRange = resultFirstScreenRange
            secondScreenRange = resultSecondScreenRange
        } else {
            when (staticFirstRange) {
                is ScreenRangeLeaf -> {
                    firstScreenRange = staticFirstRange
                    secondScreenRange = staticSecondRange
                }

                is OcclusionMask -> {
                    firstScreenRange = staticFirstRange
                    secondScreenRange = staticSecondRange
                }

                is ScreenRangeNull -> {
                    when {
                        secondScreenRange is ScreenRangeLeaf -> {
                            firstScreenRange = staticSecondRange
                            secondScreenRange = ScreenRangeNull
                        }

                        staticSecondRange is OcclusionMask -> {
                            firstScreenRange = staticSecondRange.firstScreenRange
                            secondScreenRange = staticSecondRange.secondScreenRange
                        }
                    }
                }
            }
        }

        return firstRangeList + secondRangeList
    }

    private fun clipFirstRange(range: IntRange): List<IntRange> {
        val intersection = firstScreenRange.intersection(range)

        when (intersection) {
            is IntersectionType.CoverAll -> {
                resultFirstScreenRange = ScreenRangeNull
                return listOf(IntRange(firstScreenRange.start, firstScreenRange.endInclusive))
            }

            is IntersectionType.Inside -> {
                if (secondScreenRange == ScreenRangeNull) {
                    resultFirstScreenRange =
                        ScreenRangeLeaf(firstScreenRange.start, intersection.start)
                    resultSecondScreenRange =
                        ScreenRangeLeaf(intersection.endInclusive, firstScreenRange.endInclusive)
                } else {
                    resultFirstScreenRange = OcclusionMask(
                        ScreenRangeLeaf(firstScreenRange.start, intersection.start),
                        ScreenRangeLeaf(intersection.endInclusive, firstScreenRange.endInclusive)
                    )
                }
                return listOf(IntRange(intersection.start, intersection.endInclusive))
            }

            is IntersectionType.NoIntersection -> return listOf()
            is IntersectionType.SameStart -> {
                resultFirstScreenRange =
                    ScreenRangeLeaf(intersection.endInclusive, firstScreenRange.endInclusive)
                return listOf(IntRange(firstScreenRange.start, intersection.endInclusive))
            }

            is IntersectionType.SomeEnd -> {
                resultFirstScreenRange = ScreenRangeLeaf(firstScreenRange.start, intersection.start)
                return listOf(IntRange(intersection.start, firstScreenRange.endInclusive))
            }
        }
    }

    private fun clipSecondRange(range: IntRange): List<IntRange> {
        val intersection = secondScreenRange.intersection(range)

        when (intersection) {
            is IntersectionType.CoverAll -> {
                resultSecondScreenRange = ScreenRangeNull
                return listOf(
                    IntRange(secondScreenRange.start, secondScreenRange.endInclusive)
                )
            }

            is IntersectionType.Inside -> {
                resultSecondScreenRange = OcclusionMask(
                    ScreenRangeLeaf(secondScreenRange.start, intersection.start),
                    ScreenRangeLeaf(intersection.endInclusive, secondScreenRange.endInclusive)
                )
                return listOf(IntRange(intersection.start, intersection.endInclusive))
            }

            is IntersectionType.NoIntersection -> return listOf()
            is IntersectionType.SameStart -> {
                resultSecondScreenRange = ScreenRangeLeaf(
                    intersection.endInclusive, secondScreenRange.endInclusive
                )
                return listOf(
                    IntRange(secondScreenRange.start, intersection.endInclusive)
                )
            }

            is IntersectionType.SomeEnd -> {
                resultSecondScreenRange =
                    ScreenRangeLeaf(secondScreenRange.start, intersection.start)
                return listOf(
                    IntRange(intersection.start, secondScreenRange.endInclusive)
                )
            }
        }
    }

    override fun toString(): String =
        "OcclusionMask(firstScreenRange = $firstScreenRange, secondScreenRange = $secondScreenRange)"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return (other as OcclusionMask).firstScreenRange == firstScreenRange &&
                other.secondScreenRange == secondScreenRange
    }

    override fun hashCode(): Int {
        var result = firstScreenRange.hashCode()
        result = 31 * result + secondScreenRange.hashCode()
        return result
    }
}