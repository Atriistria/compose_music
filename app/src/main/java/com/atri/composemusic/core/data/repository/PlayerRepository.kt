package com.atri.composemusic.core.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.atri.composemusic.core.common.Dispatcher
import com.atri.composemusic.core.common.CmDispatcher
import com.atri.composemusic.core.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerRepository {
    val isPlaying: StateFlow<Boolean>
    val currentPosition: StateFlow<Long>
    val totalDuration: StateFlow<Long>
    val currentVolume: StateFlow<Float>
    val playbackState: StateFlow<Int> // Player.STATE_IDLE, BUFFERING, READY, ENDED

    fun play(song: Song)

    fun resume()

    fun pause()

    fun seekTo(position: Long)

    fun setVolume(volume: Float)

}

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(CmDispatcher.Main) private val mainDispatcher: kotlinx.coroutines.CoroutineDispatcher,
) : PlayerRepository {

    private val exoPlayer = ExoPlayer.Builder(context).build()

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> get() = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    override val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow(0L)
    override val totalDuration: StateFlow<Long> = _totalDuration.asStateFlow()

    private val _currentVolume = MutableStateFlow(0.8f)
    override val currentVolume: StateFlow<Float> = _currentVolume.asStateFlow()

    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    override val playbackState: StateFlow<Int> = _playbackState.asStateFlow()

    private val scope = CoroutineScope(mainDispatcher + SupervisorJob())

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    startPositionUpdates()
                } else {
                    stopPositionUpdates()
                }
            }
        })
    }

    override fun play(song: Song) {
        exoPlayer.setMediaItem(MediaItem.fromUri(song.uri))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun resume() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _currentPosition.value = position
    }

    override fun setVolume(volume: Float) {
        val clampedVolume = volume.coerceIn(0.0f, 1.0f)
        exoPlayer.volume = clampedVolume
        _currentVolume.value = clampedVolume
    }

    private var positionUpdateJob: Job? = null

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            while (true) {
                _currentPosition.value = exoPlayer.currentPosition
                delay(1000) // 每秒更新一次
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
    }

}