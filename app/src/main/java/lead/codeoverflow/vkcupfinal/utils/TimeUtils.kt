package lead.codeoverflow.vkcupfinal.utils

import okhttp3.internal.toLongOrDefault

private const val MILLS = 1000L
private const val SECOND = 60L * MILLS
private const val MINUTE = SECOND * 60L
private const val HOUR = MINUTE * 60

fun String.parseDuration(): Long {
    val times = split(":")
    return times[0].toLongOrDefault(0) * HOUR +
            times[1].toLongOrDefault(0) * MINUTE +
            times[2].toLongOrDefault(0) * SECOND
}