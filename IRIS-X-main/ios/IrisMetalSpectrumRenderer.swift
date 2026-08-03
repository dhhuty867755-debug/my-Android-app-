import Foundation
import Metal
import MetalKit

public class IrisMetalSpectrumRenderer: NSObject, MTKViewDelegate {
    private var device: MTLDevice?

    public init(mtkView: MTKView) {
        super.init()
        self.device = mtkView.device ?? MTLCreateSystemDefaultDevice()
        mtkView.delegate = self
    }

    public func mtkView(_ view: MTKView, drawableSizeWillChange size: CGSize) {}

    public func draw(in view: MTKView) {
        guard let drawable = view.currentDrawable else { return }
        _ = drawable
    }
}
