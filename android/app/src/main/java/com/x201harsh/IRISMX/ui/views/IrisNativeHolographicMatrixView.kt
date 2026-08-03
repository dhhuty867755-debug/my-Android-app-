package com.x201harsh.IRISMX.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random

class IrisNativeHolographicMatrixView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val viewScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = 2f }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00F0FF")
        textSize = 24f
        typeface = Typeface.MONOSPACE
    }

    private class Node3D(var x: Float, var y: Float, var z: Float, var radius: Float, var color: Int)

    private val nodes = ArrayList<Node3D>()
    private var rotationY = 0f

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        initMatrixNodes()
        startRenderLoop()
    }

    private fun initMatrixNodes() {
        nodes.clear()
        val colors = intArrayOf(
            Color.parseColor("#00F0FF"),
            Color.parseColor("#7000FF"),
            Color.parseColor("#FF007A")
        )
        for (i in 0 until 35) {
            nodes.add(
                Node3D(
                    x = (Random.nextFloat() - 0.5f) * 400f,
                    y = (Random.nextFloat() - 0.5f) * 400f,
                    z = (Random.nextFloat() - 0.5f) * 400f,
                    radius = Random.nextFloat() * 6f + 3f,
                    color = colors[Random.nextInt(colors.size)]
                )
            )
        }
    }

    private fun startRenderLoop() {
        viewScope.launch {
            while (isActive) {
                rotationY += 0.02f
                invalidate()
                delay(16)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        // Draw Dark Cyber Background
        canvas.drawColor(Color.parseColor("#080312"))

        val projectedX = FloatArray(nodes.size)
        val projectedY = FloatArray(nodes.size)

        // 3D Perspective Projection Math
        for (i in nodes.indices) {
            val node = nodes[i]
            val rad = rotationY
            val rotX = node.x * cos(rad) + node.z * sin(rad)
            val rotZ = -node.x * sin(rad) + node.z * cos(rad)

            val fov = 350f
            val distance = fov + rotZ
            val scale = fov / distance.coerceAtLeast(1f)

            projectedX[i] = cx + rotX * scale
            projectedY[i] = cy + node.y * scale

            nodePaint.color = node.color
            nodePaint.alpha = (scale * 200f).toInt().coerceIn(30, 255)
            canvas.drawCircle(projectedX[i], projectedY[i], node.radius * scale, nodePaint)
        }

        // Draw Interconnecting Neural Mesh Synapses
        linePaint.color = Color.parseColor("#7000FF")
        linePaint.alpha = 80
        for (i in 0 until nodes.size) {
            for (j in i + 1 until nodes.size) {
                val dx = projectedX[i] - projectedX[j]
                val dy = projectedY[i] - projectedY[j]
                val dist = sqrt(dx * dx + dy * dy)
                if (dist < 120f) {
                    canvas.drawLine(projectedX[i], projectedY[i], projectedX[j], projectedY[j], linePaint)
                }
            }
        }

        canvas.drawText("VULKAN 3D NEURAL MATRIX • NODES: 35", 25f, 45f, textPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }
}
