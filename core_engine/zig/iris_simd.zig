const std = @import("std");

pub const IrisZigSimdAccelerator = struct {
    pub fn simdDotProduct(a: []const f32, b: []const f32) f32 {
        var sum: f32 = 0.0;
        var i: usize = 0;
        const len = @min(a.len, b.len);
        
        while (i < len) : (i += 1) {
            sum += a[i] * b[i];
        }
        return sum;
    }
};

export fn zig_simd_dot_product(a_ptr: [*]const f32, b_ptr: [*]const f32, len: usize) f32 {
    const slice_a = a_ptr[0..len];
    const slice_b = b_ptr[0..len];
    return IrisZigSimdAccelerator.simdDotProduct(slice_a, slice_b);
}
