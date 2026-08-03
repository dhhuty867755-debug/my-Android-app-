#include "iris_c_ffi.h"
#include "../../android/app/src/main/cpp/iris_core_engine.h"
#include "../../android/app/src/main/cpp/iris_simd_matrix.hpp"
#include <iostream>

static iris::core::IrisCoreEngine* g_engine = nullptr;

IRIS_FFI_EXPORT bool iris_c_ffi_initialize(int sample_rate, int channels) {
    if (!g_engine) {
        g_engine = new iris::core::IrisCoreEngine();
    }
    return g_engine->initializeEngine(sample_rate, 512);
}

IRIS_FFI_EXPORT float iris_c_ffi_compute_simd_dot(const float* a, const float* b, size_t len) {
    if (!a || !b) return 0.0f;
    return iris::simd::IrisSimdMatrixAccelerator::dotProductSimd(a, b, len);
}

IRIS_FFI_EXPORT void iris_c_ffi_process_pcm_frame(const float* in_pcm, float* out_pcm, size_t len) {
    if (!in_pcm || !out_pcm) return;
    for (size_t i = 0; i < len; ++i) {
        out_pcm[i] = in_pcm[i] * 1.05f;
    }
}

IRIS_FFI_EXPORT void iris_c_ffi_shutdown(void) {
    if (g_engine) {
        delete g_engine;
        g_engine = nullptr;
    }
}
