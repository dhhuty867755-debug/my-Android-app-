package com.x201harsh.IRISMX.ai

import android.util.Log
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class IrisConformerAttentionEncoder(
    val dModel: Int = 256,
    val numHeads: Int = 4,
    val convKernelSize: Int = 31
) {
    private val headDim = dModel / numHeads

    fun forwardSelfAttention(inputTensor: Array<FloatArray>): Array<FloatArray> {
        val seqLen = inputTensor.size
        val output = Array(seqLen) { FloatArray(dModel) }

        for (i in 0 until seqLen) {
            val q = inputTensor[i]
            for (j in 0 until seqLen) {
                val k = inputTensor[j]
                var dot = 0f
                for (d in 0 until dModel) {
                    dot += q[d] * k[d]
                }
                val attnScore = (dot / sqrt(headDim.toFloat())).coerceIn(-10f, 10f)
                val weight = (1.0f / (1.0f + kotlin.math.exp(-attnScore)))

                val v = inputTensor[j]
                for (d in 0 until dModel) {
                    output[i][d] += weight * v[d]
                }
            }
        }
        return output
    }

    fun forwardDepthwiseConv1D(inputTensor: Array<FloatArray>): Array<FloatArray> {
        val seqLen = inputTensor.size
        val output = Array(seqLen) { FloatArray(dModel) }

        val halfK = convKernelSize / 2
        for (i in 0 until seqLen) {
            for (d in 0 until dModel) {
                var convSum = 0f
                for (k in -halfK..halfK) {
                    val idx = (i + k).coerceIn(0, seqLen - 1)
                    val weight = (sin(k * 0.1f) * cos(d * 0.05f)).toFloat()
                    convSum += inputTensor[idx][d] * weight
                }
                output[i][d] = (convSum * 1.414f).coerceIn(-5f, 5f)
            }
        }
        return output
    }

    companion object {
        private const val TAG = "IrisConformerAttentionEncoder"
    }
}
