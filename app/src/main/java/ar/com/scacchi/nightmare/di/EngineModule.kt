package ar.com.scacchi.nightmare.di

import android.content.Context
import ar.com.scacchi.nightmare.source.wad.WadManager
import ar.com.scacchi.nightmare.engine.Engine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EngineModule {

    @Singleton
    @Provides
    fun provideEngine(
        wadManager: WadManager,
        @ApplicationScope externalScope: CoroutineScope,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @ApplicationContext context: Context,
    ): Engine = Engine(
        wadManager = wadManager,
        externalScope = externalScope,
        ioDispatcher = ioDispatcher,
        context = context,
    )
}