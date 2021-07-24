package com.example.rsshool2021_android_task_pomodoro

import android.os.CountDownTimer

data class MyTimer(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean,
    var allMs: Long,
    var count: CountDownTimer?
    )

