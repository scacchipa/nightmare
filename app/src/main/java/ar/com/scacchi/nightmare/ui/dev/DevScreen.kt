package ar.com.scacchi.nightmare.ui.dev

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.ui.dev.colormap.ColorMap
import ar.com.scacchi.nightmare.ui.dev.palette.PlayPal

@Composable
fun DevScreen() {
    var devEnable by remember { mutableStateOf(false) }
    Image(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = { devEnable = !devEnable }),
        painter = painterResource(R.drawable.setting_svgrepo_com),
        contentDescription = "setting",
    )
    if (devEnable) {
        Column {
            var showPlayPal by remember { mutableStateOf(false) }
            var showColorMap by remember { mutableStateOf(false) }
            Row {
                Text(
                    modifier = Modifier
                        .clickable(onClick = { showPlayPal = !showPlayPal }),
                    text = "PLAYPAL",
                    fontSize = 20.sp,
                )
                Text(
                    modifier = Modifier
                        .clickable(onClick = { showColorMap = !showColorMap }),
                    text = "COLORMAP",
                    fontSize = 20.sp,
                )
            }
            if (showPlayPal) {
                PlayPal()
            }
            if (showColorMap) {
                ColorMap()
            }
        }
    }
}

