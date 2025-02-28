package com.example.rsshool2021_android_task_pomodoro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var periodMs = 0L
    private var currentMs = 0L
    private var color = 0
    private var style = Gravity.FILL
    private val paint = Paint()

    init {
        if (attrs != null) {
            val styledAttrs = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                defStyleAttr,
                0
            )
            color = styledAttrs.getColor(R.styleable.CustomView_custom_color, Color.RED)
            styledAttrs.recycle()
        }

        paint.color = color
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5F
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (periodMs == 0L ) return
        if (periodMs ==  currentMs) return

        val startAngel = ((1-((currentMs % periodMs).toFloat() / periodMs)) * 360)

        canvas.drawArc(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            -90f,
            startAngel,
            true,
            paint
        )

    }

    fun setCurrent(current: Long) {
        currentMs = current
        invalidate()
    }

    fun setPeriod(period: Long) {
        periodMs = period
    }
}