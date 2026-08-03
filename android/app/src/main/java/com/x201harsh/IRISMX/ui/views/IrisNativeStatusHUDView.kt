package com.x201harsh.IRISMX.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.x201harsh.IRISMX.core.IrisNativeEngine
import com.x201harsh.IRISMX.core.NativeTelemetryData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

class IrisNativeStatusHUDView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val viewScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var telemetryData = NativeTelemetryData()
    private var fpsCounter = 60
    private var lastFrameTime = System.nanoTime()

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.parseColor("#00F0FF")
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#120826")
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00F0FF")
        textSize = 32f
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E2E8F0")
        textSize = 26f
        typeface = Typeface.MONOSPACE
    }

    private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF007A")
        textSize = 26f
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        startTelemetrySubscription()
    }

    private fun startTelemetrySubscription() {
        viewScope.launch {
            IrisNativeEngine.telemetryFlow.collectLatest { data ->
                telemetryData = data
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val now = System.nanoTime()
        val deltaMs = (now - lastFrameTime) / 1_000_000f
        lastFrameTime = now
        if (deltaMs > 0) {
            val instantFps = (1000f / deltaMs).toInt()
            fpsCounter = (fpsCounter * 0.9f + instantFps * 0.1f).toInt().coerceIn(30, 120)
        }

        val padding = 20f
        val w = width.toFloat() - padding * 2
        val h = height.toFloat() - padding * 2

        // Draw Container Box with Cyber Corners
        val rect = RectF(padding, padding, padding + w, padding + h)
        canvas.drawRoundRect(rect, 16f, 16f, fillPaint)
        canvas.drawRoundRect(rect, 16f, 16f, borderPaint)

        // Title Header
        canvas.drawText("⚡ IRIS KOTLIN / C++ CORE ENGINE", padding + 25f, padding + 55f, titlePaint)

        // Telemetry Grid Items
        val startY = padding + 105f
        val lineGap = 42f

        val cpuText = String.format(Locale.US, "CPU ENGINE LOAD: %.1f%%", telemetryData.cpuLoad)
        canvas.drawText(cpuText, padding + 25f, startY, textPaint)

        val memMb = telemetryData.allocatedBytes / (1024f * 1024f)
        val memText = String.format(Locale.US, "JNI HEAP ALLOC : %.2f MB", memMb)
        canvas.drawText(memText, padding + 25f, startY + lineGap, textPaint)

        val latencyText = String.format(Locale.US, "C++ LATENCY    : %.2f ms", telemetryData.inferenceLatencyMs)
        canvas.drawText(latencyText, padding + 25f, startY + lineGap * 2, accentPaint)

        val fpsText = "RENDER FPS     : $fpsCounter FPS (GPU HW ACCEL)"
        canvas.drawText(fpsText, padding + 25f, startY + lineGap * 3, titlePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }
}
