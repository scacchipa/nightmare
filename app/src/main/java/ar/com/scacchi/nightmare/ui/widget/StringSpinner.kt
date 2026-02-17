package ar.com.scacchi.nightmare.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.com.scacchi.nightmare.R
import kotlin.math.max
import kotlin.math.min

@Composable
fun StringSpinner(
    position: Int,
    values:  List<String>,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(48.dp)
                .clickable(
                    enabled = values.isNotEmpty() && position > 0,
                    onClick = { onValueChange(max(0, position - 1)) }
                ),
            painter = painterResource(R.drawable.left_direction_square_svgrepo_com),
            contentDescription = null
        )
        Text(
            text = if (values.isNotEmpty()) values[position] else "",
            fontSize = 32.sp
        )
        Image(
            modifier = Modifier
                .size(48.dp)
                .clickable(
                    enabled = values.size > position,
                    onClick = { onValueChange(min(position + 1, values.size)) }
                ),
            painter = painterResource(R.drawable.right_direction_square_svgrepo_com),
            contentDescription = null
        )
    }
}

@Preview(
    widthDp = 360,
    heightDp = 200,
    showBackground = true
)
@Composable
fun StringSpinnerPreview() = StringSpinner(
    position = 2,
    values = listOf("elem 1", "elem 2", "elem 3"),
    onValueChange = { }
)