#include "iris_filterbank.h"
#include <math.h>

void iris_c_filterbank_apply(const float* spectrum, float* mel_bands, size_t fft_bins, size_t mel_bins) {
    if (!spectrum || !mel_bands || fft_bins == 0 || mel_bins == 0) return;
    size_t step = fft_bins / mel_bins;
    if (step == 0) step = 1;

    for (size_t m = 0; m < mel_bins; ++m) {
        float sum = 0.0f;
        size_t start = m * step;
        size_t end = (m + 1) * step;
        if (end > fft_bins) end = fft_bins;

        for (size_t i = start; i < end; ++i) {
            sum += spectrum[i];
        }
        mel_bands[m] = logf(1.0f + sum);
    }
}
