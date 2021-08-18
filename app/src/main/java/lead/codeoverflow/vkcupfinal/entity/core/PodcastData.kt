package lead.codeoverflow.vkcupfinal.entity.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import tw.ktrssreader.kotlin.model.channel.ITunesChannelData
import tw.ktrssreader.kotlin.model.item.ITunesItemData

@Entity
data class PodcastData(
    @PrimaryKey val link: String,
    val title: String,
    val owner: String,
    val image: String,
    val playlist: List<PlayData>
)

@Entity
data class PlayData(
    @PrimaryKey val guid: String,
    val url: String,
    val duration: String
)

fun ITunesChannelData.toPodcastData(link:String) = PodcastData(
    title = this.title.orEmpty(),
    owner = this.author.orEmpty(),
    image = this.image?.url.orEmpty(),
    link = link,
    playlist = this.items?.map { it.toPlayData() } ?: listOf()
)

fun ITunesItemData.toPlayData() = PlayData(
    guid = this.guid?.value.orEmpty(),
    url = this.enclosure?.url.orEmpty(),
    duration = this.duration.orEmpty()
)