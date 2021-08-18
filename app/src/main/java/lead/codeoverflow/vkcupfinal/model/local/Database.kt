package lead.codeoverflow.vkcupfinal.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lead.codeoverflow.vkcupfinal.entity.core.*

@Database(
    entities = [PodcastData::class, Episode::class, Statistic::class, TimedReaction::class, PlayData::class],
    version = 1
)
@TypeConverters(PlayDataConverter::class, StatisticConverter::class, TimedReactionConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun infoDao(): InfoDao
}