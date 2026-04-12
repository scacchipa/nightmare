package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.SegHandler
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.ext.atan2
import ar.com.scacchi.nightmare.source.wad.NmColor
import ar.com.scacchi.nightmare.source.wad.NmPalette
import ar.com.scacchi.nightmare.source.wad.NmPlayPal
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class Nm3DScreenTest {

    private val mockGameState = mockk<GameState>(relaxed = true)
    private val mockImageRepository = mockk<ImageRepository>()
    private val mockFlatTex = mockk<DoomBitmap>()
    private val mockPlayer = mockk<Player>()

    private lateinit var screen: Nm3DScreen

    val testColor = NmColor(255u, 100u, 100u, 100u)
    val redColor = NmColor(255u, 255u, 0u, 0u)
    val blueColor = NmColor(255u, 0u, 0u, 255u)

    val myPalette = NmPalette(content = Array(256) { NmColor(255u, 0u, 0u, 0u) }).also {
        it[0] = redColor
        it[5] = blueColor
        it[10] = testColor
    }
    val myPlayPal = NmPlayPal(content = arrayOf(myPalette))

    @Before
    fun setup() {
        every { mockGameState.player } returns mockPlayer
        every { mockGameState.playPal } returns myPlayPal
        every { mockPlayer.pos } returns Offset(0f, 0f)
        every { mockPlayer.dirVector } returns Offset(1f, 0f)

        every { mockFlatTex[any(), any()] } returns 0.toShort()

        screen = Nm3DScreen(mockGameState, mockImageRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `drawFlatCol should not iterate if y1 is greater than or equal to y2`() {
        screen.drawFlatCol(mockFlatTex, 10f, 100f, 100f, 1.0f, 64f)

        verify(exactly = 0) { mockFlatTex[any(), any()] }
    }

    @Test
    fun `drawFlatCol should correctly calculates the UV coordinates of the texture`() {
        every { mockFlatTex[any(), any()] } returns 5.toShort()

        screen.drawFlatCol(mockFlatTex, 100f, 120f, 121f, 1f, 32f)

        verify(exactly = 1) { mockFlatTex[any(), any()] }
    }

    @Test
    fun `drawFlatCol handles transparent pixels in flat`() {

        every { mockFlatTex[any(), any()] } returns (-1).toShort()

        screen.drawFlatCol(mockFlatTex, 10f, 100f, 101f, 1f, 64f)

        verify(atLeast = 1) { mockGameState.player }

        confirmVerified(mockGameState)
    }

    @Test
    fun `rawFlatCol test with real objects`() {
        val testColor = NmColor(255u, 100u, 100u, 100u)
        val myPalette = NmPalette(content = Array(256) { NmColor(255u, 0u, 0u, 0u) }).also {
            it[10] = testColor
        }

        every { mockGameState.playPal[0] } returns myPalette
        every { mockFlatTex[any(), any()] } returns 10.toShort()

        screen.drawFlatCol(mockFlatTex, 160f, 100f, 101f, 1f, 64f)

        verify { mockFlatTex[any(), any()] }
    }

    @Test
    fun `drawFlat should call to drawWallCol when the texture is the sky`() {
        val screenSpy = spyk(screen, recordPrivateCalls = true)
        val mockSkyTex = mockk<DoomBitmap>()

        every { mockImageRepository.getPictureDoomBitmap("SKY1") } returns mockSkyTex

        every {
            screenSpy.drawWallCol(any(), any(), any(), any(), any(), any(), any(), any())
        } returns Unit

        screenSpy.drawFlat("F_SKY1", 1.0f, 100f, 50f, 150f, 0f)

        verify(exactly = 1) {
            screenSpy.drawWallCol(
                mockSkyTex, any(), 100f, 50f, 150f, any(), any(), any()
            )
        }

        verify(exactly = 0) { screenSpy.drawFlatCol(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `drawFlat should call to drawFlatCol for common textures`() {
        val screenSpy = spyk(screen, recordPrivateCalls = true)
        val mockFlatTex = mockk<DoomBitmap>()
        val floorTexName = "FLOOR0_1"

        every { mockImageRepository.getFlatDoomBitmap(floorTexName) } returns mockFlatTex
        every {
            screenSpy.drawFlatCol(any(), any(), any(), any(), any(), any())
        } returns Unit
        screenSpy.drawFlat(floorTexName, 0.8f, 100f, 120f, 180f, 64f)
        verify(exactly = 1) {
            screenSpy.drawFlatCol(mockFlatTex, 100f, 120f, 180f, 0.8f, 64f)
        }
        verify(exactly = 0) { screenSpy.drawWallCol(any(), any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `drawFlat should do nothing when y1 is greater or equal to y2`() {
        val screenSpy = spyk(screen)

        screenSpy.drawFlat("CUALQUIERA", 1f, 10f, 100f, 50f, 0f) // y1=100, y2=50

        verify(exactly = 0) { mockImageRepository.getFlatDoomBitmap(any()) }
        verify(exactly = 0) { screenSpy.drawFlatCol(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `drawFlat should calculate texColum using the player direction and a angle table`() {
        val screenSpy = spyk(screen, recordPrivateCalls = true)
        val mockSkyTex = mockk<DoomBitmap>(relaxed = true)
        val texColSlot = slot<Int>()

        val xIndex = 150
        val angleInTable = 0.25f
        SegHandler.xToAngleTable[xIndex] = angleInTable

        every { mockPlayer.dirVector } returns Offset(1f, 0f)
        every { mockImageRepository.getPictureDoomBitmap("SKY1") } returns mockSkyTex
        every {
            screenSpy.drawWallCol(
                any(), capture(texColSlot), any(), any(), any(), any(), any(), any()
            )
        } returns Unit

        screenSpy.drawFlat("F_SKY1", 1.0f, xIndex.toFloat(), 0f, 100f, 0f)
        val expectedValue = (2.2f * (Offset(1f, 0f).atan2() + angleInTable) * 90).toInt()

        assert(
            texColSlot.captured == expectedValue,
        ) { "The calculation of the column of the sky does not match the marthematical formula" }
    }
}