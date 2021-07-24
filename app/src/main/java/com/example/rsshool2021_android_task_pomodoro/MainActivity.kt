package com.example.rsshool2021_android_task_pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), TimerListeners, LifecycleObserver {
    val timerAdapter = TimerAdapter(this)
    val myTimers = mutableListOf<MyTimer>()
    private var startTime = 0L

    private lateinit var binding: ActivityMainBinding
    private var nextId = 0

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_doofi_skull_icon)

        binding.rcView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = timerAdapter
        }

                binding.addTimer.setOnClickListener {
                    if (binding.enterMinute.text?.isNotEmpty() == true) {
                        myTimers.add(
                            MyTimer(
                                nextId++,
                                binding.enterMinute.text.toString().toLong() * 1000 * 60,
                                false,
                                binding.enterMinute.text.toString().toLong() * 1000 * 60,
                                null
                            )
                        )
                        timerAdapter.submitList(myTimers.toList())
                        binding.enterMinute.text = null
                    }
                }
        }

    override fun start(id: Int) {
        for (i in 0..myTimers.size-1) {
            if (myTimers[i].id != id && myTimers[i].isStarted){
                stop(myTimers[i].id, myTimers[i].currentMs)
                myTimers[i].count?.cancel()
                myTimers[i].count?.onFinish()
            }
        }
        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
        timerAdapter.notifyDataSetChanged()
    }

    override fun delete(id: Int) {
        myTimers.remove(myTimers.find { it.id == id })
        timerAdapter.submitList(myTimers.toList())
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<MyTimer>()
        myTimers.forEach {
            if (it.id == id) {
                newTimers.add(MyTimer(it.id, currentMs ?: it.currentMs, isStarted,it.allMs,it.count))
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        myTimers.clear()
        myTimers.addAll(newTimers)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        for (i in 0..myTimers.size - 1) {
            if (myTimers[i].isStarted == true) {
                val startIntent = Intent(this, ForegroundService::class.java)
                startIntent.putExtra(COMMAND_ID, COMMAND_START)
                startTime = myTimers[i].currentMs
                startIntent.putExtra(STARTED_TIMER_TIME_MS, startTime)
                startService(startIntent)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }
}
