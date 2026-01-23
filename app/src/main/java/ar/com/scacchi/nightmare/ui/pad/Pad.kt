package ar.com.scacchi.nightmare.ui.pad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ar.com.scacchi.nightmare.R

@Composable
fun Pad(
    playerActionSetChanged: (Set<PlayerAction>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        var playerActionState by remember { mutableStateOf(emptySet<PlayerAction>()) }

        fun onKeyStateChanged(buttonState: ButtonState, action: PlayerAction) {
            println("onKeyStateChanged: $buttonState, $action")
            val newPressedKeySet = when (buttonState) {
                ButtonState.PRESSED -> playerActionState + action
                ButtonState.RELEASED -> playerActionState - action
            }

            playerActionState = newPressedKeySet
            playerActionSetChanged(newPressedKeySet)
        }

        Row {
            StateButton(
                iconId = R.drawable.turn_left_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.TURN_LEFT) },
            )
            StateButton(
                iconId = R.drawable.arrow_left_top_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_LEFT_FORWARD) },
            )
            StateButton(
                iconId = R.drawable.arrow_up_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_FORWARD) },
            )
            StateButton(
                iconId = R.drawable.arrow_right_top_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_RIGHT_FORWARD) },
            )
            StateButton(
                iconId = R.drawable.turn_right_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.TURN_RIGHT) },
            )
        }
        Row {
            StateButton(
                iconId = R.drawable.arrow_left_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_LEFT) },
            )
            StateButton(
                iconId = R.drawable.arrow_left_bottom_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_LEFT_BACKWARD) },
            )
            StateButton(
                iconId = R.drawable.arrow_down_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_BACKWARD) },
            )
            StateButton(
                iconId = R.drawable.arrow_right_bottom_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_RIGHT_BACKWARD) },
            )
            StateButton(
                iconId = R.drawable.arrow_right_svgrepo_com,
                onStateChanged = { onKeyStateChanged(it, PlayerAction.MOVE_RIGHT) },
            )
        }
    }
}

@Preview(
    widthDp = 360,
    heightDp = 360,
    showBackground = true
)
@Composable
fun PadPreview() = Pad(
    playerActionSetChanged = {
        println(it)
    }
)


