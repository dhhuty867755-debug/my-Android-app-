package com.x201harsh.IRISMX.nativebridge

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.x201harsh.IRISMX.ui.views.IrisNativeStatusHUDView

class IrisStatusHUDViewManager : SimpleViewManager<IrisNativeStatusHUDView>() {

    override fun getName(): String {
        return "IrisNativeStatusHUDView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): IrisNativeStatusHUDView {
        return IrisNativeStatusHUDView(reactContext)
    }
}
