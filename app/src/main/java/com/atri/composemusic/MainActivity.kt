package com.atri.composemusic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.atri.composemusic.core.data.util.NetworkMonitor
import com.atri.composemusic.core.ui.theme.AppTheme
import com.atri.composemusic.feature.musicplay.service.PlayerService
import com.atri.composemusic.ui.CmApp
import com.atri.composemusic.ui.rememberCmAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        enableEdgeToEdge()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.onEach {
                    uiState = it
                }.collect()
            }
        }

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1001
            )
        } else {

        }

        val serviceIntent = Intent(this, PlayerService::class.java)
        startForegroundService(serviceIntent)

        splashScreen.setKeepOnScreenCondition {
            uiState is MainActivityUiState.Loading
        }

        setContent {
            AppTheme {
                val appState = rememberCmAppState(
                    networkMonitor = networkMonitor
                )
                CmApp(appState)
            }
        }
    }
}