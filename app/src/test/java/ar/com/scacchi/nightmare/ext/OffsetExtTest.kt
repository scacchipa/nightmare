package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

class OffsetExtTest {

    @Test
    fun `Offset atan2() should return the correct angle`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(x, y).atan2()
        }
    }
    @Test
    fun `Offset atan2() should return the correct angle2`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(x, y).atan2()
        }
    }
    @Test
    fun `Offset atan2() should return the correct angle3`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(x, y).atan2()
        }
    }
    @Test
    fun `Offset fastAtan2() should return the correct angle`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(1f, y).fastAtan2()
        }
    }
    @Test
    fun `Offset fastAtan2() should return the correct angle2`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(1f, y).fastAtan2()
        }
    }
    @Test
    fun `Offset fastAtan2() should return the correct angle3`() {
        for (idx in  0 until 10_000_000) {
            val x = Random.nextFloat()
            val y = Random.nextFloat()
            Offset(1f, y).fastAtan2()
        }
    }
    @Test
    fun `Offset atan2 vs fastAtan2()`() {
        for (idx in  0 until 4_000) {
            val alpha = TWO_PI / 4_000 * idx
            val x = cos(alpha)
            val y = sin(alpha)
            val a1 = Offset(x, y).atan2()
            val b1 = Offset(x, y).fastAtan2()
            val diff = a1 - b1
            assert(diff.absoluteValue < 0.001f) {"$x, $y: tan:${tan(alpha)}: $a1, $b1 (diff: ${diff.absoluteValue})" }
        }
    }
}