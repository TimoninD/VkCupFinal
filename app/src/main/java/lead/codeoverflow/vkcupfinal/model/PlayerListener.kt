package lead.codeoverflow.vkcupfinal.model

interface PlayerListener {
    fun onEvent(event: PlayerEvent)
    fun onPlayerStarted()
    fun onPlayerStopped()
}

enum class PlayerEvent {
    PLAY,
    PAUSE,
    STOP,
    FINISH,
}