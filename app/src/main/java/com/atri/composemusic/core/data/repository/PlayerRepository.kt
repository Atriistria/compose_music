package com.atri.composemusic.core.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.atri.composemusic.core.model.Song
import com.atri.composemusic.feature.player.MusicPlayerUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerRepository {
    val playerStateFlow: StateFlow<MusicPlayerUiState>

    fun play(song: Song)

    fun pause()

    fun seekTo(position: Long)

    fun setVolume(volume: Float)

    fun toggleMode()
}

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : PlayerRepository {

    private val exoPlayer = ExoPlayer.Builder(context).build()

    private val _playerStateFlow = MutableStateFlow(MusicPlayerUiState())
    override val playerStateFlow: StateFlow<MusicPlayerUiState>
        get() = _playerStateFlow

    override fun play(song: Song) {
        exoPlayer.setMediaItem(MediaItem.fromUri(song.uri))
        exoPlayer.prepare()
        exoPlayer.play()
        _playerStateFlow.update { it.copy(playbackState = it.playbackState.copy(isPlaying = true)) }
    }

    override fun pause() {
        exoPlayer.pause()
        _playerStateFlow.update { it.copy(playbackState = it.playbackState.copy(isPlaying = false)) }
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _playerStateFlow.update { currentState ->
            val duration = currentState.playbackState.totalDuration.takeIf { it > 0 } ?: 1L
            currentState.copy(
                playbackState = currentState.playbackState.copy(
                    currentPosition = position,
                    progress = position.toFloat() / duration
                )
            )
        }
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume
        _playerStateFlow.update { it.copy(controlState = it.controlState.copy(volume = volume)) }
    }

    override fun toggleMode() {
        _playerStateFlow.update { currentState ->
            val newMode = currentState.controlState.mode.next()
            currentState.copy(
                controlState = currentState.controlState.copy(mode = newMode)
            )
        }
    }
}