package com.example.composeapp.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.composeapp.ui.viewmodel.NavigationViewModel
import com.example.composeapp.R
import com.example.composeapp.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFavor(
    navigationViewModel: NavigationViewModel
) {

    val pagerState = rememberPagerState(initialPage = 0) {
        3
    }
    val tabs = listOf("歌曲", "专辑", "歌单")
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {},
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "我的收藏",
                        modifier = Modifier.weight(1f),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        )
    }) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }

            }

            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (page) {
                        0 -> SongPage(navigationViewModel)
                        1 -> AlbumPage { }
                        2 -> PlayListPage()
                        else -> SongPage(navigationViewModel)
                    }
                }

            }
        }
    }
}

data class FavorSongItem(
    val id: Int,
    val ordinal: Int,
    val title: String,
    val artist: String
)
@Composable
fun SongPage(
    viewModel: NavigationViewModel
) {
    val itemList = listOf(FavorSongItem(1,1,"攀升","攀升"))
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
                        .clickable { viewModel.navigateTo(Screen.MusicPlayer.route) }
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
                        Text(text = it.title)
                        Text(text = it.artist, fontSize = MaterialTheme.typography.labelSmall.fontSize)
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

data class FavorAlbumItem(
    val id: Int,
    val albumName: String,
    val artist: String
)
@Composable
fun AlbumPage(
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

@Composable
fun PlayListPage() {
    Text(text = "PlayListPage")
}

@Composable
fun ButtonPlay(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { },
        contentAlignment = Alignment.Center
    ){
        Canvas(modifier = Modifier
            .matchParentSize()
            .padding(16.dp)) {
            if (isPlaying) {
                val barWidth = size.width / 3.5f
                val barHeight = size.height
                val cornerRadius = CornerRadius(x = 8f,y = 8f)

                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(x = 0f, y = 0f),
                    size = Size(width = barWidth, height = barHeight),
                    cornerRadius = cornerRadius
                )

                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(x = size.width - barWidth, y = 0f),
                    size = Size(width = barWidth, height = barHeight),
                    cornerRadius = cornerRadius
                )
            }  else {
                // --- 绘制播放图标（同上） ---
                val path = Path().apply {
                    moveTo(x = 0f, y = 0f)
                    lineTo(x = size.width, y = size.height / 2f)
                    lineTo(x = 0f, y = size.height)
                    close()
                }
                drawPath(path = path, color = Color.White)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayPauseButtonPreview() {
    // 使用 remember 来持有状态
    var isPlaying by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ButtonPlay(
            isPlaying = isPlaying,
            onClick = { isPlaying = !isPlaying }, // 点击时，翻转状态
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        // 模拟另一个状态
        ButtonPlay(
            isPlaying = true,
            onClick = { },
            modifier = Modifier.size(60.dp)
        )
    }
}
