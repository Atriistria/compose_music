package com.atri.composemusic.feature.myfavor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.atri.composemusic.R
import com.atri.composemusic.feature.myfavor.model.FavorAlbumItem

@Composable
fun AlbumPageScreen(
    onClick: () -> Unit
) {
    val list = listOf(FavorAlbumItem(1,"curve","curve"))
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.wrapContentHeight()) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {  }) {
                Icon(Icons.Default.MoreVert, contentDescription = "sort")
            }
        }
        LazyColumn(Modifier.fillMaxWidth()) {
            items(list, key = { it.id }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }) {
                    AsyncImage(
                        R.drawable.ic_launcher_foreground,
                        contentDescription = "album cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(start = 6.dp)
                    )
                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp)) {
                        Text(text = it.albumName, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                        Text(text = it.artist, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(top = 4.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "click", modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }
}
