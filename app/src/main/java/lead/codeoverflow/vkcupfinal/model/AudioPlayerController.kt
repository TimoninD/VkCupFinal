package lead.codeoverflow.vkcupfinal.model

import android.app.Application
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AudioPlayerController(private val application: Application) {
    private val simpleExoPlayer by lazy {
        val renderersFactory = DefaultRenderersFactory(application)
        renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        val trackSelector: TrackSelector = DefaultTrackSelector(application)

        SimpleExoPlayer.Builder(application, renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
    }

    private var playerListener: PlayerListener? = null

    fun addPlaylist(playlist: List<String>) {
        val mediaItemList = playlist.map {
            MediaItem.fromUri(it)
        }
        simpleExoPlayer.addMediaItems(mediaItemList)
        simpleExoPlayer.prepare()
    }

    fun getCurrentPosition() = simpleExoPlayer.currentPeriodIndex

    fun play(seekToMs: Int? = null) {
        val position =
            if (seekToMs == null && simpleExoPlayer.playbackState == Player.STATE_ENDED) {
                0
            } else {
                seekToMs
            }

        position?.let {
            simpleExoPlayer.seekTo(it.toLong())
        }
        simpleExoPlayer.playWhenReady = true
    }

    fun changeSpeed(speed: Int) {
        val pitch = simpleExoPlayer.playbackParameters.pitch
        simpleExoPlayer.playbackParameters = PlaybackParameters(speed.toFloat(), pitch)
    }

    fun pause() {
        playerListener?.onEvent(PlayerEvent.PAUSE)
        simpleExoPlayer.playWhenReady = false
    }

    fun close() {
        playerListener?.onEvent(PlayerEvent.STOP)
        simpleExoPlayer.release()
    }

    fun observeProgress() = flow {
        while (true) {
            delay(500)
            val currentTime = withContext(Dispatchers.Main){simpleExoPlayer.currentPosition}
            emit(currentTime)
        }
    }

}