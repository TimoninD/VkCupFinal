package lead.codeoverflow.vkcupfinal.entity.core

import lead.codeoverflow.vkcupfinal.utils.parseDuration
import tw.ktrssreader.kotlin.model.channel.ITunesChannelData
import tw.ktrssreader.kotlin.model.item.ITunesItemData

data class PodcastData(
    val title: String,
    val owner: String,
    val image: String,
    val playlist: List<PlayData>
)

data class PlayData(
    val guid: String,
    val url: String,
    val duration: String
)

fun ITunesChannelData.toPodcastData() = PodcastData(
    title = this.title.orEmpty(),
    owner = this.author.orEmpty(),
    image = this.image?.url.orEmpty(),
    playlist = this.items?.map { it.toPlayData() } ?: listOf()
)

fun ITunesItemData.toPlayData() = PlayData(
    guid = this.guid?.value.orEmpty(),
    url = this.enclosure?.url.orEmpty(),
    duration = this.duration.orEmpty()
)