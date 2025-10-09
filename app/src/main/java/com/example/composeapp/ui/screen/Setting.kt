package com.example.composeapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class SettingItem(
    val id: Int,
    val title: String,
    val sub: String,
    val onClick: () -> Unit
) {
    override fun hashCode(): Int {
        return id
    }
}

@Composable
fun SettingScreen() {
    val list = listOf(
        SettingItem(1, "账号安全", "修改密码、绑定手机") { println("点击了账号安全") },
        SettingItem(2, "通知设置", "消息推送与静音设置") { println("点击了通知设置") },
        SettingItem(3, "隐私", "控制谁可以看到你的信息") { println("点击了隐私") },
        SettingItem(4, "关于我们", "版本号、用户协议") { println("点击了关于我们") },
        SettingItem(5, "清除缓存", "释放存储空间") { println("点击了清除缓存") },
        SettingItem(6, "退出登录", "安全退出当前账号") { println("点击了退出登录") },)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp),
        userScrollEnabled = true
    ) {
        items(list, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ){
                SettingRow(item)
            }

        }
    }

}

@Composable
fun SettingRow(item: SettingItem) {
    val titleStyle = MaterialTheme.typography.titleMedium
    val bodyStyle = MaterialTheme.typography.bodySmall
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, style = titleStyle)
            Text(text = item.sub, style = bodyStyle, color = Color.Gray)
        }

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Arrow",
            tint = Color.Gray
        )
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen()
}