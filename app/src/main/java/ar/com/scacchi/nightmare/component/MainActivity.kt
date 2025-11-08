package ar.com.scacchi.nightmare.component

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameEngine = Engine.createFrom(this, R.raw.doom)

//        buffer.position(playPalLump!!.filePos.toInt())
//
//        val playPay = lumpMap["PLAYPAL"]?.let { lump ->
//            PlayPay.createFromLump(lump, buffer)
//        }
//
//        val colorMap = lumpMap["COLORMAP"]?.let {
//            ColorMap.createColorMap(it, buffer)
//        }
//
//        val endoom = lumpMap["ENDOOM"]?.let {
//            Endoom.createFromLump(it, buffer)
//        }
//        for (y in 0..24) {
//            for (x in 0..79) {
//                print(endoom?.get(x, y)?.letter)
//            }
//            println()
//        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                MainScreen()
            }
        }
    }
}
