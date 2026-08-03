package com.x201harsh.IRISMX.nativebridge

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.x201harsh.IRISMX.ui.views.IrisNativeSpectrumView

class IrisSpectrumViewManager : SimpleViewManager<IrisNativeSpectrumView>() {

    override fun getName(): String {
        return "IrisNativeSpectrumView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): IrisNativeSpectrumView {
        return IrisNativeSpectrumView(reactContext)
    }
}
