package com.x201harsh.IRISMX.nativebridge

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.x201harsh.IRISMX.ui.views.IrisNativeHolographicMatrixView

class IrisHolographicMatrixViewManager : SimpleViewManager<IrisNativeHolographicMatrixView>() {

    override fun getName(): String {
        return "IrisNativeHolographicMatrixView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): IrisNativeHolographicMatrixView {
        return IrisNativeHolographicMatrixView(reactContext)
    }
}
