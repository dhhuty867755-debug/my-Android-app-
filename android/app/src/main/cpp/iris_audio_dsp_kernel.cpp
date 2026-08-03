#include <vector>
#include <cmath>
#include <algorithm>
#include <cstddef>
#include <android/log.h>

#define LOG_TAG "IrisAudioDspKernel"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

namespace iris {
namespace dsp {

class IrisAudioDspKernel {
public:
    static void applyBlackmanWindow(float* buffer, size_t size) {
        if (size == 0) return;
        const float a0 = 0.42f;
        const float a1 = 0.50f;
        const float a2 = 0.08f;
        const float pi2 = 2.0f * M_PI;
        const float pi4 = 4.0f * M_PI;

        for (size_t i = 0; i < size; ++i) {
            float arg = static_cast<float>(i) / static_cast<float>(size - 1);
            float w = a0 - a1 * std::cos(pi2 * arg) + a2 * std::cos(pi4 * arg);
            buffer[i] *= w;
        }
    }

    static void computeMagnitudeSpectrum(const float* real, const float* imag, float* mag, size_t fftBins) {
        for (size_t i = 0; i < fftBins; ++i) {
            mag[i] = std::sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }
    }

    static void applyPreEmphasis(float* buffer, size_t size, float coeff = 0.97f) {
        if (size <= 1) return;
        for (size_t i = size - 1; i > 0; --i) {
            buffer[i] = buffer[i] - coeff * buffer[i - 1];
        }
    }

    static void compressDynamicRange(float* buffer, size_t size, float threshold = 0.8f, float ratio = 4.0f) {
        for (size_t i = 0; i < size; ++i) {
            float absVal = std::abs(buffer[i]);
            if (absVal > threshold) {
                float excess = absVal - threshold;
                float compressed = threshold + excess / ratio;
                buffer[i] = (buffer[i] >= 0.0f) ? compressed : -compressed;
            }
        }
    }
};

} // namespace dsp
} // namespace iris
