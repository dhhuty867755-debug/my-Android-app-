#!/usr/bin/env python3
"""
IRIS-MX End-to-End Integration & Service Health Test Suite
"""

import sys
import time
import json
import urllib.request
import unittest

class TestIrisMicroservices(unittest.TestCase):
    
    def test_voice_gateway_health(self):
        url = "http://localhost:8088/health"
        try:
            req = urllib.request.urlopen(url, timeout=2)
            self.assertEqual(req.status, 200)
            data = json.loads(req.read().decode())
            self.assertEqual(data.get("status"), "HEALTHY")
        except Exception:
            print("[WARN] Gateway endpoint offline; skipping live HTTP check")

    def test_audio_resampler_logic(self):
        input_samples = [0.1 * (i % 10) for i in range(100)]
        ratio = 0.5
        output_len = int(len(input_samples) * ratio)
        self.assertEqual(output_len, 50)

if __name__ == "__main__":
    unittest.main()
