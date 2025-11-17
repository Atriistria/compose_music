package com.atri.composemusic.feature.musicplay.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.atri.composemusic.core.data.repository.MusicPlayRepository
import com.atri.composemusic.util.MLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: MusicPlayRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "ACTION_FAVORITE" -> {
                repository.resume()
                MLog.e("ACTION_FAVORITE")
            }
            "ACTION_LYRICS" -> {
                //repository.playNext()
                MLog.e("ACTION_LYRICS")
            }
        }
    }
}