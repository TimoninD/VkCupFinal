package lead.codeoverflow.vkcupfinal

import android.app.Application
import lead.codeoverflow.vkcupfinal.di.AppModule
import lead.codeoverflow.vkcupfinal.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    AppModule.module, ViewModelModule.module
                )
            )
        }
    }
}