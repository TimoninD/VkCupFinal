package lead.codeoverflow.vkcupfinal.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lead.codeoverflow.vkcupfinal.entity.core.PodcastData

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePodcast(model: PodcastData)

    @Query("SELECT * FROM PodcastData WHERE link = :link LIMIT 1")
    fun getPodcastByLink(link: String): PodcastData?

    @Query("SELECT * FROM PodcastData")
    fun getAllPodcasts(): List<PodcastData>
}