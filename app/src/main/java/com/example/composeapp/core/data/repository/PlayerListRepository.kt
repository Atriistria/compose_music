package com.example.composeapp.core.data.repository

import com.example.composeapp.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface PlayerListRepository {
    val playListFlow: StateFlow<List<Song>>
    val currentIndexFlow: StateFlow<Int>
    val currentSongFlow: Flow<Song?>

    suspend fun playNewList(songs: List<Song>, startIndex: Int = 0)
    suspend fun playAt(index: Int)
    suspend fun playNext()
    suspend fun playPrevious()
    suspend fun changeMode()
}

@Singleton
class PlayerListRepositoryImpl @Inject constructor() : PlayerListRepository {

    override val playListFlow: StateFlow<List<Song>>
        get() = TODO("Not yet implemented")

    override val currentIndexFlow: StateFlow<Int>
        get() = TODO("Not yet implemented")

    override val currentSongFlow: Flow<Song?>
        get() = TODO("Not yet implemented")

    override suspend fun playNewList(songs: List<Song>, startIndex: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun playAt(index: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun playNext() {
        TODO("Not yet implemented")
    }

    override suspend fun playPrevious() {
        TODO("Not yet implemented")
    }

    override suspend fun changeMode() {
        TODO("Not yet implemented")
    }
}