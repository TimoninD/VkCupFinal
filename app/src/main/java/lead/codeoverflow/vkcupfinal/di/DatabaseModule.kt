package lead.codeoverflow.vkcupfinal.di

import androidx.room.Room
import lead.codeoverflow.vkcupfinal.model.local.Database
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DatabaseModule {
    val module = module {
        single {
            Room.databaseBuilder(androidContext(), Database::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<Database>().podcastDao() }
        single { get<Database>().infoDao() }
    }
}