package lead.codeoverflow.vkcupfinal.model.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lead.codeoverflow.vkcupfinal.entity.core.PlayData
import lead.codeoverflow.vkcupfinal.entity.core.Statistic
import lead.codeoverflow.vkcupfinal.entity.core.TimedReaction
import java.lang.reflect.Type


class PlayDataConverter {
    @TypeConverter
    fun fromPlayDataList(playDataList: List<PlayData?>?): String? {
        if (playDataList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<PlayData?>?>() {}.type
        return gson.toJson(playDataList, type)
    }

    @TypeConverter
    fun toPlayDatasList(playDataList: String?): List<PlayData>? {
        if (playDataList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<PlayData?>?>() {}.type
        return gson.fromJson<List<PlayData>>(playDataList, type)
    }
}

class TimedReactionConverter {
    @TypeConverter
    fun fromTimerReactionList(timedReactionList: List<TimedReaction?>?): String? {
        if (timedReactionList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<TimedReaction?>?>() {}.type
        return gson.toJson(timedReactionList, type)
    }

    @TypeConverter
    fun toTimedReactionList(timedReactionList: String?): List<TimedReaction>? {
        if (timedReactionList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<TimedReaction?>?>() {}.type
        return gson.fromJson<List<TimedReaction>>(timedReactionList, type)
    }
}

class StatisticConverter {
    @TypeConverter
    fun fromStatisticList(statisticList: List<Statistic?>?): String? {
        if (statisticList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Statistic?>?>() {}.type
        return gson.toJson(statisticList, type)
    }

    @TypeConverter
    fun toStatisticList(statisticList: String?): List<Statistic>? {
        if (statisticList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Statistic?>?>() {}.type
        return gson.fromJson<List<Statistic>>(statisticList, type)
    }
}