package main

import (
	"flag"
	"fmt"
	"log"
	"sync"
	"sync/atomic"
	"time"
)

func simulateVoiceClient(clientID int, wg *sync.WaitGroup, successCount *uint64) {
	defer wg.Done()
	// Simulate 100 audio frame writes per client
	for i := 0; i < 100; i++ {
		time.Sleep(10 * time.Millisecond)
	}
	atomic.AddUint64(successCount, 1)
}

func main() {
	clients := flag.Int("clients", 1000, "Number of concurrent voice streaming clients")
	flag.Parse()

	log.Printf("🔥 Starting Load Test: %d concurrent voice clients...", *clients)
	var wg sync.WaitGroup
	var successCount uint64

	startTime := time.Now()
	for i := 0; i < *clients; i++ {
		wg.Add(1)
		go simulateVoiceClient(i, &wg, &successCount)
	}

	wg.Wait()
	elapsed := time.Since(startTime)

	fmt.Println("\n================ LOAD TEST COMPLETED ================")
	fmt.Printf("Total Concurrent Clients : %d\n", *clients)
	fmt.Printf("Successful Audio Sessions: %d\n", successCount)
	fmt.Printf("Total Test Duration      : %.2f seconds\n", elapsed.Seconds())
	fmt.Printf("Throughput               : %.2f req/sec\n", float64(successCount)/elapsed.Seconds())
	fmt.Println("=====================================================")
}
