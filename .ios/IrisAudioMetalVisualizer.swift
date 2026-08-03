import Foundation
import MetalKit

public class IrisAudioMetalVisualizer: MTKView {
    
    private var commandQueue: MTLCommandQueue?
    private var pipelineState: MTLRenderPipelineState?
    
    public override init(frame frameRect: CGRect, device: MTLDevice?) {
        super.init(frame: frameRect, device: device ?? MTLCreateSystemDefaultDevice())
        setupMetal()
    }
    
    required init(coder: NSCoder) {
        super.init(coder: coder)
        setupMetal()
    }
    
    private func setupMetal() {
        guard let metalDevice = self.device else { return }
        self.commandQueue = metalDevice.makeCommandQueue()
        self.clearColor = MTLClearColor(red: 0.05, green: 0.02, blue: 0.10, alpha: 1.0)
        print("[IrisAudioMetalVisualizer] Metal GPU device ready: \(metalDevice.name)")
    }
    
    public override func draw(_ rect: CGRect) {
        guard let drawable = currentDrawable,
              let renderPassDescriptor = currentRenderPassDescriptor,
              let commandQueue = commandQueue,
              let commandBuffer = commandQueue.makeCommandBuffer(),
              let renderEncoder = commandBuffer.makeRenderCommandEncoder(descriptor: renderPassDescriptor) else {
            return
        }
        
        renderEncoder.endEncoding()
        commandBuffer.present(drawable)
        commandBuffer.commit()
    }
}
