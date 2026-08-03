package com.x201harsh.IRISMX.nativebridge

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class IrisNativePackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(IrisNativeModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(
            IrisSpectrumViewManager(),
            IrisStatusHUDViewManager(),
            IrisHolographicMatrixViewManager()
        )
    }
}
