package lead.codeoverflow.vkcupfinal.entity.core

import com.google.gson.annotations.SerializedName
import lead.codeoverflow.vkcupfinal.entity.Sex

data class JsonResult(
    val reactions: List<ReactionData>,
    val episodes: List<Episode>?
    )

data class ReactionData(
    @SerializedName("reaction_id") val id: Int,
    val emoji: String,
    val description: String
)

data class Episode(
    val guid: String,
    @SerializedName("") val defaultReactions: List<Int>,
    val timedReactions: List<TimedReaction>,
    val statistics: List<Statistic>
)

data class TimedReaction(
    val from: String,
    val to: String,
    @SerializedName("available_reactions") val availableReactions: List<Int>
)

data class Statistic(
    val time: Long,
    @SerializedName("reaction_id") val reactionId: Int,
    val sex: Sex,
    val age: Int,
    @SerializedName("city_id") val cityId: Int
)