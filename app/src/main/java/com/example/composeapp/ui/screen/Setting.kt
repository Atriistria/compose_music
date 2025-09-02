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
        SettingItem(6, "退出登录", "安全退出当前账号") { println("点击了退出登录") },
        SettingItem(7, "帮助与反馈", "常见问题解答") { println("点击了帮助与反馈") },
        SettingItem(8, "语言设置", "选择应用语言") { println("点击了语言设置") },
        SettingItem(9, "主题设置", "切换应用主题") { println("点击了主题设置") },
        SettingItem(10, "数据同步", "同步应用数据") { println("点击了数据同步") },
        SettingItem(11, "开发者选项", "调试和测试功能") { println("点击了开发者选项") },
        SettingItem(12, "反馈与建议", "提交应用反馈") { println("点击了反馈与建议") },
        SettingItem(13, "版本更新", "检查应用更新") { println("点击了版本更新") },
        SettingItem(14, "账号注销", "永久删除账号") { println("点击了账号注销") },
        SettingItem(15, "应用权限", "管理应用权限") { println("点击了应用权限") },
        SettingItem(16, "快捷方式", "添加桌面快捷方式") { println("点击了快捷方式") },
        SettingItem(17, "软件管理", "管理软件包") { println("点击了软件管理") },
        SettingItem(18, "数据备份", "备份应用数据") { println("点击了数据备份") },
        SettingItem(19, "账号绑定", "绑定社交账号") { println("点击了账号绑定") },
        SettingItem(20, "通知权限", "管理通知权限") { println("点击了通知权限") },
        SettingItem(21, "应用信息", "查看应用版本和大小") { println("点击了应用信息") },
        SettingItem(22, "安全中心", "安全检测和防护") { println("点击了安全中心") },
        SettingItem(23, "账号设置", "修改账号信息") { println("点击了账号设置") },

        )

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