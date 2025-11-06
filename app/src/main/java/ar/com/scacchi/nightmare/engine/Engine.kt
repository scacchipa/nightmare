package ar.com.scacchi.nightmare.engine;

import android.content.Context
import androidx.annotation.RawRes
import ar.com.scacchi.nightmare.R
import java.nio.ByteBuffer

class Engine(
    val wadData: WadData
) {
    companion object {
        fun createFrom(context: Context, @RawRes wadPath: Int = R.raw.doom): Engine {

            val file = context.resources.openRawResource(wadPath)
            val buffer = ByteBuffer.wrap(file.readBytes())

            return Engine(
                WadData.create(buffer, "E1M1")
            )
        }
    }
}


