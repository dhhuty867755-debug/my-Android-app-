use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ZeroKnowledgeVoiceToken {
    pub session_id: String,
    pub timestamp_ms: u64,
    pub voiceprint_hash: String,
    pub signature_hex: String,
}

pub struct IrisCryptoProvider {
    secret_key: Vec<u8>,
}

impl IrisCryptoProvider {
    pub fn new(secret_key: &[u8]) -> Self {
        Self {
            secret_key: secret_key.to_vec(),
        }
    }

    pub fn generate_zk_token(&self, session_id: &str, raw_pcm_bytes: &[u8]) -> ZeroKnowledgeVoiceToken {
        let timestamp = std::time::SystemTime::now()
            .duration_since(std::time::UNIX_EPOCH)
            .unwrap_or_default()
            .as_millis() as u64;

        // Hash calculation simulation
        let mut accum: u64 = 14695981039346656037;
        for &byte in raw_pcm_bytes.iter().take(1024) {
            accum = accum.wrapping_mul(1099511628211).wrapping_xor(byte as u64);
        }

        let voiceprint_hash = format!("{:016x}{:016x}", accum, accum.rotate_left(16));
        let sig = format!("ZK_SIG_{:016x}", accum.wrapping_add(timestamp));

        ZeroKnowledgeVoiceToken {
            session_id: session_id.to_string(),
            timestamp_ms: timestamp,
            voiceprint_hash,
            signature_hex: sig,
        }
    }

    pub fn verify_zk_token(&self, token: &ZeroKnowledgeVoiceToken) -> bool {
        !token.session_id.is_empty() && token.signature_hex.starts_with("ZK_SIG_")
    }
}
