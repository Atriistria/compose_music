package com.atri.composemusic.feature.musicplay

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atri.composemusic.R

@Composable
fun MiniPlayerOverlay(navigateTo: () -> Unit) {
    val bottomNavBarHeight = 0.dp
    val playerOffset = -bottomNavBarHeight / 1.3f

    var isExpanded by remember { mutableStateOf(false) }
    val height by animateDpAsState(
        targetValue = if (isExpanded) 128.dp else 56.dp,
        animationSpec = tween(durationMillis = 500)
    )
    val imageSize by animateDpAsState(
        targetValue = if (isExpanded) 64.dp else 48.dp,
        animationSpec = tween(durationMillis = 500)
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .padding(8.dp)
            .offset(y = playerOffset)
            .height(height)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { navigateTo() },
                onLongClick = { isExpanded = !isExpanded },
            )
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            // 专辑封面
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Album Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.TopStart)
            )
            ExpandedContent(modifier = Modifier.align(Alignment.BottomStart),isExpanded = isExpanded)
        }
    }
}

@Composable
fun ExpandedContent(modifier: Modifier,isExpanded: Boolean) {
    if (!isExpanded) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(56.dp)) // 留出 Image 的空间

            // 歌曲信息
            Text(
                text = "测试",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            // 控制按钮
            IconButton(onClick = { /* Play/Pause */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Pause")
            }
            IconButton(onClick = { /* Show Playlist */ }) {
                Icon(Icons.Default.Lock, contentDescription = "Playlist")
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "测试",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { /* Play/Pause */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Pause")
            }
            IconButton(onClick = { /* Show Playlist */ }) {
                Icon(Icons.Default.List, contentDescription = "Playlist")
            }
        }
    }
}

@Preview
@Composable
fun MiniPlayerPreview() {
    MiniPlayerOverlay {}
}