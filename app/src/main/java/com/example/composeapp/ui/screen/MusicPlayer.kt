package com.example.composeapp.ui.screen

import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.model.Song


sealed class MusicPlayerEvent {
    data class PlayPause(val isPlaying: Boolean) : MusicPlayerEvent()
    data class Next(val song: Song) : MusicPlayerEvent()
    data class Previous(val song: Song) : MusicPlayerEvent()
    data class Like(val song: Song) : MusicPlayerEvent()
    data class VolumeChange(val volume: Float) : MusicPlayerEvent()
    data class SeekTo(val position: Long) : MusicPlayerEvent()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    curSong: Song?,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onLikeClick: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSeekTo: (Long) -> Unit,
    curPosition: Float,
    totalDuration: Float,
    curVolume: Float,
    isPlaying: Boolean
) {

    val gradientPurple = Color(0xFF7A40F2) // 底部紫色背景
    val gradientBlue = Color(0xFF3F80FF)   // 顶部蓝色背景

    //专辑封面区域的颜色
    val albumGradientStart = Color(0xFFFF9A8B)
    val albumGradientEnd = Color(0xFFFFD1AD)

    // 进度条颜色
    val progressStartColor = Color(0xFFF0985C)
    val progressEndColor = Color(0xFFEEAD4E)

    // 播放/暂停按钮的背景色
    val playButtonColor = Color(0xFF3D80FF)

    val infiniteTransition = rememberInfiniteTransition(label = "MusicPlayerTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                delayMillis = 10000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(gradientBlue, gradientPurple)
                )
            )
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = "播放器", color = Color.White) },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
                    .clip(RoundedCornerShape(16.dp))
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
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            curSong?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 进度条
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(curPosition.toLong()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDuration(totalDuration.toLong()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = curPosition,
                    onValueChange = { newValue -> onSeekTo(newValue.toLong()) },
                    valueRange = 0f..totalDuration,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = progressStartColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                        thumbColor = progressEndColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Queue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onPreviousClick) {
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
                        .clickable { onPlayPauseClick() }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp)
                    )
                }

                IconButton(onClick = onNextClick) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Next",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )

                }

                IconButton(onClick = onLikeClick) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Volume",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )

                Slider(
                    value = curVolume,
                    onValueChange = { newValue -> onVolumeChange(newValue) },
                    valueRange = 0f..1f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                        thumbColor = Color.Transparent
                    )
                )
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

@Preview
@Composable
fun MusicPlayerScreenPreview() {
    MusicPlayerScreen(
        curSong = Song(
            id = 1,
            title = "Song Title",
            artist = "Artist Name",
            album = "Album Name",
            duration = 240,
            uri = Uri.parse("https://example.com/song.mp3")
        ),
        onPlayPauseClick = {},
        onNextClick = {},
        onPreviousClick = {},
        onLikeClick = {},
        onVolumeChange = {},
        onSeekTo = {},
        curPosition = 0f,
        totalDuration = 240f,
        curVolume = 0.5f,
        isPlaying = true
    )
}
