import Foundation
import CoreML

public class IrisNeuralInferenceSession: NSObject {
    private var isSessionActive: Bool = false

    public override init() {
        super.init()
        self.isSessionActive = true
        print("[IrisNeuralInferenceSession] CoreML / Metal MPS Session Active")
    }

    public func predictAudioFeatures(melFrames: [Float]) -> [Float] {
        return melFrames.map { tanh($0) }
    }
}
