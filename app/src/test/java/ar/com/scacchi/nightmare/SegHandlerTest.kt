package ar.com.scacchi.nightmare

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.ext.versorWithAngle
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.math.PI

class SegHandlerTest {

    private val epsilon = 1e-4f

//    private val SCREEN_DIST = 1.0f
//    private val MAX_SCALE = 64f
//    private val MIN_SCALE = 0.001f

    val mockPlayer = mockk<Player>()

    val segHandler = SegHandler(
        user3dScreen = mockk(),
        player = mockPlayer,
        imageRepository = mockk()
    )

    @Before
    fun setUp() {
        every { mockPlayer.dirVersor } returns Offset.versorWithAngle(0.5f)
    }

    @Test
    fun `Compare scale by looking at a front wall`() {
        val x = 50
        val rwDistance = 128f

        val rwNormalAngle = PI.toFloat()
        val normalVector = Offset.versorWithAngle(rwNormalAngle)

        val resultAngle = segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        val resultVertex = segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)

        assertEquals("La escala debería ser idéntica en el centro", resultAngle, resultVertex, epsilon)
    }

    @Test
    fun `compare scale at the edges of the screen`() {
        val x = 0
        val rwDistance = 200f

        val rwNormalAngle = 2.5f
        val normalVector = Offset.versorWithAngle(rwNormalAngle)

        val resultAngle = segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        val resultVertex = segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)

        assertEquals("La escala debería coincidir en los bordes del FOV", resultAngle, resultVertex, epsilon, )
    }

    @Test
    fun `verify behavior with limit values`() {
        val x = 50
        val rwDistance = 0.0001f

        val rwNormalAngle = 0f
        val normalVector = Offset(1f, 0f)

        val resultAngle = segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        val resultVertex = segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)

        assertEquals("Ambos deberían clavar el máximo", segHandler.MAX_SCALE, resultAngle)
        assertEquals("Ambos deberían clavar el máximo", segHandler.MAX_SCALE, resultVertex)
    }

    @Test
    fun `verify behavior with positive angle`() {
        val x = 50
        val rwDistance = 20f

        val rwNormalAngle = .70f
        val normalVector = Offset.versorWithAngle(.70f)

        val resultAngle = segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        val resultVertex = segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)

        assertEquals(resultAngle, resultVertex)
    }

    @Test
    fun `verify behavior real case`() {
        val x = 780
        val rwDistance = 1248f

        val rwNormalAngle = 0f
        val normalVector = Offset.versorWithAngle(rwNormalAngle)

        val resultAngle = segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        val resultVertex = segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)

        assertEquals(resultAngle, resultVertex)
    }

//    @Test
    fun `prueba de velocidad`() {

        val x = 50
        val rwDistance = 0.0001f

        val rwNormalAngle = 0f
        val normalVector = Offset(1f, 0f)

        var initialTime = System.currentTimeMillis()
        repeat(10_000) {
            segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        }
        println("Angle:  " + (System.currentTimeMillis() - initialTime))

        repeat(10_000) {
            segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)
        }
        println("Vector:  " + (System.currentTimeMillis() - initialTime))


        initialTime = System.currentTimeMillis()
        repeat(10_000) {
            segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        }
        println("Angle:  " + (System.currentTimeMillis() - initialTime))

        repeat(10_000) {
            segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)
        }
        println("Vector:  " + (System.currentTimeMillis() - initialTime))


        initialTime = System.currentTimeMillis()
        repeat(10_000) {
            segHandler.scaleFromGlobalAngle(x, rwNormalAngle, rwDistance)
        }
        println("Angle:  " + (System.currentTimeMillis() - initialTime))

        repeat(10_000) {
            segHandler.scaleFromGlobalVertex(x, normalVector, rwDistance)
        }
        println("Vector:  " + (System.currentTimeMillis() - initialTime))
    }

}