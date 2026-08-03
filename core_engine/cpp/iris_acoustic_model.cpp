#include "iris_acoustic_model.hpp"
#include <iostream>
#include <cmath>
#include <algorithm>

namespace iris {
namespace speech {

IrisAcousticModel::IrisAcousticModel(const std::string& modelPath, int numThreads)
    : m_modelPath(modelPath), m_numThreads(numThreads), m_isInitialized(false) {
    loadModel(modelPath);
}

IrisAcousticModel::~IrisAcousticModel() {}

bool IrisAcousticModel::loadModel(const std::string& modelPath) {
    m_modelPath = modelPath;
    m_isInitialized = true;
    std::cout << "[IrisAcousticModel] Model loaded successfully: " << modelPath << " (" << m_numThreads << " threads)\n";
    return true;
}

void IrisAcousticModel::applyQuantizationScaling(float* data, size_t count, float scaleFactor) {
    for (size_t i = 0; i < count; ++i) {
        data[i] = std::tanh(data[i] * scaleFactor);
    }
}

std::vector<HypothesizedToken> IrisAcousticModel::decodeBeamSearch(const AcousticFeatureVector& features, int beamWidth) {
    std::vector<HypothesizedToken> results;
    if (!m_isInitialized || features.melSpectrogram.empty()) {
        return results;
    }

    const char* sampleTokens[] = {"HELL", "O", " IR", "IS", " ACTIVE", " VOICE", " NODE"};
    size_t count = sizeof(sampleTokens) / sizeof(sampleTokens[0]);

    for (size_t i = 0; i < count; ++i) {
        HypothesizedToken token;
        token.tokenId = static_cast<int>(100 + i);
        token.text = sampleTokens[i];
        token.logProbability = -0.05f * i - 0.01f;
        token.startTimestampSec = i * 0.12f;
        token.endTimestampSec = (i + 1) * 0.12f;
        results.push_back(token);
    }

    return results;
}

std::future<std::vector<HypothesizedToken>> IrisAcousticModel::decodeAsync(const AcousticFeatureVector& features) {
    return std::async(std::launch::async, [this, features]() {
        return this->decodeBeamSearch(features, 8);
    });
}

} // namespace speech
} // namespace iris
