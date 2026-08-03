#!/usr/bin/env python3
"""
IRIS-MX GGUF v3 & ONNX Binary Model Generator
"""

import os
import struct

def write_string_gguf(f, text: str):
    data = text.encode('utf-8')
    f.write(struct.pack('<Q', len(data)))
    f.write(data)

def generate_gguf_model(filepath: str):
    with open(filepath, 'wb') as f:
        # 1. GGUF Magic Header ("GGUF" in little endian)
        f.write(b'GGUF')
        
        # 2. GGUF Version (uint32_t = 3)
        f.write(struct.pack('<I', 3))
        
        # 3. Tensor Count (uint64_t = 16)
        f.write(struct.pack('<Q', 16))
        
        # 4. Metadata KV Count (uint64_t = 8)
        f.write(struct.pack('<Q', 8))

        # Metadata Pair 1: general.architecture (String)
        write_string_gguf(f, "general.architecture")
        f.write(struct.pack('<I', 8)) # Type 8 = STRING
        write_string_gguf(f, "llama")

        # Metadata Pair 2: general.name (String)
        write_string_gguf(f, "general.name")
        f.write(struct.pack('<I', 8))
        write_string_gguf(f, "IRIS-Llama-3-8B-Instruct-Q4_0")

        # Metadata Pair 3: llama.context_length (UInt32)
        write_string_gguf(f, "llama.context_length")
        f.write(struct.pack('<I', 4)) # Type 4 = UINT32
        f.write(struct.pack('<I', 4096))

        # Metadata Pair 4: llama.embedding_length (UInt32)
        write_string_gguf(f, "llama.embedding_length")
        f.write(struct.pack('<I', 4))
        f.write(struct.pack('<I', 4096))

        # Metadata Pair 5: llama.block_count (UInt32)
        write_string_gguf(f, "llama.block_count")
        f.write(struct.pack('<I', 4))
        f.write(struct.pack('<I', 32))

        # Metadata Pair 6: llama.feed_forward_length (UInt32)
        write_string_gguf(f, "llama.feed_forward_length")
        f.write(struct.pack('<I', 4))
        f.write(struct.pack('<I', 14336))

        # Metadata Pair 7: general.file_type (UInt32 = 2 for Q4_0)
        write_string_gguf(f, "general.file_type")
        f.write(struct.pack('<I', 4))
        f.write(struct.pack('<I', 2))

        # Metadata Pair 8: general.quantization_version (UInt32)
        write_string_gguf(f, "general.quantization_version")
        f.write(struct.pack('<I', 4))
        f.write(struct.pack('<I', 2))

        # Tensor Information Records
        tensor_names = [
            "token_embd.weight",
            "blk.0.attn_q.weight", "blk.0.attn_k.weight", "blk.0.attn_v.weight", "blk.0.attn_output.weight",
            "blk.0.ffn_gate.weight", "blk.0.ffn_up.weight", "blk.0.ffn_down.weight",
            "blk.1.attn_q.weight", "blk.1.attn_k.weight", "blk.1.attn_v.weight", "blk.1.attn_output.weight",
            "output_norm.weight", "output.weight", "blk.31.attn_q.weight", "blk.31.attn_output.weight"
        ]

        offset = 0
        for name in tensor_names:
            write_string_gguf(f, name)
            f.write(struct.pack('<I', 2)) # 2 dimensions
            f.write(struct.pack('<Q', 4096)) # dim 0
            f.write(struct.pack('<Q', 4096)) # dim 1
            f.write(struct.pack('<I', 2)) # Type 2 = Q4_0
            f.write(struct.pack('<Q', offset)) # Offset in binary buffer
            offset += 1024 * 16

        # Alignment padding to 32 bytes
        cur_pos = f.tell()
        pad = (32 - (cur_pos % 32)) % 32
        f.write(b'\x00' * pad)

        # Packed Q4_0 Quantized Binary Weights Payload (100 KB binary blob)
        # Q4_0 block structure: fp16 scale (2 bytes) + 16 packed uint8 nibble pairs (16 bytes)
        for _ in range(5000):
            scale_fp16 = struct.pack('<e', 0.035)
            nibbles = bytes([ (i * 17) & 0xFF for i in range(16) ])
            f.write(scale_fp16 + nibbles)

    print(f"[GGUF] Generated binary GGUF v3 model file: {filepath} ({os.path.getsize(filepath)} bytes)")

def generate_onnx_model(filepath: str):
    with open(filepath, 'wb') as f:
        # ONNX Protocol Buffer Binary Payload Header
        # Field 1 (ir_version = 8): 0x08, 0x08
        # Field 2 (producer_name = "IRIS-MX-Exporter"): 0x12, len, bytes
        # Field 3 (producer_version = "1.0.0"): 0x1A, len, bytes
        # Field 7 (graph): 0x3A, len, graph_bytes
        
        producer = b"IRIS-MX-Conformer-Exporter"
        version = b"1.0.0"
        graph_name = b"iris_conformer_ctc_asr"

        # Construct Protobuf payload bytes
        header = bytearray()
        header.extend([0x08, 0x08]) # ir_version = 8
        
        # Producer Name field
        header.append(0x12)
        header.append(len(producer))
        header.extend(producer)

        # Producer Version field
        header.append(0x1A)
        header.append(len(version))
        header.extend(version)

        # Graph Proto field
        graph_data = bytearray()
        graph_data.append(0x0A) # graph name tag
        graph_data.append(len(graph_name))
        graph_data.extend(graph_name)

        # Nodes (Conformer Block 1, CTC Head, LayerNorm)
        node_names = [b"ConformerEncoder_Block0", b"DepthwiseConv1D", b"LinearCTCHead", b"LayerNorm_Output"]
        for node in node_names:
            graph_data.append(0x12)
            node_body = bytearray([0x0A, len(node)]) + node
            graph_data.append(len(node_body))
            graph_data.extend(node_body)

        # Dummy quantized ONNX float weights tensor buffer
        tensor_data = bytearray([ (i * 31) & 0xFF for i in range(65536) ])
        graph_data.extend(tensor_data)

        # Package graph into model
        header.append(0x3A)
        header.extend(struct.pack('>I', len(graph_data)))
        header.extend(graph_data)

        f.write(header)

    print(f"[ONNX] Generated binary ONNX Protobuf model file: {filepath} ({os.path.getsize(filepath)} bytes)")

if __name__ == "__main__":
    os.makedirs("models", exist_ok=True)
    generate_gguf_model("models/iris_llama_3_int4.gguf")
    generate_onnx_model("models/iris_conformer_asr_v1.onnx")
