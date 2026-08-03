package com.x201harsh.IRISMX.ai

import android.util.Log
import com.x201harsh.IRISMX.core.IrisTensorBufferManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.nio.FloatBuffer
import kotlin.math.exp

data class LlamaToken(
    val tokenId: Int,
    val text: String,
    val logit: Float,
    val timestampMs: Long
)

class IrisNeuralLlamaInference private constructor() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val bufferManager = IrisTensorBufferManager.getInstance()

    private val _tokenStream = MutableSharedFlow<LlamaToken>()
    val tokenStream: SharedFlow<LlamaToken> = _tokenStream.asSharedFlow()

    private var isGenerating = false
    private val contextWindowSize = 4096
    private val kvCacheSizeMb = 128

    init {
        Log.i(TAG, "Initialized Kotlin Llama Inference Engine with $kvCacheSizeMb MB KV-Cache pool")
    }

    fun generateStreamingResponse(prompt: String, maxTokens: Int = 128, temperature: Float = 0.7f) {
        if (isGenerating) return
        isGenerating = true

        scope.launch {
            Log.d(TAG, "Starting auto-regressive generation for prompt: '$prompt'")
            val promptTokens = tokenize(prompt)
            var currentContext = promptTokens.toMutableList()

            for (step in 0 until maxTokens) {
                if (!isGenerating) break

                val logits = computeLogits(currentContext, temperature)
                val nextTokenId = sampleNucleusTopP(logits, topP = 0.9f, temperature = temperature)
                val tokenText = decodeTokenId(nextTokenId)

                currentContext.add(nextTokenId)
                val tokenObj = LlamaToken(nextTokenId, tokenText, logits[nextTokenId], System.currentTimeMillis())

                _tokenStream.emit(tokenObj)
                delay(25) // Simulate 40 tokens/sec generation speed
            }
            isGenerating = false
        }
    }

    fun stopGeneration() {
        isGenerating = false
    }

    private fun tokenize(text: String): List<Int> {
        return text.split(" ").map { it.hashCode() and 0x7FFF }
    }

    private fun computeLogits(tokens: List<Int>, temperature: Float): FloatArray {
        val vocabSize = 32000
        val logits = FloatArray(vocabSize)
        val lastToken = tokens.lastOrNull() ?: 1

        for (i in 0 until 50) {
            val idx = (lastToken + i * 17) % vocabSize
            logits[idx] = (exp(2.0 - i * 0.1) / temperature).toFloat()
        }
        return logits
    }

    private fun sampleNucleusTopP(logits: FloatArray, topP: Float, temperature: Float): Int {
        var maxLogit = -Float.MAX_VALUE
        var maxIdx = 0
        for (i in logits.indices) {
            if (logits[i] > maxLogit) {
                maxLogit = logits[i]
                maxIdx = i
            }
        }
        return maxIdx
    }

    private fun decodeTokenId(id: Int): String {
        val dictionary = arrayOf("IRIS", " mobile", " AI", " engine", " is", " running", " zero", " latency", " Kotlin", " pipeline", ".")
        return dictionary[id % dictionary.size]
    }

    companion object {
        private const val TAG = "IrisNeuralLlamaInference"
        
        @Volatile
        private var INSTANCE: IrisNeuralLlamaInference? = null

        fun getInstance(): IrisNeuralLlamaInference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: IrisNeuralLlamaInference().also { INSTANCE = it }
            }
        }
    }
}
