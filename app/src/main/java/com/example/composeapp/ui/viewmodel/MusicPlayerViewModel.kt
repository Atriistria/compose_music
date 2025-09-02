package com.example.composeapp.ui.viewmodel

import com.example.composeapp.data.repository.PlayerRepository
import com.example.composeapp.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository

) : BaseViewModel<MusicPlayerUiState, MusicPlayerIntent, MusicPlayerEffect>(MusicPlayerUiState()) {
    override fun onIntent(intent: MusicPlayerIntent) {
        when(intent) {
            is MusicPlayerIntent.PlayPause -> {
                setState { it.copy(isPlaying = !it.isPlaying) }
            }
            is MusicPlayerIntent.Next -> {
               setState {
                   if (it.currentSong == null || it.currentIndex == it.songs.size - 1) {
                       it
                   }else {
                       it.copy(currentIndex = (it.currentIndex + 1) % it.songs.size)
                   }
               }

            }

            is MusicPlayerIntent.Previous -> {
                setState {
                    if (it.currentSong == null || it.currentIndex == 0) {
                        it
                    }else {
                        it.copy(currentIndex = (it.currentIndex - 1) % it.songs.size)
                    }
                }
            }
            is MusicPlayerIntent.Like -> {
                setState { it.copy(like = !it.like) }
            }

            is MusicPlayerIntent.ModeChange -> {
                setState { it.copy(mode = it.mode.next()) }
            }

            is MusicPlayerIntent.SeekTo -> {
                setState {
                    val duration = it.hasDuration.takeIf { d -> d > 0 } ?: 1L
                    it.copy(progress = intent.position.toFloat() / duration)
                }
            }

            is MusicPlayerIntent.VolumeChange -> {
                setState { it.copy(volume = intent.volume) }
            }
        }
    }

}

enum class MusicPlayerMode {
    Normal, Random, Repeat;

    fun next(): MusicPlayerMode = when (this) {
        Normal -> Random
        Random -> Repeat
        Repeat -> Normal
    }
}

data class MusicPlayerUiState(
    val isPlaying: Boolean = false,
    val songs: List<Song> = emptyList(),
    val currentIndex: Int = 0,
    val progress: Float = 0f,
    val mode: MusicPlayerMode = MusicPlayerMode.Normal,
    val hasDuration: Long = 0,
    val like: Boolean = false,
    val volume: Float = 0.8f
) : UiState {
    val currentSong: Song? get() = songs.getOrNull(currentIndex)
}

sealed interface MusicPlayerIntent: UiIntent {
    data object PlayPause: MusicPlayerIntent
    data object Next: MusicPlayerIntent
    data object Previous: MusicPlayerIntent
    data object Like: MusicPlayerIntent
    data object ModeChange: MusicPlayerIntent
    data class SeekTo(val position: Long): MusicPlayerIntent
    data class VolumeChange(val volume: Float): MusicPlayerIntent
}

sealed interface MusicPlayerEffect: UiEffect {
    // TODO define effects if needed
}