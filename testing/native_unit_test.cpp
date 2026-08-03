#include "../android/app/src/main/cpp/iris_core_engine.h"
#include "../android/app/src/main/cpp/iris_simd_matrix.hpp"
#include <iostream>
#include <cassert>
#include <cmath>

void testFFTProcessing() {
    iris::core::IrisCoreEngine engine;
    engine.initializeEngine(44100, 512);

    float samples[512];
    for (int i = 0; i < 512; ++i) {
        samples[i] = std::sin(2.0 * M_PI * 440.0 * i / 44100.0);
    }

    iris::core::SignalFrame frame = engine.processAudioBuffer(samples, 512);
    assert(frame.spectrumData.size() == 256);
    assert(frame.peakAmplitude > 0.9f);
    std::cout << "[PASS] C++ Core Engine FFT Test\n";
}

void testSimdDotProduct() {
    float a[4] = {1.0f, 2.0f, 3.0f, 4.0f};
    float b[4] = {2.0f, 0.5f, 1.0f, 2.0f};
    float dot = iris::simd::IrisSimdMatrixAccelerator::dotProductSimd(a, b, 4);
    assert(std::abs(dot - 14.0f) < 1e-4);
    std::cout << "[PASS] C++ ARM NEON SIMD Dot Product Test\n";
}

int main() {
    std::cout << "================ RUNNING C++ NATIVE ENGINE TESTS ================\n";
    testFFTProcessing();
    testSimdDotProduct();
    std::cout << "================ ALL C++ NATIVE TESTS PASSED ====================\n";
    return 0;
}
