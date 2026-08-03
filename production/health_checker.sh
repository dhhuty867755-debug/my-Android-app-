#!/usr/bin/env bash
# IRIS-MX Production Microservices Health Monitor

echo "========================================================="
echo "⚡ IRIS-MX PRODUCTION CLUSTER HEALTH CHECKER"
echo "========================================================="

check_service() {
    local name=$1
    local url=$2
    echo -n "Checking $name ($url)... "
    if curl -s --max-time 2 "$url" > /dev/null; then
        echo "[OK]"
    else
        echo "[WARN - OFFLINE/UNREACHABLE]"
    fi
}

check_service "Go Voice Gateway" "http://localhost:8088/health"
check_service "Go Intent Router" "http://localhost:8089/api/v1/intent/route"
check_service "Telemetry Aggregator" "http://localhost:9090/metrics"

echo "========================================================="
