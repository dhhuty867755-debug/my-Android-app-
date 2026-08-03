#ifndef IRIS_FFT_RADIX4_H
#define IRIS_FFT_RADIX4_H

#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

void iris_c_radix4_fft(float* real, float* imag, size_t length);
void iris_c_power_spectrum(const float* real, const float* imag, float* power, size_t length);

#ifdef __cplusplus
}
#endif

#endif // IRIS_FFT_RADIX4_H
