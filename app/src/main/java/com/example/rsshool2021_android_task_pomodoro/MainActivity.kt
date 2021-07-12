package com.example.rsshool2021_android_task_pomodoro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import java.util.*

val myTimers = mutableListOf<MyTimer>()

class MainActivity : AppCompatActivity(), TimerListeners {
    val timerAdapter = TimerAdapter(this)
    val myTimers = mutableListOf<MyTimer>()

    private lateinit var binding: ActivityMainBinding
 //   val adapter = TimerAdapter()
    private var nextId = 0

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                                false
                            )

                        )
                        timerAdapter.submitList(myTimers.toList())
                        binding.enterMinute.text = null
                    }
                }
    }

    override fun start(id: Int) {
        changeTimer(id, null, true)
       //    Log.d("MyLog",com.example.rsshool2021_android_task_pomodoro.myTimers[id].isStarted.toString())

    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun delete(id: Int) {
        myTimers.remove(myTimers.find { it.id == id })
        timerAdapter.submitList(myTimers.toList())

    }
    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<MyTimer>()
        myTimers.forEach {
            if (it.id == id) {
                newTimers.add(MyTimer(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        timerAdapter.submitList(newTimers)
        myTimers.clear()
        myTimers.addAll(newTimers)
    }
}
