#ifndef RAW_C_DSP_H
#define RAW_C_DSP_H

#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    float b0, b1, b2, a1, a2;
    float x1, x2, y1, y2;
} BiquadFilter;

void raw_c_biquad_init(BiquadFilter* filter, float b0, float b1, float b2, float a1, float a2);
void raw_c_biquad_process(BiquadFilter* filter, const float* input, float* output, size_t length);
void raw_c_apply_hann_window(float* buffer, size_t length);
float raw_c_compute_rms_energy(const float* buffer, size_t length);

#ifdef __cplusplus
}
#endif

#endif // RAW_C_DSP_H
