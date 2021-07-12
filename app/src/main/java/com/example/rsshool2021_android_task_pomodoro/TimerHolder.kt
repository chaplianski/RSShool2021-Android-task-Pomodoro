package com.example.rsshool2021_android_task_pomodoro

import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import kotlin.properties.Delegates


class TimerHolder (
    private val binding: TimerItemBinding,
    val listeners: TimerListeners


): RecyclerView.ViewHolder(binding.root){

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
            //for (n in 0..myTimers.size-1){
                Log.d("MyLog","Таймер номер ${myTimers.size}")
        //        listeners.stop(n, myTimer.currentMs)
        //    }
            if (myTimer.isStarted) {
                listeners.stop(myTimer.id, myTimer.currentMs)
                Log.d("MyLog","Pause")
            } else {

                Log.d("MyLog","Timer Id = ${myTimer.id}")
                listeners.start(myTimer.id)
                Log.d("MyLog","Start")

            }
        }

      //  binding.restartButton.setOnClickListener { listener.reset(myTimer.id) }

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


        binding.indicator.isInvisible = false
        (binding.indicator.background as? AnimationDrawable)?.start()

        binding.circularProgressbar.progress = 0 // Main Progress
        binding.circularProgressbar.secondaryProgress = 100 // Secondary Progress
        binding.circularProgressbar.max = 100 // Maximum Progress
       // binding.circularProgressbar.progressDrawable = drawable

        val m = 100- myTimer.currentMs.toInt()*100/60000
        animation = ObjectAnimator.ofInt (binding.circularProgressbar, "progress",m,100)

        Log.d("MyLog", (100-((myTimer.currentMs*100)/60000)).toString())

        animation.setDuration(myTimer.currentMs.toString().toLong())
    //    Log.d("MyLog",myTimer.currentMs.toString())
        //   animation.setInterpolator(TimeInterpolator 100)
        animation.start()

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun stopTimer(myTimer: MyTimer) {

        binding.btControl.text = "START"
        timer?.cancel()

        binding.indicator.isInvisible = true
        (binding.indicator.background as? AnimationDrawable)?.stop()

        binding.circularProgressbar.progress = 50

            //myTimer.currentMs.toString().toInt() // Main Progress
     //   binding.circularProgressbar.secondaryProgress = 100 // Secondary Progress
     //   binding.circularProgressbar.max = 100 // Maximum Progress
     //   binding.circularProgressbar.progressDrawable = drawable


        //val animation = ObjectAnimator.ofInt (binding.circularProgressbar, "progress", 0, 100)
        //animation.setDuration(1000)
        //animation.setInterpolator(TimeInterpolator 100)
        //animation.pause()

    }


    private fun getCountDownTimer(myTimer: MyTimer): CountDownTimer {

       // return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
        return object : CountDownTimer(myTimer.currentMs.toString().toLong(), 1000) {
       // val interval = UNIT_TEN_MS
          //  var timerTime = myTimer.currentMs.toString().toLong()
            override fun onTick(timerTime: Long) {
           // override fun onTick(millisUntilFinished: Long) {


               //     myTimer.currentMs = timerTime
                 //   myTimer.currentMs -= interval
                    //   myTimer.currentMs += interval
                    Log.d("MyLog","Tick ${myTimer.currentMs.toString()}")

           binding.timeView.text = timerTime.displayTime()}
         //  binding.timeView.text = myTimer.currentMs.displayTime()}
                //binding.timeView.text = myTimer.currentMs.displayTime()



            override fun onFinish() {
                binding.timeView.text = "00:00:00"
                binding.indicator.isInvisible = true
             //   val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
             //   toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,15000)
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

    companion object {
    //    const val START_TIME = "00:00:00:01"
     //   private const val UNIT_TEN_MS = 10L
        private const val UNIT_TEN_MS = 1000L
    }
}