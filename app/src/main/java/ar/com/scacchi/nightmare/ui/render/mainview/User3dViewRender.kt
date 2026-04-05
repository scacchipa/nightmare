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

@Composable
fun ColumnScope.User3dViewRender(
    modifier: Modifier = Modifier,
    gameState: GameState,
    imageRepository: ImageRepository,
) {
    val user3DScreen = Nm3DScreen(gameState, imageRepository)

    val user3dBitmap = with(user3DScreen) {
        isTraverseBsp = false
        renderBspNode(gameState, gameState.episodeMap.rootNodeId)
        user3DScreen.createBitmap()
    }
    Image(
        modifier = Modifier.fillMaxWidth().aspectRatio(SCREEN_ASPECT),
        bitmap = user3dBitmap.asImageBitmap(),
        contentDescription = null,
    )
}