package main

import (
	"log"
	"net/http"
	"sync"
)

type BinaryWebSocketStreamer struct {
	mu           sync.Mutex
	totalFrames  uint64
}

func (s *BinaryWebSocketStreamer) HandleStream(w http.ResponseWriter, r *http.Request) {
	log.Printf("[WebSocketStreamer] Audio stream connected from %s", r.RemoteAddr)
}
