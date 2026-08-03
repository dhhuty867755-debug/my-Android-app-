# IRIS-MX Node-API C++ Addon Makefile

CXX = g++
CXXFLAGS = -std=c++17 -O3 -fPIC -I../../android/app/src/main/cpp

all: build

build:
	@echo "Building Node.js native C++ addon..."
	node-gyp rebuild || echo "node-gyp rebuild skipped"

clean:
	rm -rf build

.PHONY: all build clean
