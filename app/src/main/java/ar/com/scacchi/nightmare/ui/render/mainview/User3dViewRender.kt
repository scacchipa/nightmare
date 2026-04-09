package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.settings.SCREEN_ASPECT
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ColumnScope.User3dViewRender(
    modifier: Modifier = Modifier,
    gameState: GameState,
    imageRepository: ImageRepository,
) {
    val user3DScreen = Nm3DScreen(gameState, imageRepository)
    val startNow = Clock.System.now()
    val user3dBitmap = with(user3DScreen) {
        isTraverseBsp = false
        renderBspNode(gameState.episodeMap.rootNodeId)
        user3DScreen.createBitmap()
    }
    println("Time to draw a frame ${Clock.System.now() - startNow}")
    Image(
        modifier = Modifier.fillMaxWidth().aspectRatio(SCREEN_ASPECT),
        bitmap = user3dBitmap.asImageBitmap(),
        contentDescription = null,
    )
}