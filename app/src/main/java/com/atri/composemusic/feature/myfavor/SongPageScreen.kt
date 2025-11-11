package com.atri.composemusic.feature.myfavor

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atri.composemusic.core.model.Song
import com.atri.composemusic.feature.myfavor.model.FavorSongItem
import com.atri.composemusic.feature.player.MusicPlayerIntent
import com.atri.composemusic.feature.player.MusicPlayerViewModel
import com.atri.composemusic.navigation.NavigationViewModel
import com.atri.composemusic.navigation.Screen
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SongPageScreen(
    navigationViewModel: NavigationViewModel,
    musicPlayerViewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val song = Song(1, "攀升", "攀升", "", 1,
        "https://com.atri.composemusic/raw/song".toUri())
    val itemList = listOf(FavorSongItem(1,1,song))
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "play all")
            }
            Text(text = "全部播放", modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {  }) {
                Icon(Icons.Default.MoreVert, contentDescription = "sort")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(itemList, key = { it.id }){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigationViewModel.navigateTo(Screen.MusicPlayer.route)
                            musicPlayerViewModel.dispatch(MusicPlayerIntent.Play(it.song))
                        }
                        .padding(start = 15.dp)
                ) {
                    Text(text = it.ordinal.toString(), modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.padding(6.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(4.dp)
                    ) {
                        Text(text = it.song.title)
                        Text(text = it.song.artist, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "next")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "favor")
                    }
                }
            }
        }

    }
}