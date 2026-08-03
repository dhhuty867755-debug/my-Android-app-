pub struct LockFreeRingBuffer {
    buffer: Vec<f32>,
    capacity: usize,
    read_pos: usize,
    write_pos: usize,
}

impl LockFreeRingBuffer {
    pub fn new(capacity: usize) -> Self {
        Self {
            buffer: vec![0.0; capacity],
            capacity,
            read_pos: 0,
            write_pos: 0,
        }
    }

    pub fn write_samples(&mut self, data: &[f32]) -> usize {
        let mut count = 0;
        for &val in data {
            let next_write = (self.write_pos + 1) % self.capacity;
            if next_write == self.read_pos {
                break; // Buffer full
            }
            self.buffer[self.write_pos] = val;
            self.write_pos = next_write;
            count += 1;
        }
        count
    }

    pub fn read_samples(&mut self, output: &mut [f32]) -> usize {
        let mut count = 0;
        for item in output.iter_mut() {
            if self.read_pos == self.write_pos {
                break; // Buffer empty
            }
            *item = self.buffer[self.read_pos];
            self.read_pos = (self.read_pos + 1) % self.capacity;
            count += 1;
        }
        count
    }
}
