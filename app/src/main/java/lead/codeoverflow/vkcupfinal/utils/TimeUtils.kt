package lead.codeoverflow.vkcupfinal.utils

import okhttp3.internal.toLongOrDefault
import java.util.concurrent.TimeUnit

const val MILLS = 1000L
private const val SECOND = MILLS
private const val MINUTE = SECOND * 60L
private const val HOUR = MINUTE * 60

fun String.parseDuration(): Long {
    return try {
        val times = split(":")
        times[0].toLongOrDefault(0) * HOUR +
                times[1].toLongOrDefault(0) * MINUTE +
                times[2].toLongOrDefault(0) * SECOND
    } catch (t: Throwable) {
        -1L
    }
}

fun Long.extractTime(): String {
    return String.format(
        "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                this
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                this
            )
        )
    )

}