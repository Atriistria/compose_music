package com.atri.composemusic.core.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T : Any> NavGraphBuilder.verticalComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        // 1. 进场动画：从下往上滑 + 淡入
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(400)
            ) + fadeIn(tween(400))
        },
        // 2. 离场动画（去更深层页面，比如从播放器点进评论）：
        // 建议只是变暗淡出，不要滑走，否则会觉得播放器“跑了”
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        // 3. 返回进场动画（从更深层页面回来）：
        popEnterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        // 4. 返回离场动画（点击返回键/关闭）：从上往下滑走（关键）
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(400)
            ) + fadeOut(tween(400))
        },
        content = content
    )
}