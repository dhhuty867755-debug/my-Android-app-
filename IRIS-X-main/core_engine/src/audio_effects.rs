pub struct DynamicCompressor {
    pub threshold_db: f32,
    pub ratio: f32,
    pub attack_ms: f32,
    pub release_ms: f32,
}

impl DynamicCompressor {
    pub fn new(threshold_db: f32, ratio: f32) -> Self {
        Self {
            threshold_db,
            ratio,
            attack_ms: 5.0,
            release_ms: 100.0,
        }
    }

    pub fn process_samples(&self, samples: &mut [f32]) {
        for sample in samples.iter_mut() {
            let abs_val = sample.abs();
            if abs_val > 1e-5 {
                let db = 20.0 * abs_val.log10();
                if db > self.threshold_db {
                    let over_db = db - self.threshold_db;
                    let compressed_db = self.threshold_db + over_db / self.ratio;
                    let gain = 10.0f32.powf((compressed_db - db) / 20.0);
                    *sample *= gain;
                }
            }
        }
    }
}
