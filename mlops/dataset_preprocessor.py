#!/usr/bin/env python3
"""
IRIS-MX Audio Dataset Preprocessor & Normalizer
"""

import sys
import math

class AudioDatasetPreprocessor:
    def __init__(self, target_sample_rate: int = 44100):
        self.sample_rate = target_sample_rate

    def normalize_pcm_waveform(self, samples):
        max_val = max(abs(s) for s in samples) if samples else 1.0
        scale = 0.95 / max_val if max_val > 0 else 1.0
        return [s * scale for s in samples]

if __name__ == "__main__":
    preprocessor = AudioDatasetPreprocessor()
    test_wave = [math.sin(i * 0.1) for i in range(100)]
    normed = preprocessor.normalize_pcm_waveform(test_wave)
    print(f"[MLOPS] Preprocessed audio dataset: {len(normed)} samples normalized.")
