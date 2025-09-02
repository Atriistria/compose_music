package com.example.composeapp.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.composeapp.model.Song
import com.example.composeapp.ui.viewmodel.MusicPlayerUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerRepository{
    val playerStateFlow: StateFlow<MusicPlayerUiState>

    suspend fun play(song: Song)

    suspend fun pause()

    suspend fun seekTo(position: Long)

    suspend fun setVolume(volume: Float)

    suspend fun toggleMode()
}

@Singleton
class PlayerRepositoryImpl @Inject constructor(
   @ApplicationContext context: Context,
): PlayerRepository {

    private val exoPlayer = ExoPlayer.Builder(context).build()

    private val _playerStateFlow = MutableStateFlow(MusicPlayerUiState())
    override val playerStateFlow: StateFlow<MusicPlayerUiState>
        get() = _playerStateFlow

    override suspend fun play(song: Song) {
        exoPlayer.setMediaItem(MediaItem.fromUri(song.uri))
        exoPlayer.prepare()
        exoPlayer.play()
        _playerStateFlow.update { it.copy(isPlaying = true) }
    }

    override suspend fun pause() {
        // Implement pause logic
    }

    override suspend fun seekTo(position: Long) {
        // Implement seek logic
    }

    override suspend fun setVolume(volume: Float) {
        // Implement volume change logic
    }

    override suspend fun toggleMode() {
        // Implement mode toggle logic
    }
}