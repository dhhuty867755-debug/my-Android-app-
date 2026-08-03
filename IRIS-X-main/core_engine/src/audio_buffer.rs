use std::sync::atomic::{AtomicUsize, Ordering};

pub struct RustAtomicAudioRingBuffer {
    buffer: Vec<f32>,
    write_idx: AtomicUsize,
    read_idx: AtomicUsize,
    capacity: usize,
}

impl RustAtomicAudioRingBuffer {
    pub fn new(capacity: usize) -> Self {
        Self {
            buffer: vec![0.0; capacity],
            write_idx: AtomicUsize::new(0),
            read_idx: AtomicUsize::new(0),
            capacity,
        }
    }

    pub fn available_to_read(&self) -> usize {
        let w = self.write_idx.load(Ordering::Relaxed);
        let r = self.read_idx.load(Ordering::Relaxed);
        if w >= r { w - r } else { self.capacity - r + w }
    }
}
