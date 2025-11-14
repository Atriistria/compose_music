package com.atri.composemusic.feature.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    listState: LazyListState
) {
    LazyColumn(state = listState) {
        items(20) { index ->
            Text("Item $index", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
