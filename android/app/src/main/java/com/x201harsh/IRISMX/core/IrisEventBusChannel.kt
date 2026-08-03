package com.x201harsh.IRISMX.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class IrisNativeEvent {
    data class AudioSpectrumUpdated(val peakRms: Float, val bands: FloatArray) : IrisNativeEvent()
    data class NeuralIntentDetected(val intent: String, val confidence: Float) : IrisNativeEvent()
    data class TelemetryLogged(val cpuUsage: Double, val heapMb: Double) : IrisNativeEvent()
}

object IrisEventBusChannel {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _eventFlow = MutableSharedFlow<IrisNativeEvent>(replay = 1)
    val eventFlow: SharedFlow<IrisNativeEvent> = _eventFlow.asSharedFlow()

    fun postEvent(event: IrisNativeEvent) {
        scope.launch {
            _eventFlow.emit(event)
        }
    }
}
