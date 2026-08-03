# 🚀 IRIS-MX Enterprise Production Deployment Guide

## 1. Native Android Compilation (Kotlin, Java, C++ NDK)
To compile the native Android C++ library (`libiris_native_engine.so`) and Android APK:

```bash
cd android
./gradlew assembleRelease
```

---

## 2. Kubernetes Cluster Deployment
To deploy the Go Voice Gateway, Intent Router, and Telemetry Aggregator microservices:

```bash
kubectl apply -f deploy/k8s/deployment.yaml
```

---

## 3. Terraform Cloud Infrastructure Provisioning
To provision GCP L4 GPU instances for ONNX & Llama inference serving:

```bash
cd infra/terraform
terraform init
terraform apply -auto-approve
```
