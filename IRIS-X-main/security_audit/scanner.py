#!/usr/bin/env python3
"""
IRIS-MX Static Code Vulnerability & Security Scanner
"""

import os
import sys
import re

class SecurityScanner:
    def __init__(self):
        self.rules = [
            (r'strcpy\(', 'Buffer Overflow Risk: Use strncpy or std::string instead'),
            (r'gets\(', 'Critical Security Risk: Unsafe gets() function'),
            (r'password\s*=\s*["\'][^"\']+["\']', 'Hardcoded Secret Risk: Found hardcoded password string')
        ]

    def scan_directory(self, root_dir: str):
        print(f"[SECURITY] Scanning codebase at '{root_dir}' for security vulnerabilities...")
        issues = 0
        for dirpath, _, filenames in os.walk(root_dir):
            if 'node_modules' in dirpath or '.git' in dirpath or 'security_audit' in dirpath:
                continue
            for fname in filenames:
                if fname.endswith(('.cpp', '.h', '.kt', '.java', '.py', '.go', '.rs')):
                    fpath = os.path.join(dirpath, fname)
                    with open(fpath, 'r', encoding='utf-8', errors='ignore') as f:
                        for idx, line in enumerate(f, 1):
                            for pattern, msg in self.rules:
                                if re.search(pattern, line):
                                    print(f"  [SECURITY ALERT] {fpath}:{idx} -> {msg}")
                                    issues += 1
        print(f"\nScan Complete: {issues} security issues flagged.")

if __name__ == "__main__":
    scanner = SecurityScanner()
    scanner.scan_directory(".")
