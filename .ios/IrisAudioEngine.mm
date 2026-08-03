#import <Foundation/Foundation.h>
#include "../android/app/src/main/cpp/iris_core_engine.h"

@interface IrisAudioEngineBridge : NSObject
- (BOOL)initNativeEngine:(int)sampleRate fftSize:(int)fftSize;
- (NSArray<NSNumber *> *)processAudioBuffer:(const float *)samples count:(int)count;
@end

@implementation IrisAudioEngineBridge {
    iris::core::IrisCoreEngine* _cppEngine;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _cppEngine = new iris::core::IrisCoreEngine();
    }
    return self;
}

- (void)dealloc {
    if (_cppEngine) {
        delete _cppEngine;
        _cppEngine = nullptr;
    }
}

- (BOOL)initNativeEngine:(int)sampleRate fftSize:(int)fftSize {
    if (_cppEngine) {
        _cppEngine->initializeEngine(sampleRate, fftSize);
        return YES;
    }
    return NO;
}

- (NSArray<NSNumber *> *)processAudioBuffer:(const float *)samples count:(int)count {
    if (!_cppEngine) return @[];
    
    iris::core::SignalFrame frame = _cppEngine->processAudioBuffer(samples, count);
    NSMutableArray<NSNumber *> *result = [NSMutableArray arrayWithCapacity:frame.spectrumData.size()];
    for (float val : frame.spectrumData) {
        [result addObject:@(val)];
    }
    return result;
}

@end
