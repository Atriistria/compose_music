package com.atri.composemusic.feature.musicplay

fun interface PlayModeStrategy{
    fun next(currentIndex: Int, size: Int): Int
}

object NormalPlayModeStrategy : PlayModeStrategy {
    override fun next(currentIndex: Int, size: Int): Int {
        return (currentIndex + 1) % size
    }
}

object RandomPlayModeStrategy : PlayModeStrategy {
    private val random = kotlin.random.Random.Default

    override fun next(currentIndex: Int, size: Int): Int {
        return random.nextInt(size)
    }
}

object RepeatPlayModeStrategy : PlayModeStrategy {
    override fun next(currentIndex: Int, size: Int): Int {
        return currentIndex
    }
}