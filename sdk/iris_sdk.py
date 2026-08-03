#!/usr/bin/env python3
"""
IRIS-MX Python SDK for Third-Party Developers
"""

import urllib.request
import json
from typing import Dict, Any

class IrisClient:
    def __init__(self, endpoint: str = "http://localhost:8088", api_key: str = None):
        self.endpoint = endpoint
        self.api_key = api_key

    def get_health(self) -> Dict[str, Any]:
        url = f"{self.endpoint}/health"
        req = urllib.request.Request(url)
        if self.api_key:
            req.add_header("Authorization", f"Bearer {self.api_key}")
        with urllib.request.urlopen(req) as resp:
            return json.loads(resp.read().decode())

    def route_intent(self, query_text: str) -> Dict[str, Any]:
        url = "http://localhost:8089/api/v1/intent/route"
        payload = json.dumps({"query_text": query_text}).encode('utf-8')
        req = urllib.request.Request(url, data=payload, headers={"Content-Type": "application/json"})
        with urllib.request.urlopen(req) as resp:
            return json.loads(resp.read().decode())

if __name__ == "__main__":
    client = IrisClient()
    print("IRIS Python SDK Client Initialized")
