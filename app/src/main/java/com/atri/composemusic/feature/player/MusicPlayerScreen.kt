package com.atri.composemusic.feature.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atri.composemusic.navigation.NavigationViewModel

@Composable
fun MusicPlayerScreen(
    navigationViewModel: NavigationViewModel,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val pageCount = 3
    val pageState = rememberPagerState(initialPage = 1) { pageCount }

    val gradientPurple = Color(0xFF76944E) // 底部
    val gradientBlue = Color(0xFF8F9E6E)   // 顶部
    val gradientColors = remember { listOf(gradientBlue, gradientPurple) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {
        // --- 主体内容 ---
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> { /* 歌词页 (透明) */ }
                1 -> MusicPlayerScreen(state, viewModel::dispatch) // MusicPlayer 也是透明的
                2 -> { /* 推荐页 (透明) */ }
            }
        }

        // --- 悬浮在上方的 UI ---

        // 顶部自定义栏，用 Row 来实现
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding() // 适配状态栏，避免内容顶到最上面
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(onClick = { navigationViewModel.navigateBack() }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Back", tint = Color.White)
            }

            // 菜单按钮
            IconButton(onClick = { /* TODO: Show menu */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
            }
        }

        // 页面指示器
        PagerIndicator(
            pagerState = pageState,
            pageCount = pageCount,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding() // 同样需要适配状态栏
                .padding(top = 24.dp) // 给指示器一个合适的上边距
        )
    }
}

@Composable
fun PagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index

            val width by animateDpAsState(
                targetValue = if (isSelected) 16.dp else 4.dp,
                // animationSpec 可以自定义动画效果，比如速度
                animationSpec = tween(durationMillis = 300),
                label = "PagerIndicatorWidth"
            )

            val color by animateColorAsState(
                targetValue = if (isSelected) activeColor else inactiveColor,
                animationSpec = tween(durationMillis = 300),
                label = "PagerIndicatorColor"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = width, height = 4.dp) // 使用动画化的宽度
                    .clip(CircleShape)
                    .background(color) // 使用动画化的颜色
            )

            // --- 动画修改结束 ---
        }
    }
}

@Composable
fun MusicPlayerScreen(
    state: MusicPlayerUiState,
    onIntent: (MusicPlayerIntent) -> Unit
) {
    //专辑封面区域的颜色
    val albumGradientStart = Color(0xFFEBF0A8)
    val albumGradientEnd = Color(0xFF9FDF8F)

    // 进度条颜色
    val progressStartColor = Color(0xFFF8DECC)
    val progressEndColor = Color(0xFFE7D9C5)

    // 播放/暂停按钮的背景色
    val playButtonColor = Color(0xFF3D80FF)

    val infiniteTransition = rememberInfiniteTransition(label = "MusicPlayerTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 15000,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .align(Alignment.Center)
        ) {
            // 专辑封面
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(albumGradientStart, albumGradientEnd)
                        )
                    )
                    .align(Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 歌曲信息
            state.playlistState.let {
                Text(
                    text = it.currentSong?.title ?: "未知",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.currentSong?.artist ?: "未知",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(48.dp))

            // 进度条
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(state.hasDuration),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDuration(state.currentSong?.duration ?: 0L),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = state.hasDuration.toFloat(),
                    onValueChange = { newValue -> onIntent(MusicPlayerIntent.SeekTo(newValue.toLong())) },
                    valueRange = 0f..(state.currentSong?.duration?.toFloat() ?: 1f),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = progressStartColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                        thumbColor = progressEndColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 控制按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Queue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = { onIntent(MusicPlayerIntent.Previous) }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Previous",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(playButtonColor)
                        .clickable { onIntent(MusicPlayerIntent.PlayPause) }
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.PlayArrow else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp)
                    )
                }

                IconButton(onClick = { onIntent(MusicPlayerIntent.Next) }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Next",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )

                }

                IconButton(onClick = { onIntent(MusicPlayerIntent.Like) }) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

        }
    }

}

fun formatDuration(durationMillis: Long): String {
    if (durationMillis < 0) return "--:--"
    val totalSeconds = (durationMillis / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

