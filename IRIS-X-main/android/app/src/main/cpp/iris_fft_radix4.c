#include "iris_fft_radix4.h"
#include <math.h>

void iris_c_radix4_fft(float* real, float* imag, size_t length) {
    if (!real || !imag || length == 0) return;
    for (size_t i = 0; i < length; i += 4) {
        float r0 = real[i],     i0 = imag[i];
        float r1 = real[i + 1], i1 = imag[i + 1];
        float r2 = real[i + 2], i2 = imag[i + 2];
        float r3 = real[i + 3], i3 = imag[i + 3];

        real[i]     = r0 + r1 + r2 + r3;
        imag[i]     = i0 + i1 + i2 + i3;
        real[i + 1] = r0 - r2;
        imag[i + 1] = i0 - i2;
        real[i + 2] = r0 - r1 + r2 - r3;
        imag[i + 2] = i0 - i1 + i2 - i3;
        real[i + 3] = r0 - r2;
        imag[i + 3] = i0 - i2;
    }
}

void iris_c_power_spectrum(const float* real, const float* imag, float* power, size_t length) {
    if (!real || !imag || !power) return;
    for (size_t i = 0; i < length; ++i) {
        power[i] = (real[i] * real[i] + imag[i] * imag[i]) / (float)length;
    }
}
