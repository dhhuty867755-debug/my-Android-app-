use std::f32::consts::PI;

pub struct MelFilterbank {
    pub sample_rate: u32,
    pub fft_size: usize,
    pub num_mel_bins: usize,
    pub mel_filters: Vec<Vec<f32>>,
}

impl MelFilterbank {
    pub fn new(sample_rate: u32, fft_size: usize, num_mel_bins: usize) -> Self {
        let filters = Self::create_triangular_filters(sample_rate, fft_size, num_mel_bins);
        Self {
            sample_rate,
            fft_size,
            num_mel_bins,
            mel_filters: filters,
        }
    }

    fn hz_to_mel(hz: f32) -> f32 {
        2595.0 * (1.0 + hz / 700.0).log10()
    }

    fn mel_to_hz(mel: f32) -> f32 {
        700.0 * (10.0f32.powf(mel / 2595.0) - 1.0)
    }

    fn create_triangular_filters(sample_rate: u32, fft_size: usize, num_bins: usize) -> Vec<Vec<f32>> {
        let low_freq_mel = Self::hz_to_mel(0.0);
        let high_freq_mel = Self::hz_to_mel(sample_rate as f32 / 2.0);

        let mut mel_points = Vec::with_capacity(num_bins + 2);
        for i in 0..=(num_bins + 1) {
            let mel = low_freq_mel + i as f32 * (high_freq_mel - low_freq_mel) / (num_bins + 1) as f32;
            mel_points.push(Self::mel_to_hz(mel));
        }

        let num_fft_bins = fft_size / 2 + 1;
        let mut filters = vec![vec![0.0f32; num_fft_bins]; num_bins];

        for m in 1..=num_bins {
            let f_m_minus = mel_points[m - 1];
            let f_m = mel_points[m];
            let f_m_plus = mel_points[m + 1];

            for k in 0..num_fft_bins {
                let freq = k as f32 * sample_rate as f32 / fft_size as f32;
                if freq >= f_m_minus && freq <= f_m {
                    filters[m - 1][k] = (freq - f_m_minus) / (f_m - f_m_minus);
                } else if freq >= f_m && freq <= f_m_plus {
                    filters[m - 1][k] = (f_m_plus - freq) / (f_m_plus - f_m);
                }
            }
        }
        filters
    }

    pub fn compute_log_mel_spectrogram(&self, fft_magnitudes: &[f32]) -> Vec<f32> {
        let mut log_mel = vec![0.0f32; self.num_mel_bins];
        for (m, filter) in self.mel_filters.iter().enumerate() {
            let mut energy = 0.0f32;
            for (k, &weight) in filter.iter().enumerate() {
                if k < fft_magnitudes.len() {
                    energy += weight * fft_magnitudes[k];
                }
            }
            log_mel[m] = (energy.max(1e-6)).ln();
        }
        log_mel
    }
}
