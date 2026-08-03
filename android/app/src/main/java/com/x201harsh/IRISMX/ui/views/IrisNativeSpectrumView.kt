package com.x201harsh.IRISMX.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.x201harsh.IRISMX.core.AudioSpectrumFrame
import com.x201harsh.IRISMX.core.IrisNativeEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.*
import kotlin.random.Random

class IrisNativeSpectrumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val viewScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#80FFFFFF")
        textSize = 28f
        typeface = Typeface.MONOSPACE
    }

    private var currentSpectrum: FloatArray = FloatArray(64)
    private var peakAmplitude: Float = 0f
    private var quantumCoherence: Float = 0.95f
    private var rotationAngle: Float = 0f

    private class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var radius: Float,
        var alpha: Int,
        var color: Int
    )

    private val particles = ArrayList<Particle>()

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        initParticles()
        startDataSubscription()
        IrisNativeEngine.startNativeLoop()
    }

    private fun initParticles() {
        particles.clear()
        val colors = intArrayOf(
            Color.parseColor("#00F0FF"),
            Color.parseColor("#7000FF"),
            Color.parseColor("#FF007A")
        )
        for (i in 0..40) {
            particles.add(
                Particle(
                    x = Random.nextFloat() * 1000f,
                    y = Random.nextFloat() * 1000f,
                    vx = (Random.nextFloat() - 0.5f) * 2f,
                    vy = (Random.nextFloat() - 0.5f) * 2f,
                    radius = Random.nextFloat() * 5f + 2f,
                    alpha = Random.nextInt(100, 240),
                    color = colors[Random.nextInt(colors.size)]
                )
            )
        }
    }

    private fun startDataSubscription() {
        viewScope.launch {
            IrisNativeEngine.spectrumFlow.collectLatest { frame ->
                if (frame.spectrum.isNotEmpty()) {
                    currentSpectrum = frame.spectrum
                }
                peakAmplitude = frame.peakAmplitude
                quantumCoherence = frame.quantumCoherence
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = min(width, height) * 0.28f + (peakAmplitude * 30f)

        rotationAngle += 0.8f

        // Draw Dark Background with Glassmorphism Radial Gradient
        val bgGradient = RadialGradient(
            cx, cy, max(width, height) * 0.7f,
            intArrayOf(Color.parseColor("#150A21"), Color.parseColor("#05010B")),
            floatArrayOf(0.3f, 1.0f),
            Shader.TileMode.CLAMP
        )
        val bgPaint = Paint().apply { shader = bgGradient }
        canvas.drawRect(0f, 0f, width, height, bgPaint)

        // Draw Glowing Outer & Inner Orbital Rings
        glowPaint.color = Color.parseColor("#00F0FF")
        glowPaint.alpha = (100 + (peakAmplitude * 155)).toInt().coerceIn(50, 255)
        glowPaint.strokeWidth = 6f + (peakAmplitude * 10f)
        canvas.drawCircle(cx, cy, baseRadius + 15f, glowPaint)

        ringPaint.color = Color.parseColor("#7000FF")
        ringPaint.alpha = 180
        ringPaint.strokeWidth = 3f
        canvas.drawCircle(cx, cy, baseRadius - 10f, ringPaint)

        // Draw Circular Spectrum Bars
        val barCount = 48
        val angleStep = (2 * PI / barCount).toFloat()

        for (i in 0 until barCount) {
            val angle = i * angleStep + Math.toRadians(rotationAngle.toDouble()).toFloat()
            val valIndex = (i % currentSpectrum.size.coerceAtLeast(1))
            val rawVal = if (currentSpectrum.isNotEmpty()) currentSpectrum[valIndex] else 0.2f
            val barHeight = (rawVal * 120f + 15f) * (0.8f + peakAmplitude * 0.5f)

            val x1 = cx + cos(angle) * baseRadius
            val y1 = cy + sin(angle) * baseRadius
            val x2 = cx + cos(angle) * (baseRadius + barHeight)
            val y2 = cy + sin(angle) * (baseRadius + barHeight)

            val barShader = LinearGradient(
                x1, y1, x2, y2,
                Color.parseColor("#00F0FF"),
                Color.parseColor("#FF007A"),
                Shader.TileMode.CLAMP
            )
            barPaint.shader = barShader
            barPaint.strokeWidth = 8f
            canvas.drawLine(x1, y1, x2, y2, barPaint)
        }

        // Draw Dynamic Background Quantum Particles
        for (p in particles) {
            p.x += p.vx
            p.y += p.vy

            if (p.x < 0) p.x = width
            if (p.x > width) p.x = 0f
            if (p.y < 0) p.y = height
            if (p.y > height) p.y = 0f

            particlePaint.color = p.color
            particlePaint.alpha = p.alpha
            canvas.drawCircle(p.x, p.y, p.radius, particlePaint)
        }

        // Draw Native Kotlin Overlay Text
        canvas.drawText("IRIS KOTLIN DSP SPECTRUM • COHERENCE: ${(quantumCoherence * 100).toInt()}%", 30f, 50f, textPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }
}
