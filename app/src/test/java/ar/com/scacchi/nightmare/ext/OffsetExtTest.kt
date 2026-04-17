package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class OffsetExtTest {
    @Test
    fun `Offset atan2 vs fastAtan2()`() {
        for (idx in  0 until 360) {
            val alpha = TWO_PI / 360 * idx
            val x = cos(alpha)
            val y = sin(alpha)
            val a1 = Offset(x, y).atan2()
            val b1 = Offset(x, y).fastAtan2()
            val diff = a1 - b1
            assert(diff.absoluteValue < 0.001f) {"$x, $y: tan:${tan(alpha)}: $a1, $b1 (diff: ${diff.absoluteValue})" }
        }
    }

    @Test
    fun `vectorToPoint() should return a vector with the same direction as the other one`() {

        val playerDirection = Offset.versorWithAngle(0.4f) * 3f
        val point = Offset(0.1f, 0.2f)

        val vectorResult = playerDirection.vectorToPoint(point)
        val versorResult = playerDirection.versorToPoint(point)

        assertEquals(vectorResult.x, -2.663f, 0.01f)
        assertEquals(vectorResult.y, -0.968f, 0.01f)
        assertEquals(versorResult.x, -0.939f, 0.01f)
        assertEquals(versorResult.y, -0.341f, 0.01f)
    }

    @Test
    fun `rotateBy() should rotate the correct vector`() {

        val vector = Offset(1f, 4f)
        val dir = Offset(1f, 0.25f)

        val result1 = vector.rotatedBy(dir)
        val result2 = vector.deRotateBY(dir)
        val result3 = vector.rotatedBy(dir.flipY())

        val vectorAngle = vector.atan2()
        val dirAngle = dir.atan2()
        val result1Angle = result1.atan2()
        val result2Angle = result2.atan2()
        val result3Angle = result3.atan2()

        assertEquals(result1Angle, vectorAngle + dirAngle, 0.01f)
        assertEquals(result2Angle, vectorAngle - dirAngle, 0.01f)
        assertEquals(result3Angle, vectorAngle - dirAngle, 0.01f)
    }

    @Test
    fun `Offset normalize() should return the correct value`() {
        val vector1 = Offset(1f, 4f)    // angle: 1.32
        val vector2 = Offset(2f, 1f)    // angle: 0.46
        val vector3 = Offset(-2f, 1f)   // angle: 2.67
        val vector4= Offset(-4f, -4f)   // angle: -2.35
        val vector5 = Offset(-2f, -2f)  // angle: -2.35
        val vector6 = Offset(0.5f, 2.001f)    // angle: 1.33
        val vector7 = Offset(0.5f, 1.999f)    // angle: 1.31

        assert(vector1 > vector2)
        assert(vector1 < vector3)
        assert(vector3 > vector4)
//        assert(vector4 == vector5)
        assert(vector1 < vector6)
        assert(vector1 > vector7)
    }

    @Test
    fun `Offset have to compare correctly`() {
        val v1 = Offset(2f, 0f)       // angle: 0.0
        val v2 = Offset(4f, 1f)       // angle: 0.24
        val v3 = Offset(4f, -1f)      // angle: -0.24
        val v4 = Offset(-2f, -2f)     // angle: -2.35
        val v5 = Offset(128f, -10f)   // angle: -0.07
        val v6 = Offset(-128f, -10f)  // angle: -3.06
        val v7 = Offset(-10f, 10f)    // angle: 2.5

        assert(v1 < v2)
        assert(v1 > v3)
        assert(v1 > v4)
        assert(v1 > v5)

        assert(v7 > v2)
        assert(v2 > v1)
        assert(v1 > v5)
        assert(v5 > v3)
        assert(v3 > v4)
        assert(v4 > v6)

        assert(v7 > v6)
        assert(v7 > v3)
        assert(v2 > v4)
    }


    @Test
    fun `test cruce por semirrecta positiva`() {
        // Un segmento que va de (1, -1) a (1, 1) cruza el eje X en x=1
        val a = Offset(1f, -1f)
        val b = Offset(1f, 1f)
        assertEquals(AxisIntersection.POSITIVE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test cruce por semirrecta negativa`() {
        // Un segmento que va de (-5, 2) a (-5, -2) cruza el eje X en x=-5
        val a = Offset(-5f, 2f)
        val b = Offset(-5f, -2f)
        assertEquals(AxisIntersection.NEGATIVE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test segmento diagonal cruzando positivo`() {
        // De (-1, -1) a (3, 1). Cruza en el punto medio (1, 0)
        val a = Offset(-1f, -1f)
        val b = Offset(3f, 1f)
        assertEquals(AxisIntersection.POSITIVE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test sin cruce (ambos arriba)`() {
        val a = Offset(2f, 2f)
        val b = Offset(-2f, 5f)
        assertEquals(AxisIntersection.NONE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test sin cruce (paralelo al eje X)`() {
        val a = Offset(-2f, 2f)
        val b = Offset(2f, 2f)
        assertEquals(AxisIntersection.NONE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test cruce exactamente por el origen`() {
        // De (-1, -1) a (1, 1). Cruza en (0,0)
        val a = Offset(-1f, -1f)
        val b = Offset(1f, 1f)
        assertEquals(AxisIntersection.NONE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test un vertice sobre el eje no cuenta como cruce`() {
        // Si un punto está en el eje, técnicamente no lo "atraviesa" según la lógica de signos opuestos
        val a = Offset(5f, 0f)
        val b = Offset(5f, 5f)
        assertEquals(AxisIntersection.NONE, Offset.checkXAxisIntersection(a, b))
    }

    @Test
    fun `test cruce con valores muy cercanos al eje`() {
        // Caso de precisión
        val a = Offset(10f, 0.00001f)
        val b = Offset(10f, -0.00001f)
        assertEquals(AxisIntersection.POSITIVE, Offset.checkXAxisIntersection(a, b))
    }
}