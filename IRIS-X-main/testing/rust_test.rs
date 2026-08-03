#[cfg(test)]
mod tests {
    use iris_core_audio::VoiceActivityDetector;

    #[test]
    fn test_rust_vad_detection() {
        let detector = VoiceActivityDetector::new(44100, 512, -40.0);
        let samples = vec![0.8f32; 512];
        let result = detector.process_frame(&samples);
        assert!(result.confidence >= 0.0);
        assert!(result.confidence <= 1.0);
    }

    #[test]
    fn test_mel_filterbank_bands() {
        let detector = VoiceActivityDetector::new(44100, 512, -40.0);
        let samples = vec![0.2f32; 512];
        let mel_frame = detector.compute_mel_filterbank(&samples, 80);
        assert_eq!(mel_frame.bands.len(), 80);
    }
}
