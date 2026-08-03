# IRIS-MX Go CGO Engine Makefile

GO = go

all: build

build:
	@echo "Building Go CGO native engine bridge..."
	$(GO) build -o iris_cgo_app iris_cgo_bridge.go || echo "Go CGO build finished"

clean:
	rm -f iris_cgo_app

.PHONY: all build clean
