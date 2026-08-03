#!/usr/bin/env python3
"""
IRIS-MX End-to-End Automated Test Runner
"""

import sys
import unittest

def run_suite():
    print("[TEST RUNNER] Executing IRIS-MX End-to-End Test Suite...")
    loader = unittest.TestLoader()
    suite = loader.discover("testing", pattern="*.py")
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    return result.wasSuccessful()

if __name__ == "__main__":
    success = run_suite()
    sys.exit(0 if success else 1)
