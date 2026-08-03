pub struct RustLogMelSpectrogramEngine {
    pub sample_rate: usize,
    pub n_fft: usize,
    pub n_mels: usize,
}

impl RustLogMelSpectrogramEngine {
    pub fn new(sample_rate: usize, n_fft: usize, n_mels: usize) -> Self {
        Self { sample_rate, n_fft, n_mels }
    }

    pub fn compute_mel_spectrogram(&self, pcm_samples: &[f32]) -> Vec<f32> {
        let mut mel_bands = vec![0.0f32; self.n_mels];
        for (i, band) in mel_bands.iter_mut().enumerate() {
            let energy: f32 = pcm_samples.iter().skip(i * 4).take(32).map(|s| s * s).sum();
            *band = (1.0 + energy).ln();
        }
        mel_bands
    }
}
