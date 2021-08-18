package lead.codeoverflow.vkcupfinal.model

import android.app.Application
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface AudioPlayerController {
    fun init()
    fun addPlaylist(playlist: List<String>)
    fun getCurrentPosition(): Int
    fun play(seekToMs: Int? = null)
    fun changeSpeed(speed: Int)
    fun pause()
    fun changeVolume(volumePercent: Int)
    fun playNextEpisode()
    fun playPrevEpisode()
    fun close()
    fun observeProgress(): Flow<Long>
    fun seekTo(duration: Long)
}

class AudioPlayerControllerImpl(private val application: Application) : AudioPlayerController {
    private var simpleExoPlayer: SimpleExoPlayer? = null

    private var playerListener: PlayerListener? = null

    override fun init() {
        val renderersFactory = DefaultRenderersFactory(application)
        renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        val trackSelector: TrackSelector = DefaultTrackSelector(application)

        simpleExoPlayer = SimpleExoPlayer.Builder(application, renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
    }

    override fun addPlaylist(playlist: List<String>) {
        val mediaItemList = playlist.map {
            MediaItem.fromUri(it)
        }
        simpleExoPlayer?.addMediaItems(mediaItemList)
        simpleExoPlayer?.prepare()
    }

    override fun getCurrentPosition() = simpleExoPlayer?.currentPeriodIndex ?: 0

    override fun play(seekToMs: Int?) {
        val position =
            if (seekToMs == null && simpleExoPlayer?.playbackState == Player.STATE_ENDED) {
                0
            } else {
                seekToMs
            }

        position?.let {
            simpleExoPlayer?.seekTo(it.toLong())
        }
        simpleExoPlayer?.playWhenReady = true
    }

    override fun changeSpeed(speed: Int) {
        val pitch = simpleExoPlayer?.playbackParameters?.pitch
        pitch?.let {
            simpleExoPlayer?.playbackParameters = PlaybackParameters(speed.toFloat(), it)
        }
    }

    override fun pause() {
        playerListener?.onEvent(PlayerEvent.PAUSE)
        simpleExoPlayer?.playWhenReady = false
    }

    override fun changeVolume(volumePercent: Int) {
        simpleExoPlayer?.volume = volumePercent.toFloat() / 100
    }

    override fun playNextEpisode() {
        if (getCurrentPosition() + 1 < simpleExoPlayer?.mediaItemCount ?: 0) {
            simpleExoPlayer?.seekToNext()
        }
    }

    override fun playPrevEpisode() {
        if (getCurrentPosition() > 0) {
            simpleExoPlayer?.seekToPrevious()
        }
    }

    override fun close() {
        playerListener?.onEvent(PlayerEvent.STOP)
        simpleExoPlayer?.release()
    }

    override fun observeProgress() = flow {
        while (true) {
            delay(500)
            val currentTime =
                withContext(Dispatchers.Main) { simpleExoPlayer?.currentPosition ?: 0L }
            emit(currentTime)
        }
    }

    override fun seekTo(duration: Long) {
        simpleExoPlayer?.seekTo(duration)
    }

}