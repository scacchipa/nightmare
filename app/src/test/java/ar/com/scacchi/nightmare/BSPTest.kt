package ar.com.scacchi.nightmare

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.atan2
import ar.com.scacchi.nightmare.ext.deRotateBy
import ar.com.scacchi.nightmare.ext.rotatedBy
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_DIST
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.tan

class BSPTest {

    @Test
    fun `vectorToX() should return column number`() {
        val vector = Offset(1f, 0.20f)

        val result1 = BSP.vectorToX(vector)
        val result2 = BSP.angleToX(vector.atan2())
        val result3 = H_WIDTH - SCREEN_DIST * tan(vector.atan2())  // 320f

        assertEquals(result1, result3, 0.001f)
        assertEquals(result2, result3, 0.001f)
    }


    @Test
    fun `isOutsideLeft() checks if a point in left outside`() {
        val point1 = Offset(3f, 1f)
        val point2 = Offset(2f, 3f)
        val point3 = Offset(-2f, -1f)

        val angle1 = point1.atan2()
        val angle2 = point2.atan2()
        val angle3 = point3.atan2()

        val resultPoint1 = point1.rotatedBy(point2)
        val resultAngle1 = resultPoint1.atan2()
        val resultPoint2 = point1.rotatedBy(point3)
        val resultAngle2 = resultPoint2.atan2()
        val resultPoint3 = point2.rotatedBy(point3)
        val resultAngle3 = resultPoint3.atan2()
        val resultPoint4 = point1.deRotateBy(point2)
        val resultAngle4 = resultPoint4.atan2()


        assertEquals(resultAngle1, angle1 + angle2, 0.001f)
        assertEquals(resultAngle2, angle1 + angle3, 0.001f)
        assertEquals(resultAngle3, angle2 + angle3, 0.001f)
        assertEquals(resultAngle4, angle1 - angle2, 0.001f)


        assertFalse(BSP.isOutsideLeft(point1))
        assertFalse(BSP.isOutsideRight(point1))
        assertTrue(BSP.isOutsideLeft(point2))
        assertFalse(BSP.isOutsideRight(point2))
        assertTrue(BSP.isOutsideLeft(point3))
        assertTrue(BSP.isOutsideRight(point3))
        assertTrue(BSP.isOutsideLeft(resultPoint1))
        assertFalse(BSP.isOutsideRight(resultPoint1))
//        assertTrue(BSP.isOutsideLeft(resultPoint2))
        assertTrue(BSP.isOutsideRight(resultPoint2))
        assertFalse(BSP.isOutsideLeft(resultPoint3))
        assertTrue(BSP.isOutsideRight(resultPoint3))
    }
}