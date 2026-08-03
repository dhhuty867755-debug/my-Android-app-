import Foundation
import AVFoundation

@objc(IrisNativeEngineBridge)
public class IrisNativeEngineBridge: NSObject {
    
    private var audioEngine: AVAudioEngine?
    private var isEngineRunning: Bool = false

    @objc public override init() {
        super.init()
        self.audioEngine = AVAudioEngine()
        print("[IrisNativeEngineBridge] Initialized native iOS AVAudioEngine core")
    }

    @objc public func startAudioSession() -> Bool {
        guard let engine = audioEngine else { return false }
        
        let session = AVAudioSession.sharedInstance()
        do {
            try session.setCategory(.playAndRecord, mode: .voiceChat, options: [.defaultToSpeaker, .allowBluetooth])
            try session.setActive(true)
            
            let inputNode = engine.inputNode
            let format = inputNode.outputFormat(forBus: 0)
            
            inputNode.installTap(onBus: 0, bufferSize: 1024, format: format) { (buffer, time) in
                // Process PCM buffer for C++ DSP engine
                let frameCount = buffer.frameLength
                _ = frameCount
            }
            
            engine.prepare()
            try engine.start()
            isEngineRunning = true
            print("[IrisNativeEngineBridge] AVAudioEngine started successfully at \(format.sampleRate) Hz")
            return true
        } catch {
            print("[IrisNativeEngineBridge] Failed to start audio session: \(error)")
            return false
        }
    }

    @objc public func stopAudioSession() {
        audioEngine?.stop()
        audioEngine?.inputNode.removeTap(onBus: 0)
        isEngineRunning = false
        print("[IrisNativeEngineBridge] Audio session stopped")
    }
}
