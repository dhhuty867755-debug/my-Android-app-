#ifndef IRIS_FILTERBANK_H
#define IRIS_FILTERBANK_H

#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

void iris_c_filterbank_apply(const float* spectrum, float* mel_bands, size_t fft_bins, size_t mel_bins);

#ifdef __cplusplus
}
#endif

#endif // IRIS_FILTERBANK_H
