pub struct GmmVoiceActivityDetector {
    pub speech_threshold_db: f32,
}

impl GmmVoiceActivityDetector {
    pub fn new(threshold_db: f32) -> Self {
        Self { speech_threshold_db: threshold_db }
    }

    pub fn is_speech_active(&self, pcm_energy_db: f32) -> bool {
        pcm_energy_db > self.speech_threshold_db
    }
}
