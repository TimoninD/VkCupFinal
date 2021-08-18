package lead.codeoverflow.vkcupfinal.di

import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.model.AudioPlayerControllerImpl
import lead.codeoverflow.vkcupfinal.model.Prefs
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AppModule {
    val module = module {
        single { Prefs(androidContext()) }
        single<AudioPlayerController> { AudioPlayerControllerImpl(androidApplication()) }
    }
}