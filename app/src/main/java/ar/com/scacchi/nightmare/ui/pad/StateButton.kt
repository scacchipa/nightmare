package ar.com.scacchi.nightmare.ui.pad

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.com.scacchi.nightmare.R


@Composable
fun StateButton(
    @DrawableRes iconId: Int,
    onStateChanged: (ButtonState) -> Unit
) = Image(
        painter = painterResource(id = iconId),
        contentDescription = null,
        modifier = Modifier
            .size(72.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        onStateChanged(ButtonState.PRESSED)
                        waitForUpOrCancellation()
                        onStateChanged(ButtonState.RELEASED)
                    }
                }
            }
    )

@Preview(
    widthDp = 72,
    heightDp = 72,
    showBackground = true
)
@Composable
fun StateButtonPreview() = StateButton(
    iconId = R.drawable.turn_left_svgrepo_com,
    onStateChanged = {
        println(it)
    }
)
