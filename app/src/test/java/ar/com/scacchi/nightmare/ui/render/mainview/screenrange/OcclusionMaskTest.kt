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

        assert(mask.firstScreenRange == ScreenRangeLeaf(10, 20))
        assert(mask.secondScreenRange == ScreenRangeNull)
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

        assert(mask.firstScreenRange == ScreenRangeLeaf(12, 15))
        assert(mask.secondScreenRange == ScreenRangeNull)
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

        assert(mask1.firstScreenRange == ScreenRangeNull)
        assert(mask1.secondScreenRange == ScreenRangeNull)
        assert(mask2.firstScreenRange == ScreenRangeNull)
        assert(mask2.secondScreenRange == ScreenRangeNull)
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
    fun testOcclusionMaskIntersectionCoverAllMovingRange() {
        val mask1 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )
        val mask2 = OcclusionMask(
            firstScreenRange = ScreenRangeLeaf(10, 20),
            secondScreenRange = ScreenRangeLeaf(30, 40),
        )

        val result1 = mask1.clipIntersection(IntRange(10, 20))
        val result2 = mask2.clipIntersection(IntRange(30, 40))

        assert(mask1.firstScreenRange == ScreenRangeLeaf(30, 40))
        assert(mask1.secondScreenRange == ScreenRangeNull)
        assertEquals(result1.first(), IntRange(10, 20))
        assertEquals(result1.size, 1)

        assert(mask2.firstScreenRange == ScreenRangeLeaf(10, 20))
        assert(mask2.secondScreenRange == ScreenRangeNull)
        assertEquals(result2.first(), IntRange(30, 40))
        assertEquals(result2.size, 1)
    }

    @Test
    fun processedComplexOcclusionTest() {
        val mask = OcclusionMask(ScreenRangeLeaf(10, 50))
        val result1 = mask.clipIntersection(IntRange(20, 30))

        assertEquals(mask, OcclusionMask(ScreenRangeLeaf(10, 20), ScreenRangeLeaf(30, 50)))
        assertEquals(result1.first(), IntRange(20, 30))
        assertEquals(result1.size, 1)

        val result2 = mask.clipIntersection(IntRange(40, 45))
        assertEquals(
            mask,
            OcclusionMask(
                ScreenRangeLeaf(10, 20),
                OcclusionMask(ScreenRangeLeaf(30, 40), ScreenRangeLeaf(45, 50))
            )
        )

        assertEquals(result2.first(), IntRange(40, 45))
        assertEquals(result2.size, 1)

        val result3 = mask.clipIntersection(IntRange(33, 35))
        assertEquals(
            OcclusionMask(
                ScreenRangeLeaf(10, 20),
                OcclusionMask(
                    OcclusionMask(ScreenRangeLeaf(30, 33), ScreenRangeLeaf(35, 40)),
                    ScreenRangeLeaf(45, 50)
                )
            ),
            mask,
        )
        assertEquals(result3.first(), IntRange(33, 35))
        assertEquals(result3.size, 1)

        val result4 = mask.clipIntersection(IntRange(31, 31))

        assertEquals(
            OcclusionMask(
                ScreenRangeLeaf(10, 20),
                OcclusionMask(
                    OcclusionMask(
                        OcclusionMask(
                            ScreenRangeLeaf(30, 31),
                            ScreenRangeLeaf(31, 33)
                        ),
                        ScreenRangeLeaf(35, 40)),
                    ScreenRangeLeaf(45, 50)
                )
            ),
            mask,
        )
        assertEquals(result4.first(), IntRange(31, 31))
        assertEquals(result4.size, 1)

        val result5 = mask.clipIntersection(IntRange(25, 50))

        assertEquals(
            OcclusionMask(
                ScreenRangeLeaf(10, 20),
                ScreenRangeNull,
            ),
            mask,
        )
        assertEquals(result5[0], IntRange(30, 31))
        assertEquals(result5[1], IntRange(31, 33))
        assertEquals(result5[2], IntRange(35, 40))
        assertEquals(result5[3], IntRange(45, 50))
        assertEquals(result5.size, 4)
    }
}