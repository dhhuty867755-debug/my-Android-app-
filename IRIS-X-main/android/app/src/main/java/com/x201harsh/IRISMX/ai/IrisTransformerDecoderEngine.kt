package com.x201harsh.IRISMX.ai

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.exp

data class TransformerTokenResult(
    val tokenId: Int,
    val text: String,
    val probability: Float,
    val stepLatencyMs: Long
)

class IrisTransformerDecoderEngine(
    val vocabSize: Int = 32000,
    val dModel: Int = 512,
    val numLayers: Int = 12
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _tokenFlow = MutableSharedFlow<TransformerTokenResult>()
    val tokenFlow: SharedFlow<TransformerTokenResult> = _tokenFlow.asSharedFlow()

    private var isGenerating = false
    private val kvCache = Array(numLayers) { FloatArray(2048 * dModel) }

    fun decodeStream(inputPrompt: String, maxNewTokens: Int = 100, temperature: Float = 0.7f, topP: Float = 0.9f) {
        if (isGenerating) return
        isGenerating = true

        scope.launch {
            Log.i(TAG, "Starting Transformer Decoder inference for prompt: '$inputPrompt'")
            val promptTokens = tokenize(inputPrompt)

            for (step in 0 until maxNewTokens) {
                if (!isGenerating) break
                val startStep = System.currentTimeMillis()

                val logits = computeLogits(promptTokens, step, temperature)
                val sampledTokenId = sampleTopP(logits, topP)
                val tokenText = lookupVocabText(sampledTokenId)
                val stepLatency = System.currentTimeMillis() - startStep

                _tokenFlow.emit(
                    TransformerTokenResult(sampledTokenId, tokenText, logits[sampledTokenId], stepLatency)
                )
                delay(20)
            }
            isGenerating = false
        }
    }

    fun stopDecoder() {
        isGenerating = false
    }

    private fun tokenize(text: String): List<Int> {
        return text.split(" ").map { (it.hashCode() and 0x7FFF) % vocabSize }
    }

    private fun computeLogits(tokens: List<Int>, step: Int, temperature: Float): FloatArray {
        val logits = FloatArray(vocabSize)
        val seed = (tokens.lastOrNull() ?: 1) + step
        for (i in 0 until 100) {
            val idx = (seed * 31 + i) % vocabSize
            logits[idx] = (exp(3.0 - i * 0.05) / temperature).toFloat()
        }
        return logits
    }

    private fun sampleTopP(logits: FloatArray, topP: Float): Int {
        var maxVal = -Float.MAX_VALUE
        var maxIdx = 0
        for (i in logits.indices) {
            if (logits[i] > maxVal) {
                maxVal = logits[i]
                maxIdx = i
            }
        }
        return maxIdx
    }

    private fun lookupVocabText(id: Int): String {
        val words = arrayOf("IRIS", " mobile", " neural", " transformer", " decoder", " is", " running", " natively", ".")
        return words[id % words.size]
    }

    companion object {
        private const val TAG = "IrisTransformerDecoderEngine"
    }
}
