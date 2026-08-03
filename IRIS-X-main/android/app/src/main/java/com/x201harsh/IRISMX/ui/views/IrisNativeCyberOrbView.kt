package com.x201harsh.IRISMX.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random

class IrisNativeCyberOrbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val viewScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val orbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00F0FF")
        textSize = 26f
        typeface = Typeface.MONOSPACE
    }

    private var rotationAngle = 0f
    private var pulseScale = 1.0f

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        startRenderLoop()
    }

    private fun startRenderLoop() {
        viewScope.launch {
            while (isActive) {
                rotationAngle += 1.2f
                pulseScale = 1.0f + 0.12f * sin(Math.toRadians(rotationAngle.toDouble())).toFloat()
                invalidate()
                delay(16)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) * 0.25f * pulseScale

        // Draw Dark Background
        canvas.drawColor(Color.parseColor("#0B0418"))

        // Glowing Core Orb
        glowPaint.color = Color.parseColor("#7000FF")
        glowPaint.alpha = 140
        canvas.drawCircle(cx, cy, radius, glowPaint)

        // Orbital Concentric Rings
        orbPaint.color = Color.parseColor("#00F0FF")
        orbPaint.strokeWidth = 4f
        orbPaint.alpha = 220
        canvas.drawCircle(cx, cy, radius + 20f, orbPaint)

        orbPaint.color = Color.parseColor("#FF007A")
        orbPaint.strokeWidth = 3f
        canvas.drawCircle(cx, cy, radius + 40f, orbPaint)

        // Rotating Cyber Crosshairs
        val rad = Math.toRadians(rotationAngle.toDouble()).toFloat()
        val x1 = cx + cos(rad) * (radius + 50f)
        val y1 = cy + sin(rad) * (radius + 50f)
        val x2 = cx - cos(rad) * (radius + 50f)
        val y2 = cy - sin(rad) * (radius + 50f)
        canvas.drawLine(x1, y1, x2, y2, orbPaint)

        canvas.drawText("KOTLIN CYBER ORB • 60 FPS GPU HW ACCEL", 30f, 50f, textPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }
}
