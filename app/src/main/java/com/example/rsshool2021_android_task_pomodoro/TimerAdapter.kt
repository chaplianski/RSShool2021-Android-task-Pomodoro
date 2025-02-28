package com.example.rsshool2021_android_task_pomodoro

import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import java.util.*

class TimerAdapter(val listeners: TimerListeners): ListAdapter<MyTimer,TimerHolder> (itemComparator){

          override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
            return TimerHolder(binding, listeners)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun onBindViewHolder(holder: TimerHolder, position: Int) {

            holder.bind(getItem(position))
            if(getItem(position).isStarted) {
                holder.setIsRecyclable(false)}
             }

     override fun getItemId(position: Int): Long {
        return getItemId(position)
    }

     companion object {
        const val START_TIME = "00:00:00"
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