package com.atri.composemusic.core.data.repository

import com.atri.composemusic.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerListRepository {
    val playList: StateFlow<List<Song>>
    val currentIndex: StateFlow<Int>
    val currentSongFlow: Flow<Song?>

    suspend fun playNewList(songs: List<Song>, startIndex: Int = 0)
    suspend fun playAt(index: Int)
    suspend fun playNext()
    suspend fun playPrevious()
    fun changeMode()
}

@Singleton
class PlayerListRepositoryImpl @Inject constructor() : PlayerListRepository {

    private val _playList: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    private val _currentIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    override val playList: StateFlow<List<Song>>
        get() = _playList

    override val currentIndex: StateFlow<Int>
        get() = _currentIndex

    override val currentSongFlow: Flow<Song?> = combine(_playList, _currentIndex) { list, index ->
        list.getOrNull(index)
    }

    override suspend fun playNewList(songs: List<Song>, startIndex: Int) {
        _playList.value = songs
        _currentIndex.value = startIndex.coerceIn(songs.indices)
    }
    override suspend fun playAt(index: Int) {
        if (index in _playList.value.indices) {
            _currentIndex.value = index
        }
    }

    override suspend fun playNext() {
        _currentIndex.value = (_currentIndex.value + 1).coerceAtMost(_playList.value.lastIndex)
    }

    override suspend fun playPrevious() {
        _currentIndex.value = (_currentIndex.value - 1).coerceAtLeast(0)
    }

    // TODO
    override fun changeMode() {

    }

    fun addToPlayList(song: Song) {
        _playList.value += song
    }
}