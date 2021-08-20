package lead.codeoverflow.vkcupfinal.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lead.codeoverflow.vkcupfinal.entity.core.Episode

@Dao
interface InfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEpisodes(episodes: List<Episode>)

    @Query("SELECT * FROM Episode WHERE link = :link")
    fun getAllEpisodesFromLink(link: String): List<Episode>

    @Query("SELECT * FROM Episode WHERE guid = :guid LIMIT 1")
    fun getEpisode(guid: String): Episode?
}