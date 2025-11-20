package com.atri.composemusic.feature.myfavor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atri.composemusic.navigation.NavigationViewModel
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
            navigationIcon = {
                IconButton(onClick = {
                    navigationViewModel.navigateBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
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

            HorizontalPager(
                state = pagerState,
                key = { index -> tabs[index] },
                beyondViewportPageCount = 1
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (page) {
                        0 -> SongPageScreen(navigationViewModel)
                        1 -> AlbumPageScreen {  }
                        2 -> PlayListPage()
                        else -> SongPageScreen(navigationViewModel)
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
