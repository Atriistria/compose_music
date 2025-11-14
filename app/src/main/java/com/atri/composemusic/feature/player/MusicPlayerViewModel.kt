package com.atri.composemusic.feature.player

import androidx.lifecycle.viewModelScope
import com.atri.composemusic.core.data.repository.MusicPlayRepository
import com.atri.composemusic.core.model.Song
import com.atri.composemusic.core.ui.BaseViewModel
import com.atri.composemusic.core.ui.UiEffect
import com.atri.composemusic.core.ui.UiIntent
import com.atri.composemusic.core.ui.UiState
import com.atri.composemusic.util.MLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val repository: MusicPlayRepository
) : BaseViewModel<MusicPlayerUiState, MusicPlayerIntent, MusicPlayerEffect>(MusicPlayerUiState()) {

    override fun onIntent(intent: MusicPlayerIntent) {
        viewModelScope.launch {
            when (intent) {
                is MusicPlayerIntent.Play -> {
                    repository.play(intent.song)
                }

                is MusicPlayerIntent.PlayPause -> {
                    val currentState = uiState.value
                    if (currentState.isPlaying) {
                        MLog.e("暂停播放")
                        repository.pause()
                    } else {
                        currentState.currentSong?.let {
                            repository.play(it)
                        }
                    }
                }

                is MusicPlayerIntent.Next -> {
                    updatePlaylistState { playlistState ->
                        if (playlistState.songs.isEmpty() || playlistState.currentIndex == playlistState.songs.size - 1) {
                            playlistState
                        } else {
                            val newIndex =
                                (playlistState.currentIndex + 1) % playlistState.songs.size
                            playlistState.copy(currentIndex = newIndex, isLiked = false)
                        }
                    }
                    // 切换到下一首时重置播放进度
                    updatePlaybackState { it.copy(progress = 0f, currentPosition = 0L) }
                }

                is MusicPlayerIntent.Previous -> {
                    updatePlaylistState { playlistState ->
                        if (playlistState.songs.isEmpty() || playlistState.currentIndex == 0) {
                            playlistState
                        } else {
                            val newIndex = if (playlistState.currentIndex - 1 < 0) {
                                playlistState.songs.size - 1
                            } else {
                                playlistState.currentIndex - 1
                            }
                            playlistState.copy(currentIndex = newIndex, isLiked = false)
                        }
                    }
                    // 切换到上一首时重置播放进度
                    updatePlaybackState { it.copy(progress = 0f, currentPosition = 0L) }
                }

                is MusicPlayerIntent.Like -> {
                    updatePlaylistState { it.copy(isLiked = !it.isLiked) }
                }

                is MusicPlayerIntent.ModeChange -> {
                    updateControlState { it.copy(mode = it.mode.next()) }
                }

                is MusicPlayerIntent.SeekTo -> {
                    updatePlaybackState { playbackState ->
                        val duration = playbackState.totalDuration.takeIf { it > 0 } ?: 1L
                        playbackState.copy(
                            progress = intent.position.toFloat() / duration,
                            currentPosition = intent.position
                        )
                    }
                }

                is MusicPlayerIntent.VolumeChange -> {
                    updateControlState { it.copy(volume = intent.volume) }
                }
            }
        }
    }

    // 更新播放列表状态
    private fun updatePlaylistState(transform: (MusicPlayerUiState.PlaylistState) -> MusicPlayerUiState.PlaylistState) {
        setState { currentState ->
            currentState.copy(playlistState = transform(currentState.playlistState))
        }
    }

    // 更新播放状态
    private fun updatePlaybackState(transform: (MusicPlayerUiState.PlaybackState) -> MusicPlayerUiState.PlaybackState) {
        setState { currentState ->
            currentState.copy(playbackState = transform(currentState.playbackState))
        }
    }

    // 更新控制状态
    private fun updateControlState(transform: (MusicPlayerUiState.ControlState) -> MusicPlayerUiState.ControlState) {
        setState { currentState ->
            currentState.copy(controlState = transform(currentState.controlState))
        }
    }

    // 加载播放列表
    fun loadPlaylist(songs: List<Song>, startIndex: Int = 0) {
        updatePlaylistState { it.copy(songs = songs, currentIndex = startIndex) }
        updatePlaybackState { it.copy(totalDuration = songs.getOrNull(startIndex)?.duration ?: 0L) }
    }

    // 更新播放进度
    fun updateProgress(currentPosition: Long, totalDuration: Long) {
        updatePlaybackState {
            it.copy(
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                progress = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f
            )
        }
    }

}

// 播放模式
enum class MusicPlayerMode {
    Normal, Random, Repeat;

    fun next(): MusicPlayerMode = when (this) {
        Normal -> Random
        Random -> Repeat
        Repeat -> Normal
    }
}

// 播放器状态数据类
data class MusicPlayerUiState(
    val playlistState: PlaylistState = PlaylistState(),
    val playbackState: PlaybackState = PlaybackState(),
    val controlState: ControlState = ControlState()
) : UiState {
    val currentSong: Song? get() = playlistState.currentSong
    val isPlaying: Boolean get() = playbackState.isPlaying
    val progress: Float get() = playbackState.progress
    val hasDuration: Long get() = playbackState.totalDuration
    val like: Boolean get() = playlistState.isLiked
    val volume: Float get() = controlState.volume

    data class PlaylistState(
        val songs: List<Song> = emptyList(),
        val currentIndex: Int = 0,
        val isLiked: Boolean = false
    ) {
        val currentSong: Song? get() = songs.getOrNull(currentIndex)
    }

    data class PlaybackState(
        val isPlaying: Boolean = false,
        val progress: Float = 0f,
        val currentPosition: Long = 0L, // 新增：精确的当前位置
        val totalDuration: Long = 0L,
        val bufferedPercentage: Float = 0f // 新增：缓冲进度
    )

    data class ControlState(
        val volume: Float = 0.8f,
        val mode: MusicPlayerMode = MusicPlayerMode.Normal,
    )
}

sealed interface MusicPlayerIntent : UiIntent {
    data class Play(val song: Song): MusicPlayerIntent
    data object PlayPause : MusicPlayerIntent
    data object Next : MusicPlayerIntent
    data object Previous : MusicPlayerIntent
    data object Like : MusicPlayerIntent
    data object ModeChange : MusicPlayerIntent
    data class SeekTo(val position: Long) : MusicPlayerIntent
    data class VolumeChange(val volume: Float) : MusicPlayerIntent
}

sealed interface MusicPlayerEffect : UiEffect {

}