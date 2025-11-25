package com.atri.composemusic.feature.myfavourite.model

import com.atri.composemusic.core.model.Song


data class FavorSongItem(
    val id: Int,
    val ordinal: Int,
    val song: Song
)