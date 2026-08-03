# 📡 IRIS-MX API Specification (v2.4.0)

## 1. WebSocket Streaming Endpoint
`WS /ws/voice-stream`

### Binary Frame Payload Format (Protobuf Header + PCM Data)
| Offset (Bytes) | Field Name | Type | Description |
| :--- | :--- | :--- | :--- |
| `0 - 7` | `sequence_number` | UInt64 | Monotonically increasing frame index |
| `8 - 15` | `timestamp_ns` | UInt64 | Timestamp in nanoseconds |
| `16 - 19` | `sample_rate` | UInt32 | Sample rate (e.g. 44100 Hz) |
| `20+` | `raw_pcm` | Bytes | 16-bit PCM Mono samples |

---

## 2. REST Endpoints

### Route Neural Intent
`POST /api/v1/intent/route`

#### Request Body
```json
{
  "query_text": "Show system status and GPU telemetry",
  "audio_rms": 0.42,
  "session_id": "sess_89a0bc412"
}
```

#### Response Body (200 OK)
```json
{
  "intent_name": "QUERY_SYSTEM_TELEMETRY",
  "confidence": 0.96,
  "execution_target": "C_CPP_NDK_ENGINE",
  "latency_ms": 1
}
```
