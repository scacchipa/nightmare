package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.settings.SCREEN_ASPECT
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.settings.SCREEN_WIDTH

@Composable
fun ColumnScope.User3dViewRender(
    modifier: Modifier = Modifier,
    gameState: GameState,
    wadManager: WadManager,
) {

    var scaleX = 1080.0F
    var scaleY = 675.0F

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(SCREEN_ASPECT)
            .onGloballyPositioned { coordinates ->
                scaleX = coordinates.size.width / SCREEN_WIDTH
                scaleY = coordinates.size.height / SCREEN_HEIGHT
            }
    ) {
//        if (gameState.lumpDirectory.lumpEntries.isEmpty()) return@Canvas
        withTransform(
            {
                scale(scaleX, scaleY, Offset(0f, 0f))
            },
        ) {
            drawCircle(
                color = Color.Magenta,
                radius = 10f,
                center = Offset(160f, 100f)
            )

            val user3dViewDrawScope = User3dViewDrawScope(this, gameState, wadManager)

            with(user3dViewDrawScope) {
                isTraverseBsp = false

                renderBspNode(gameState, gameState.episodeMap.rootNodeId)
            }
        }
    }
}