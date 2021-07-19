package com.example.rsshool2021_android_task_pomodoro

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AnimationDrawable
import android.media.*
import android.os.Build
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity.FILL
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import kotlin.properties.Delegates


class TimerHolder (
    private val binding: TimerItemBinding,
    val listeners: TimerListeners
): RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun bind(myTimer: MyTimer) {
        binding.timeView.text = myTimer.currentMs.displayTime()
             if (myTimer.isStarted) {
            startTimer(myTimer)
        }else {
                 stopTimer(myTimer)
             }

        Log.d("MyLog","fun bind")
        initButtonsListeners(myTimer)

    }

    private fun initButtonsListeners(myTimer: MyTimer) {
        binding.btControl.setOnClickListener {

                Log.d("MyLog","Таймер номер ${myTimers.size}")

            if (myTimer.isStarted) {
                listeners.stop(myTimer.id, myTimer.currentMs)
                Log.d("MyLog","Pause")
            } else {

                Log.d("MyLog","Timer Id = ${myTimer.id}")
                listeners.start(myTimer.id)

                Log.d("MyLog","Start")
            }

        }

        binding.deleteBt.setOnClickListener {
            listeners.delete(myTimer.id)
        }
    }
    lateinit var animation: ObjectAnimator


    private fun startTimer(myTimer: MyTimer) {
        binding.btControl.text = "PAUSE"


        timer?.cancel()
        timer = getCountDownTimer(myTimer)
        timer?.start()


        if(myTimer.currentMs != 0L) binding.indicator.isInvisible = false
        (binding.indicator.background as? AnimationDrawable)?.start()



        binding.circularProgressbarOne.setPeriod(myTimer.allMs)
     //   binding.circularProgressbarTwo.setPeriod(myTimer.allMs)

        binding.circularProgressbarOne.setCurrent(myTimer.currentMs)
     //   binding.circularProgressbarTwo.setCurrent(myTimer.currentMs)




        Log.d("MyLog", "осталось ${myTimer.currentMs}")


    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun stopTimer(myTimer: MyTimer) {

        binding.btControl.text = "START"
        timer?.cancel()


        binding.indicator.isInvisible = true
        (binding.indicator.background as? AnimationDrawable)?.stop()

        binding.circularProgressbarOne.setPeriod(myTimer.allMs)
        binding.circularProgressbarOne.setCurrent(myTimer.currentMs)

        }


    private fun getCountDownTimer(myTimer: MyTimer): CountDownTimer {

       return object : CountDownTimer(myTimer.currentMs.toString().toLong(), 1000) {


        //   val leftTime = myTimer.currentMs + System.currentTimeMillis()


            override fun onTick(timerTime: Long) {
                Log.d("MyLog",myTimer.isStarted.toString())
            //    myTimer.currentMs = (leftTime - System.currentTimeMillis())

                Log.d("MyLog",binding.btControl.text.toString())
                Log.d("MyLog","alternative count ${myTimer.currentMs}}")

                    myTimer.currentMs = timerTime

                    Log.d("MyLog","Tick ${myTimer.currentMs.toString()}")
                binding.timeView.text = myTimer.currentMs.displayTime()
                //    binding.timeView.text = timerTime.displayTime()
                    binding.circularProgressbarOne.setCurrent(myTimer.currentMs)

            }


           override fun onFinish() {
               binding.indicator.isInvisible = true

            Log.d("MyLog","display ${binding.timeView.text}")

                val beeper = MediaPlayer.create(itemView.context, R.raw.beep)
                beeper.start()

            myTimer.currentMs = 0L
               binding.circularProgressbarOne.setCurrent(myTimer.currentMs)
            //   binding.circularProgressbarTwo.setCurrent(myTimer.currentMs)

               binding.timeView.text = myTimer.currentMs.displayTime()
               Log.d("MyLog","End time ${myTimer.currentMs.toString()}")

           }

       }

       }


    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return TimerAdapter.START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60
        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
        }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) "$count"
        else "0$count"
    }
}
