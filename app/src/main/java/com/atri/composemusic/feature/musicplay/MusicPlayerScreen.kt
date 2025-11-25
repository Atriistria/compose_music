package com.atri.composemusic.feature.musicplay

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atri.composemusic.R
import kotlin.math.absoluteValue

@Composable
fun MusicPlayerScreen(
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
                0 -> { }
                1 -> MusicPlayerScreen(state, viewModel::dispatch)
                2 -> { }
            }
        }

        // 顶部自定义栏，用 Row 来实现
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(onClick = {  }) {
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
            // 计算当前指示点相对于当前页面的偏移量
            val pageOffset = ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue

            // 根据偏移量计算宽度和颜色插值因子 (0 = 完全选中, 1 = 完全未选中)
            val interpolationFactor = pageOffset.coerceIn(0f, 1f)

            // 使用插值因子计算实际的宽度和颜色
            val width = lerp(12.dp, 4.dp, interpolationFactor)
            val color = androidx.compose.ui.graphics.lerp(activeColor, inactiveColor, interpolationFactor)

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = width, height = 4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
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
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
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
                .padding(top = 60.dp)
                .align(Alignment.Center)
        ) {
            // 专辑封面
            Box(
                modifier = Modifier
                    .size(340.dp)
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
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter) // 对齐到父容器顶部中心
                        .padding(top = 10.dp)       // 向下移动一点，使其在圆内
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

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
            Spacer(modifier = Modifier.weight(1f))

            // 进度条
            Column(modifier = Modifier.fillMaxWidth()) {
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

                Spacer(modifier = Modifier.height(8.dp))

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

            }

            // 控制按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(onClick = { onIntent(MusicPlayerIntent.Like) }) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = { onIntent(MusicPlayerIntent.Previous) }) {
                    Icon(
                        painterResource(R.drawable.icon_prev),
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
                        painterResource(R.drawable.icon_next),
                        contentDescription = "Next",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )

                }

                IconButton(onClick = {  }) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = "Queue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(36.dp))

        }
    }

}

@SuppressLint("DefaultLocale")
fun formatDuration(durationMillis: Long): String {
    if (durationMillis < 0) return "--:--"
    val totalSeconds = (durationMillis / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

