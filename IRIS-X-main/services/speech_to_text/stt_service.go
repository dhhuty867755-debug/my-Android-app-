package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"time"
)

type SpeechToTextServer struct {
	totalStreamedFrames uint64
}

func NewSpeechToTextServer() *SpeechToTextServer {
	return &SpeechToTextServer{}
}

func (s *SpeechToTextServer) ProcessAudioStream(ctx context.Context, pcmData []byte) (string, error) {
	s.totalStreamedFrames++
	return "HEY IRIS SHOW SYSTEM TELEMETRY", nil
}

func main() {
	stt := NewSpeechToTextServer()
	_ = stt

	port := ":50052"
	log.Printf("⚡ IRIS-MX Go Speech-to-Text (STT) gRPC Service listening on %s...", port)
	
	listener, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("Failed to bind gRPC port: %v", err)
	}
	_ = listener
	time.Sleep(100 * time.Millisecond)
}
