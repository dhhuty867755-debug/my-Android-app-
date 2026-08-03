package main

/*
#cgo CXXFLAGS: -std=c++17 -I../../android/app/src/main/cpp
#cgo LDFLAGS: -L../c_ffi -liris_c_ffi
#include "../c_ffi/iris_c_ffi.h"
*/
import "C"
import (
	"fmt"
	"unsafe"
)

type IrisCgoEngine struct {
	sampleRate int
}

func NewIrisCgoEngine(sampleRate int) *IrisCgoEngine {
	C.iris_c_ffi_initialize(C.int(sampleRate), 1)
	return &IrisCgoEngine{sampleRate: sampleRate}
}

func (e *IrisCgoEngine) ComputeSimdDot(a, b []float32) float32 {
	if len(a) != len(b) || len(a) == 0 {
		return 0.0
	}
	res := C.iris_c_ffi_compute_simd_dot(
		(*C.float)(unsafe.Pointer(&a[0])),
		(*C.float)(unsafe.Pointer(&b[0])),
		C.size_t(len(a)),
	)
	return float32(res)
}

func main() {
	engine := NewIrisCgoEngine(44100)
	a := []float32{1.0, 2.0, 3.0, 4.0}
	b := []float32{2.0, 0.5, 1.0, 2.0}
	dot := engine.ComputeSimdDot(a, b)
	fmt.Printf("[Go CGO] Computed SIMD Dot Product: %.2f\n", dot)
}
