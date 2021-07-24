package com.example.rsshool2021_android_task_pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import java.util.*

//val myTimers = mutableListOf<MyTimer>()
//private var timer: CountDownTimer? = null

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

         //   startTime = System.currentTimeMillis()

        }

    override fun start(id: Int) {

        for (i in 0..myTimers.size-1) {
            if (myTimers[i].id != id && myTimers[i].isStarted){
                stop(myTimers[i].id, myTimers[i].currentMs)
                myTimers[i].count?.cancel()
                myTimers[i].count?.onFinish()
                Log.d("MyLog","Отключен таймер ${i} c id ${myTimers[i].id}")
            }
          //  Log.d("MyLog","Значение таймера ${i} c id ${myTimers[i].id} = ${myTimers[i].isStarted}")
        }
     //   timerAdapter.notifyDataSetChanged()

        changeTimer(id, null, true)
        Log.d("MyLog","Значение id $id ")
    }

    override fun stop(id: Int, currentMs: Long) {

        changeTimer(id, currentMs, false)
        Log.d("MyLog","Stop")
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
                Log.d("MyLog","Lifecycle.onAppBackgrounded.End")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("MyLog","Lifecycle.Event.ON_START")
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
        Log.d("MyLog","Lifecycle.onAppForegrounded.End")
    }

    private companion object {
        private const val INTERVAL = 10L
    }


}
