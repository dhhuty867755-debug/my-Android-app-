#ifndef IRIS_C_FFI_H
#define IRIS_C_FFI_H

#include <stddef.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

#if defined(_WIN32)
  #define IRIS_FFI_EXPORT __declspec(dllexport)
#else
  #define IRIS_FFI_EXPORT __attribute__((visibility("default")))
#endif

IRIS_FFI_EXPORT bool iris_c_ffi_initialize(int sample_rate, int channels);
IRIS_FFI_EXPORT float iris_c_ffi_compute_simd_dot(const float* a, const float* b, size_t len);
IRIS_FFI_EXPORT void iris_c_ffi_process_pcm_frame(const float* in_pcm, float* out_pcm, size_t len);
IRIS_FFI_EXPORT void iris_c_ffi_shutdown(void);

#ifdef __cplusplus
}
#endif

#endif // IRIS_C_FFI_H
