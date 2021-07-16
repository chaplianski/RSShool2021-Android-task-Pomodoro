package com.example.rsshool2021_android_task_pomodoro

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

val myTimers = mutableListOf<MyTimer>()

class MainActivity : AppCompatActivity(), TimerListeners, LifecycleObserver {
    val timerAdapter = TimerAdapter(this)
    val myTimers = mutableListOf<MyTimer>()
    private var startTime = 0L

    private lateinit var binding: ActivityMainBinding
 //   val adapter = TimerAdapter()
    private var nextId = 0

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

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
                                false,
                                binding.enterMinute.text.toString().toLong() * 1000 * 60

                            )

                        )
                        timerAdapter.submitList(myTimers.toList())
                        binding.enterMinute.text = null
                    }
                }

            startTime = System.currentTimeMillis()

           // lifecycleScope.launch(Dispatchers.Main) {
            //    while (true) {

                    // был timerview  поставил addTimer
                //    binding.addTimer.text = (System.currentTimeMillis() - startTime).displayTime()
            //        delay(INTERVAL)
            //    }
           // }




    }

    override fun start(id: Int) {
        changeTimer(id, null, true)

        for (i in 0..myTimers.size-1){
            if (i != id)
            stop(myTimers[i].id, myTimers[i].currentMs)
            //isStarted = false
            Log.d("MyLog","Значение таймера ${i} = ${myTimers[i].isStarted.toString()}")
           // changeTimer(i, currentMs, false)
        }
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
        Log.d("MyLog","Stop")
    }

    override fun delete(id: Int) {
        myTimers.remove(myTimers.find { it.id == id })
        timerAdapter.submitList(myTimers.toList())

    }


    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<MyTimer>()
        myTimers.forEach {
            if (it.id == id) {
                newTimers.add(MyTimer(it.id, currentMs ?: it.currentMs, isStarted,it.allMs))
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
        Log.d("MyLog", "Lifecycle.Event.ON_STOP")
        for (i in 0..myTimers.size - 1) {
            if (myTimers[i].isStarted == true) {
                val startIntent = Intent(this, ForegroundService::class.java)
                startIntent.putExtra(COMMAND_ID, COMMAND_START)
                startTime = myTimers[i].currentMs
                startIntent.putExtra(STARTED_TIMER_TIME_MS, startTime)


                Log.d("MyLog","Таймер показывает ${myTimers[i].currentMs}")
                Log.d("MyLog","Таймер показывает ${startTime.displayTime()}")
                //     startTime)
                startService(startIntent)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("MyLog","Lifecycle.Event.ON_START")
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    private companion object {
        private const val INTERVAL = 10L
    }


}
