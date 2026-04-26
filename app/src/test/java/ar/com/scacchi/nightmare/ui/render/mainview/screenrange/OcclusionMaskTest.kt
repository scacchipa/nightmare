package ar.com.scacchi.nightmare.ui.render.mainview.screenrange

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcclusionMaskTest {

    @Test
    fun testOcclusionMaskIntersectionInside() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(0, 10),
        )

        val result = mask.clipIntersection(IntRange(3, 5))

        assertEquals(mask.firstScreenRange, ScreenRangeLeaf(0, 3))
        assertEquals(mask.secondScreenRange, ScreenRangeLeaf(5, 10))
        assertEquals(result.first(), IntRange(3, 5))
        assertEquals(result.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionNoIntersection() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
        )

        val result1 = mask.clipIntersection(IntRange(3, 5))
        val result2 = mask.clipIntersection(IntRange(23, 25))

        assertEquals(mask.firstScreenRange, ScreenRangeLeaf(10, 20))
        assertEquals(mask.secondScreenRange, null)
        assertTrue(result1.isEmpty())
        assertTrue(result2.isEmpty())
    }

    @Test
    fun testOcclusionMaskIntersectionSameStartAndEnd() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
        )

        val result1 = mask.clipIntersection(IntRange(10, 12))
        val result2 = mask.clipIntersection(IntRange(15, 20))

        assertEquals(mask.firstScreenRange, ScreenRangeLeaf(12, 15))
        assertEquals(mask.secondScreenRange, null)
        assertEquals(result1.first(), IntRange(10, 12))
        assertEquals(result1.size, 1)
        assertEquals(result2.first(), IntRange(15, 20))
        assertEquals(result2.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionSameStartAndEndExtended() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
        )

        val result1 = mask.clipIntersection(IntRange(5, 12))
        val result2 = mask.clipIntersection(IntRange(15, 25))

        assertEquals(mask.firstScreenRange, ScreenRangeLeaf(12, 15))
        assertEquals(result1.first(), IntRange(10, 12))
        assertEquals(result1.size, 1)
        assertEquals(result2.first(), IntRange(15, 20))
        assertEquals(result2.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionCoverAll() {
        val mask1 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
        )
        val mask2 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
        )

        val result1 = mask1.clipIntersection(IntRange(10, 20))
        val result2 = mask2.clipIntersection(IntRange(5, 25))

        assertEquals(mask1.firstScreenRange, ScreenRangeNull)
        assertEquals(mask1.secondScreenRange, null)
        assertEquals(mask2.firstScreenRange, ScreenRangeNull)
        assertEquals(mask2.secondScreenRange, null)
        assertEquals(result1.first(), IntRange(10, 20))
        assertEquals(result1.size, 1)
        assertEquals(result2.first(), IntRange(10, 20))
        assertEquals(result2.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionInsideWithSecondRangeNotNUll() {
        val mask1 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40)
        )
        val result = mask1.clipIntersection(IntRange(14, 17))

        assertEquals(
            mask1.firstScreenRange,
            OcclusionMask(ScreenRangeLeaf(10, 14), ScreenRangeLeaf(17, 20))
        )
        assertEquals(mask1.secondScreenRange, ScreenRangeLeaf(30, 40))

        assertEquals(result.first(), IntRange(14, 17))
        assertEquals(result.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionInsideInSecondScreenRange() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )

        val result = mask.clipIntersection(IntRange(33, 35))
        assertEquals(mask.firstScreenRange, ScreenRangeLeaf(10, 20))
        assertEquals(
            mask.secondScreenRange,
            OcclusionMask(ScreenRangeLeaf(30, 33), ScreenRangeLeaf(35, 40))
        )
        assertEquals(result.first(), IntRange(33, 35))
    }

    @Test
    fun testOcclusionMaskIntersectionNoIntersectionWithSecondRange() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )

        val result1 = mask.clipIntersection(IntRange(3, 5))
        val result2 = mask.clipIntersection(IntRange(23, 25))
        val result3 = mask.clipIntersection(IntRange(45, 47))

        val resultOcclusionMask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )
        assertEquals(mask, resultOcclusionMask)
        assertTrue(result1.isEmpty())
        assertTrue(result2.isEmpty())
        assertTrue(result3.isEmpty())
    }

    @Test
    fun testOcclusionMaskIntersectionSameStartAndEndExtendedInSecondScreenRange() {
        val mask = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )

        val result1 = mask.clipIntersection(IntRange(25, 32))
        val result2 = mask.clipIntersection(IntRange(35, 45))

        assertEquals(
            mask,
            OcclusionMask(
                firstScreenRange = ScreenRangeLeaf(10, 20),
                secondScreenRange = ScreenRangeLeaf(32, 35)
            )
        )
        assertEquals(result1.first(), IntRange(30, 32))
        assertEquals(result1.size, 1)
        assertEquals(result2.first(), IntRange(35, 40))
        assertEquals(result2.size, 1)
    }

    @Test
    fun testOcclusionMaskIntersectionCoverAllInSecondScreenRange() {
        val mask1 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )

        val result1 = mask1.clipIntersection(IntRange(0, 50))

        assertEquals(mask1.firstScreenRange, ScreenRangeNull)
        assertEquals(mask1.secondScreenRange, null)
        assertEquals(result1[0], IntRange(10, 20))
        assertEquals(result1[1], IntRange(30, 40))
        assertEquals(result1.size, 2)
    }
}