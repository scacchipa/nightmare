package ar.com.scacchi.nightmare.ui.dev

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import ar.com.scacchi.nightmare.ui.dev.endoom.EndoomWidget
import ar.com.scacchi.nightmare.ui.dev.flat.FlatWidget
import ar.com.scacchi.nightmare.ui.dev.palette.PlayPalWidget
import ar.com.scacchi.nightmare.ui.dev.patch.PatchWidget
import ar.com.scacchi.nightmare.ui.dev.sprite.SpriteWidget
import ar.com.scacchi.nightmare.ui.dev.texture.TextureWidget

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
            var showEndoom  by remember { mutableStateOf(false) }
            var showPlayPal by remember { mutableStateOf(false) }
            var showColorMap by remember { mutableStateOf(false) }
            var showPatch by remember { mutableStateOf(false) }
            var showFlat by remember { mutableStateOf(false) }
            var showSprite by remember { mutableStateOf(false) }
            var showTextureMap by remember { mutableStateOf(false) }
            Column {
                Text(
                    modifier = Modifier.clickable(onClick = { showEndoom = !showEndoom }),
                    text = "ENDOOM",
                    fontSize = 20.sp,
                )
                if (showEndoom) EndoomWidget()

                Text(
                    modifier = Modifier.clickable(onClick = { showPlayPal = !showPlayPal }),
                    text = "PLAYPAL",
                    fontSize = 20.sp,
                )
                if (showPlayPal) PlayPalWidget()
                Text(
                    modifier = Modifier.clickable(onClick = { showColorMap = !showColorMap }),
                    text = "COLORMAP",
                    fontSize = 20.sp,
                )
                if (showColorMap) ColorMap()
                Text(
                    modifier = Modifier.clickable(onClick = { showPatch = !showPatch }),
                    text = "PATCH",
                    fontSize = 20.sp,
                )
                if (showPatch) PatchWidget()
                Text(
                    modifier = Modifier.clickable(onClick = { showFlat = !showFlat }),
                    text = "FLAT",
                    fontSize = 20.sp,
                )
                if (showFlat) FlatWidget()
                Text(
                    modifier = Modifier.clickable(onClick = { showSprite = !showSprite }),
                    text = "SPRITE",
                    fontSize = 20.sp,
                )
                if (showSprite) SpriteWidget()
                Text(
                    modifier = Modifier.clickable(onClick = { showTextureMap = !showTextureMap }),
                    text = "TEXTURE",
                    fontSize = 20.sp,
                )
                if (showTextureMap) TextureWidget()
            }
        }
    }
}

