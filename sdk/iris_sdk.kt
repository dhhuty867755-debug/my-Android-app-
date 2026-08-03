package in.irisxai.sdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class IrisSdkConfig(
    val apiKey: String,
    val endpointUrl: String = "https://api.irisxai.in",
    val enableNativeDsp: Boolean = true
)

class IrisSdkClient(private val config: IrisSdkConfig) {

    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        println("Initializing IRIS Kotlin SDK with API Key: ${config.apiKey.take(4)}****")
        true
    }

    suspend fun sendAudioFrame(pcmData: FloatArray): FloatArray = withContext(Dispatchers.Default) {
        // Process PCM frame via SDK
        pcmData
    }
}
