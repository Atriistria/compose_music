package com.atri.composemusic.feature.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.atri.composemusic.R

@Composable
fun ProfileScreen(
    navigate: () -> Unit
) {
    ProfileScreen(navigate, "")
}

@Composable
private fun ProfileScreen(
    navigate: () -> Unit,
    test: String
) {
    Scaffold(
        topBar = {
            Row{
                Column {
                    Text(text = "我的",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Box(
                        modifier = Modifier
                            .width(32.dp)  // 和内容一样宽
                            .height(2.dp)
                            .background(Color.Green)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    imageVector = Icons.Default.Mail,
                    contentDescription = "讯息"
                )
                Spacer(Modifier.width(14.dp))
                Image(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "设置"
                )
            }
        },
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .verticalScroll(rememberScrollState())
        ) {
            Avatar(navigate)
            Spacer(modifier = Modifier.size(16.dp))
            ProfileSection()
            RecentItem()
        }
    }

}

@Composable
fun Avatar(
    navigate: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = "这是个头像",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Column {
                    Text(
                        text = " 张三",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .clickable { navigate() },
                    )
                    Text("vip", modifier = Modifier.padding(4.dp))
                }

            }
            Row(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text("点击进入我的收藏")
                }
            }

        }
    }
}

@Composable
fun ProfileSection() {
    val actions = listOf(
        Pair("我的收藏", R.drawable.ic_like),
        Pair("本地", R.drawable.ic_like),
        Pair("我的收藏", R.drawable.ic_like),
        Pair("我的收藏", R.drawable.ic_like),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        actions.forEach {  item ->
            ProfileItem(item,Modifier.weight(1f))
        }
    }
}

@Composable
fun ProfileItem(
    item: Pair<String, Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {}
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(item.second),
            contentDescription = "这是个头像",
            contentScale = ContentScale.Fit,
            modifier = Modifier.wrapContentSize()
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = item.first,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}

@Composable
fun RecentItem(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row {
            Text("最近播放")
            Spacer(modifier = Modifier.weight(1f))
            Image(imageVector = Icons.Default.ChevronRight, contentDescription = "")
        }

        LazyRow {
            items(5) { index: Int ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "这是个头像",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen({}, "")
}
