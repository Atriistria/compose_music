package com.atri.composemusic.core.data.repository

import com.atri.composemusic.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

interface MusicPlayRepository {

    val currentSongFlow: Flow<Song?>  // 新增
    val playListFlow: StateFlow<List<Song>>
    val currentIndexFlow: StateFlow<Int>
    val currentPosition: StateFlow<Long>
    val isPlaying: StateFlow<Boolean>
    val currentPlayingSong: StateFlow<Song?>

    suspend fun playNewList(songs: List<Song>, startIndex: Int = 0)
    suspend fun playAt(index: Int)
    suspend fun playNext()
    suspend fun playPrevious()

    fun play(song: Song)
    fun pause()
    fun resume()
    fun seekTo(position: Long)
    fun changeMode()
}

@Singleton
class MusicPlayRepositoryImpl @Inject constructor(
    private val playerListRepository: PlayerListRepository,
    private val playerRepository: PlayerRepository
) : MusicPlayRepository {

    override val playListFlow: StateFlow<List<Song>>
        get() = playerListRepository.playList

    override val isPlaying: StateFlow<Boolean>
        get() = playerRepository.isPlaying

    override val currentIndexFlow: StateFlow<Int>
        get() = playerListRepository.currentIndex

    override val currentSongFlow: Flow<Song?>
        get() = playerListRepository.currentSongFlow

    override val currentPosition: StateFlow<Long>
        get() = playerRepository.currentPosition

    override val currentPlayingSong: StateFlow<Song?>
        get() = playerListRepository.currentPlayingSong

    override suspend fun playNewList(songs: List<Song>, startIndex: Int) {
        playerListRepository.playNewList(songs, startIndex)
        playerRepository.play(songs[startIndex])
    }

    override suspend fun playAt(index: Int) {
        playerListRepository.playAt(index)
        playerListRepository.currentSongFlow.firstOrNull()?.let { playerRepository.play(it) }
    }

    override suspend fun playNext() {
        playerListRepository.playNext()
        playerListRepository.currentSongFlow.firstOrNull()?.let { playerRepository.play(it) }
    }

    override suspend fun playPrevious() {
        playerListRepository.playPrevious()
        playerListRepository.currentSongFlow.firstOrNull()?.let { playerRepository.play(it) }
    }

    override fun play(song: Song) {
        playerRepository.play(song)
        playerListRepository.playNewList(listOf(song), 0)
    }

    override fun pause() = playerRepository.pause()
    override fun resume() = playerRepository.resume()
    override fun seekTo(position: Long) = playerRepository.seekTo(position)
    override fun changeMode() = playerListRepository.changeMode()

    suspend fun playCurrentSong() {
        currentSongFlow.firstOrNull()?.let { playerRepository.play(it) }
    }

}