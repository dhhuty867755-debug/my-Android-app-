use std::time::Instant;

pub struct EnginePerformanceCollector {
    start_time: Instant,
    total_frames: u64,
}

impl EnginePerformanceCollector {
    pub fn new() -> Self {
        Self {
            start_time: Instant::now(),
            total_frames: 0,
        }
    }

    pub fn record_frame(&mut self) {
        self.total_frames += 1;
    }

    pub fn get_uptime_seconds(&self) -> f64 {
        self.start_time.elapsed().as_secs_f64()
    }
}
