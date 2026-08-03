#include "raw_c_dsp.h"
#include <math.h>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

void raw_c_biquad_init(BiquadFilter* filter, float b0, float b1, float b2, float a1, float a2) {
    if (!filter) return;
    filter->b0 = b0; filter->b1 = b1; filter->b2 = b2;
    filter->a1 = a1; filter->a2 = a2;
    filter->x1 = 0.0f; filter->x2 = 0.0f;
    filter->y1 = 0.0f; filter->y2 = 0.0f;
}

void raw_c_biquad_process(BiquadFilter* filter, const float* input, float* output, size_t length) {
    if (!filter || !input || !output) return;
    for (size_t i = 0; i < length; ++i) {
        float x = input[i];
        float y = filter->b0 * x + filter->b1 * filter->x1 + filter->b2 * filter->x2
                  - filter->a1 * filter->y1 - filter->a2 * filter->y2;
        filter->x2 = filter->x1;
        filter->x1 = x;
        filter->y2 = filter->y1;
        filter->y1 = y;
        output[i] = y;
    }
}

void raw_c_apply_hann_window(float* buffer, size_t length) {
    if (!buffer || length == 0) return;
    for (size_t i = 0; i < length; ++i) {
        float window = 0.5f * (1.0f - cosf(2.0f * (float)M_PI * i / (length - 1)));
        buffer[i] *= window;
    }
}

float raw_c_compute_rms_energy(const float* buffer, size_t length) {
    if (!buffer || length == 0) return 0.0f;
    float sum = 0.0f;
    for (size_t i = 0; i < length; ++i) {
        sum += buffer[i] * buffer[i];
    }
    return sqrtf(sum / (float)length);
}
