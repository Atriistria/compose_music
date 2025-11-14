package com.atri.composemusic.feature.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.media.session.MediaButtonReceiver
import com.atri.composemusic.R
import com.atri.composemusic.core.data.repository.MusicPlayRepository
import com.atri.composemusic.core.model.Song
import com.atri.composemusic.util.MLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOTIFICATION_ID = 1
private const val NOTIFICATION_CHANNEL_ID = "player_channel"
private const val NOTIFICATION_CHANNEL_NAME = "Player Controls"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class PlayerService: LifecycleService() {

    @Inject
    lateinit var repository: MusicPlayRepository

    private lateinit var mediaSession: MediaSessionCompat

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, "PlayerService").apply {
            setCallback(mediaSessionCallback)
            isActive = true
        }

        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
        startForeground(NOTIFICATION_ID, createNotification())

        lifecycleScope.launch {
            repository.currentPosition.collectLatest { position ->
                updatePlaybackState(
                    if (repository.isPlaying.value) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,position,
                    if (repository.isPlaying.value) 1.0f else 0f
                )
            }
        }

    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
            val audioUri = "${Environment.getExternalStorageDirectory()}/卡音/Music/tenchou (8级别).flac".toUri()
            val duration = getAudioDuration(audioUri) ?: 0
            val song = Song(2, "攀升", "攀升", "", duration, audioUri)

            updateMetadata(song)
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
            startForeground(NOTIFICATION_ID, createNotification(song))
            if (repository.currentPlayingSong.value != null) {
                repository.resume()
            } else {
                repository.play(song)
            }
        }

        override fun onPause() {
            super.onPause()
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, createNotification())
            repository.pause()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            repository.seekTo(pos)
        }

        override fun onStop() {
            super.onStop()
            MLog.e("onStop")
            stopSelf()
        }


    }

    private fun updatePlaybackState(state: Int, duration: Long = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, playbackSpeed: Float = 0f) {
        val playbackStateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_STOP or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
            .setState(state, duration, playbackSpeed, SystemClock.elapsedRealtime())
        mediaSession.setPlaybackState(playbackStateBuilder.build())
    }

    private fun createNotification(song: Song ?= null): Notification {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(song?.title ?: "未知")
            .setContentText(song?.artist ?: "未知艺术家")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1, 2, 3, 4)

        builder.setStyle(mediaStyle)
            .addAction(
                R.drawable.ic_launcher_foreground, "喜欢",
                PendingIntent.getBroadcast(this, 0, Intent(this, PlayerReceiver::class.java).apply {
                    action = "ACTION_FAVORITE"
                }, PendingIntent.FLAG_UPDATE_CURRENT)
            )
            .addAction(
                R.drawable.ic_prev, "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
            .addAction(
                if(!repository.isPlaying.value) R.drawable.ic_play else R.drawable.ic_pause, "PlayPause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
            .addAction(
                R.drawable.ic_next, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
            .addAction(
                R.drawable.ic_launcher_foreground, "歌词",
                PendingIntent.getBroadcast(this, 1, Intent(this, PlayerReceiver::class.java).apply {
                    action = "ACTION_LYRICS"
                }, PendingIntent.FLAG_UPDATE_CURRENT)
            )

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
    }

    private fun getAudioDuration(uri: Uri): Long? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(this, uri)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release()
            duration?.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateMetadata(song: Song) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artist)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration)
            .build()
        mediaSession.setMetadata(metadata)
    }

}