import Foundation

@objc(IrisNativeBridge)
public class IrisNativeBridge: NSObject {

    @objc public func startEngine(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        print("[IrisNativeBridge] Native iOS Engine Started")
        resolve(true)
    }

    @objc public static func requiresMainQueueSetup() -> Bool {
        return false
    }
}
