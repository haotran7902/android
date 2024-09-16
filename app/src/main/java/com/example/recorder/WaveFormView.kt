package com.example.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

class WaveFormView(context: Context?, attrs: AttributeSet?): View(context, attrs) {

    private var paint = Paint()
    private var amplitudes = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private var radius = 6f
    private var w = 9f
    private var d = 6f

    private var sw = 0f
    private var sh = 400f

    private var maxSpikes = 0;

    init {
        paint.color = Color.rgb(244, 81, 30)

        sw = resources.displayMetrics.widthPixels.toFloat()

        maxSpikes = (sw / (w+d)).toInt()
    }

    fun addAmplitude(amp: Float){
        println(amp)
        var norm = (amp.toInt() / 20).coerceAtMost(400).toFloat()
        if (amp < 20 && amp > 3) {
            amplitudes.add(amp * 20)
        } else {
            norm = max(norm, 3f)
            amplitudes.add(norm)
        }

        spikes.clear()
        var amps = amplitudes.takeLast(maxSpikes).reversed()
        for(i in amps.indices){
            var left = sw - i*(w+d)
            var top = sh/2 - amps[i]/2
            var right: Float = left + w
            var bottom: Float = top + amps[i]
            spikes.add(RectF(left, top, right, bottom))
        }

        invalidate()
    }

    fun clear(): ArrayList<Float> {
        var amps = amplitudes.clone() as ArrayList<Float>
        amplitudes.clear()
        spikes.clear()
        invalidate()

        return amps
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        spikes.forEach {
            canvas?.drawRoundRect(it, radius, radius, paint)
        }
    }
}