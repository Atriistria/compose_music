package com.atri.composemusic.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Avatar(navigate)
        ProfileItem()
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
            Row {
                Column {
                    Text("点击进入我的收藏")
                }
            }

        }
    }
}

@Composable
fun ProfileItem() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column {
            AsyncImage(
                model = R.drawable.ic_like,
                contentDescription = "这是个头像",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "收藏")
        }

        Column {

        }

    }
}

@Preview(apiLevel = 34)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen({}, "")
}
