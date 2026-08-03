#include <jni.h>
#include <string>
#include <vector>
#include "iris_core_engine.h"
#include <android/log.h>

#define LOG_TAG "IrisJNIBridge"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

static iris::core::IrisCoreEngine* g_engineInstance = nullptr;

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_x201harsh_IRISMX_core_IrisNativeEngine_nativeInit(JNIEnv *env, jobject thiz, jint sampleRate, jint fftSize) {
    if (g_engineInstance == nullptr) {
        g_engineInstance = new iris::core::IrisCoreEngine();
    }
    g_engineInstance->initializeEngine(sampleRate, fftSize);
    LOGI("JNI: nativeInit successful");
    return JNI_TRUE;
}

JNIEXPORT jfloatArray JNICALL
Java_com_x201harsh_IRISMX_core_IrisNativeEngine_nativeProcessAudioFrame(JNIEnv *env, jobject thiz, jfloatArray rawSamples) {
    if (g_engineInstance == nullptr) {
        return env->NewFloatArray(0);
    }

    jsize len = env->GetArrayLength(rawSamples);
    jfloat* samplesPtr = env->GetFloatArrayElements(rawSamples, nullptr);

    iris::core::SignalFrame frame = g_engineInstance->processAudioBuffer(samplesPtr, len);

    env->ReleaseFloatArrayElements(rawSamples, samplesPtr, JNI_ABORT);

    size_t specLen = frame.spectrumData.size();
    jfloatArray resultArray = env->NewFloatArray(specLen + 3);
    if (resultArray == nullptr) return nullptr;

    std::vector<float> pack;
    pack.push_back(frame.peakAmplitude);
    pack.push_back(frame.rmsEnergy);
    pack.push_back(frame.quantumCoherence);
    pack.insert(pack.end(), frame.spectrumData.begin(), frame.spectrumData.end());

    env->SetFloatArrayRegion(resultArray, 0, pack.size(), pack.data());
    return resultArray;
}

JNIEXPORT jdoubleArray JNICALL
Java_com_x201harsh_IRISMX_core_IrisNativeEngine_nativeGetTelemetry(JNIEnv *env, jobject thiz) {
    jdoubleArray result = env->NewDoubleArray(4);
    if (g_engineInstance == nullptr) {
        return result;
    }

    iris::core::EngineMetrics m = g_engineInstance->getMetrics();
    jdouble buffer[4] = {
        m.cpuLoadPercentage,
        static_cast<jdouble>(m.totalAllocatedBytes),
        static_cast<jdouble>(m.processedFrameCount),
        static_cast<jdouble>(m.meanInferenceLatencyMs)
    };

    env->SetDoubleArrayRegion(result, 0, 4, buffer);
    return result;
}

JNIEXPORT void JNICALL
Java_com_x201harsh_IRISMX_core_IrisNativeEngine_nativeDestroy(JNIEnv *env, jobject thiz) {
    if (g_engineInstance != nullptr) {
        delete g_engineInstance;
        g_engineInstance = nullptr;
        LOGI("JNI: nativeDestroy executed");
    }
}

} // extern "C"
