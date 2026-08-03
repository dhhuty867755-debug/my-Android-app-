import Foundation
import AVFoundation

public class IrisAudioRecorder: NSObject {
    private var audioRecorder: AVAudioRecorder?
    private var isRecording: Bool = false

    public override init() {
        super.init()
    }

    public func startRecording() -> Bool {
        let settings: [String: Any] = [
            AVFormatIDKey: kAudioFormatLinearPCM,
            AVSampleRateKey: 44100.0,
            AVNumberOfChannelsKey: 1,
            AVLinearPCMBitDepthKey: 16,
            AVLinearPCMIsFloatKey: false
        ]

        let url = FileManager.default.temporaryDirectory.appendingPathComponent("iris_record.pcm")
        do {
            audioRecorder = try AVAudioRecorder(url: url, settings: settings)
            audioRecorder?.record()
            isRecording = true
            print("[IrisAudioRecorder] Recording started at \(url.path)")
            return true
        } catch {
            print("[IrisAudioRecorder] Error starting recorder: \(error)")
            return false
        }
    }

    public func stopRecording() {
        audioRecorder?.stop()
        isRecording = false
        print("[IrisAudioRecorder] Recording stopped")
    }
}
