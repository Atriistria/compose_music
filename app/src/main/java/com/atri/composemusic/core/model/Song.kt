package com.atri.composemusic.core.model

import android.net.Uri
import com.atri.composemusic.feature.myfavor.model.FavorSongItem

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: Uri
)
