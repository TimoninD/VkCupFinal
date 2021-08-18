package lead.codeoverflow.vkcupfinal.entity.core

import androidx.room.*
import com.google.gson.annotations.SerializedName
import lead.codeoverflow.vkcupfinal.entity.Sex
import lead.codeoverflow.vkcupfinal.model.local.TimedReactionConverter

data class JsonResult(
    val reactions: List<ReactionData>?,
    val episodes: List<Episode>?
)

data class ReactionData(
    @SerializedName("reaction_id") val id: Int,
    val emoji: String,
    val description: String
)

@Entity
data class Episode(
    var link: String = "",
    @PrimaryKey var guid: String = "",
    @Ignore
    @SerializedName("default_reactions") var defaultReactions: List<Int> = listOf(),
    @TypeConverters(TimedReactionConverter::class)
    @SerializedName("timed_reactions")
    @ColumnInfo(name = "timed_reactions") var timedReactions: List<TimedReaction> = listOf(),
    var statistics: List<Statistic> = listOf()
)

@Entity
data class TimedReaction(
    @PrimaryKey var from: String = "",
    var to: String = "",
    @Ignore
    @SerializedName("available_reactions") var availableReactions: List<Int> = listOf()
)

@Entity
data class Statistic(
    @PrimaryKey var time: Long = 0L,
    @SerializedName("reaction_id") var reactionId: Int = 0,
    var sex: Sex = Sex.MALE,
    var age: Int = 0,
    @SerializedName("city_id") var cityId: Int = 0
)