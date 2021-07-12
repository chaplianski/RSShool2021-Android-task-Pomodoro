package com.example.rsshool2021_android_task_pomodoro

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import java.util.*

class TimerAdapter(val listeners: TimerListeners): ListAdapter<MyTimer,TimerHolder> (itemComparator){
    val timerList = arrayListOf<MyTimer>()

          override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            Log.d("MyLog","onCreateViewHolder")
            val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
            return TimerHolder(binding, listeners)
        }

        override fun onBindViewHolder(holder: TimerHolder, position: Int) {
            holder.bind(getItem(position))
            //   notifyDataSetChanged()
            Log.d("MyLog","onBindViewHolder")
            }

     //   override fun getItemCount(): Int {
    //       return myTimers.count()
   //     }

    /*    fun AddTimer(myTimer: MyTimer) {
            timerList.add(myTimer)
            notifyDataSetChanged()
        }*/


        companion object {
        const val START_TIME = "00:00:00"
    //    private const val UNIT_TEN_MS = 10L
    //    private const val PERIOD  = 1000L * 60L * 60L * 24L // Day

        private val itemComparator = object : DiffUtil.ItemCallback<MyTimer>() {

            override fun areItemsTheSame(oldItem: MyTimer, newItem: MyTimer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyTimer, newItem: MyTimer): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }


}