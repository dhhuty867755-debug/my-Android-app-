package com.x201harsh.IRISMX.core

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class IrisNativeBridgeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "IrisNativeBridgeModule"

    @ReactMethod
    fun getNativeSystemStatus(promise: Promise) {
        promise.resolve("IRIS_KOTLIN_DSP_ENGINE_ACTIVE")
    }
}
