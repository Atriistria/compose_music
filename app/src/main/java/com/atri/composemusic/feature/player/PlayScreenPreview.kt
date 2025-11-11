package com.atri.composemusic.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.atri.composemusic.core.model.Song

@Preview
@Composable
fun MusicPlayerScreenPreview() {
    val song = Song(id = 1, title = "Preview Song", artist = "Preview Artist", duration = 240000L, album = "", uri = "".toUri())
    val state = MusicPlayerUiState(
        playlistState = MusicPlayerUiState.PlaylistState(
            songs = listOf(song),
            currentIndex = 0
        ),
    )
    MusicPlayerScreen(state) {}
}
