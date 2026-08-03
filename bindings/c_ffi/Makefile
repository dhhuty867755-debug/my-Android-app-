# IRIS-MX C FFI Shared Library Makefile

CXX = g++
CXXFLAGS = -std=c++17 -O3 -fPIC -I../../android/app/src/main/cpp

ifeq ($(OS),Windows_NT)
    TARGET = libiris_c_ffi.dll
else
    TARGET = libiris_c_ffi.so
endif

all: $(TARGET)

$(TARGET): iris_c_ffi.cpp
	$(CXX) $(CXXFLAGS) -shared -o $@ iris_c_ffi.cpp ../../android/app/src/main/cpp/iris_core_engine.cpp ../../android/app/src/main/cpp/iris_simd_matrix.cpp

clean:
	rm -f $(TARGET) *.o

.PHONY: all clean
