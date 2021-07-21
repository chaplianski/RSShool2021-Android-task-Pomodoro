package com.example.rsshool2021_android_task_pomodoro

data class MyTimer(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean,
    var allMs: Long
    )

