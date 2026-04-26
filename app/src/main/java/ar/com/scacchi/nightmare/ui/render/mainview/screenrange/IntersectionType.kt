package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

sealed class IntersectionType {
    data object NoIntersection: IntersectionType()
    data object CoverAll: IntersectionType()
    data class SameStart(val endInclusive: Int): IntersectionType()
    data class SomeEnd(val start: Int): IntersectionType()
    data class Inside(val start: Int, val endInclusive: Int): IntersectionType()
}