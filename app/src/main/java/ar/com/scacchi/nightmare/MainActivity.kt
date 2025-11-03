package ar.com.scacchi.nightmare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.color.PlayPay
import ar.com.scacchi.nightmare.data.WadInfo
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.endoom.Endoom
import java.nio.ByteBuffer
import kotlin.repeat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val file = resources.openRawResource(R.raw.doom)

        val buffer = ByteBuffer.wrap(file.readBytes())

        val wadInfo = WadInfo(buffer)

        println("*** WadInfo: $wadInfo")
        println("Identification: ${String(wadInfo.identification)}")
        println("NumLumps: ${wadInfo.numLumps}")
        println("InfoTableOfs: ${wadInfo.infoTableOfs}")

        println("File: $file")
        println("File size: ${file.available()}")


        buffer.position(wadInfo.infoTableOfs.toInt())
        val lumpMap = mutableMapOf<String, FileLump>()


        repeat(wadInfo.numLumps.toInt()) {
            val lump = FileLump(buffer)
            val name = lump.name.filter { it != 0.toByte() }.joinToString("") {
                it.toInt().toChar().toString()
            }
            lumpMap[name] = lump
            println("** Lump:")
            println("Filepos: ${lump.filepos}")
            println("Size: ${lump.size}")
            println("Name: ${String(lump.name.filter { it != 0.toByte() }.toByteArray())}")
        }

        val playPalLump = lumpMap["PLAYPAL"]

        buffer.position(playPalLump!!.filepos.toInt())

        val playPay = lumpMap["PLAYPAL"]?.let { lump ->
            PlayPay.createFromLump(lump, buffer)
        }

        val colorMap = lumpMap["COLORMAP"]?.let {
            ColorMap.createColorMap(it, buffer)
        }

        val endoom = lumpMap["ENDOOM"]?.let {
            Endoom.createFromLump(it, buffer)
        }
        for (y in 0..24) {
            for (x in 0..79) {
                print(endoom?.get(x, y)?.letter)
            }
            println()
        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Main()

            }
        }
    }
}

@Composable
fun Main() {
    Text("Hello World!")
}