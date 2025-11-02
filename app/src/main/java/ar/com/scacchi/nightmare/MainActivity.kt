package ar.com.scacchi.nightmare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val file = resources.openRawResource(R.raw.doom)

        val x = file.bufferedReader()

        println("File: $file")
        println("File size: ${file.available()}")

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