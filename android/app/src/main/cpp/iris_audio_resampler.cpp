#include <vector>
#include <cmath>
#include <algorithm>
#include <android/log.h>

#define LOG_TAG "IrisAudioResampler"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

namespace iris {
namespace audio {

class IrisAudioResampler {
public:
    IrisAudioResampler(double inputRate, double outputRate)
        : m_inputRate(inputRate), m_outputRate(outputRate), m_ratio(outputRate / inputRate) {
        LOGI("Resampler initialized: %.0f Hz -> %.0f Hz (ratio=%.4f)", inputRate, outputRate, m_ratio);
    }

    std::vector<float> resampleSinc(const float* inputSamples, size_t inputLength) {
        size_t outputLength = static_cast<size_t>(std::ceil(inputLength * m_ratio));
        std::vector<float> output(outputLength, 0.0f);

        const int filterHalfWidth = 16;
        for (size_t i = 0; i < outputLength; ++i) {
            double srcIdx = i / m_ratio;
            int center = static_cast<int>(std::floor(srcIdx));
            double frac = srcIdx - center;

            float sum = 0.0f;
            for (int k = -filterHalfWidth; k <= filterHalfWidth; ++k) {
                int idx = center + k;
                if (idx >= 0 && idx < static_cast<int>(inputLength)) {
                    double t = (k - frac) * M_PI;
                    double sinc = (std::abs(t) < 1e-5) ? 1.0 : std::sin(t) / t;
                    double window = 0.54 + 0.46 * std::cos(M_PI * (k - frac) / filterHalfWidth);
                    sum += static_cast<float>(inputSamples[idx] * sinc * window);
                }
            }
            output[i] = sum;
        }
        return output;
    }

private:
    double m_inputRate;
    double m_outputRate;
    double m_ratio;
};

} // namespace audio
} // namespace iris
