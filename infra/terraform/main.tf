terraform {
  required_version = ">= 1.5.0"
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.0"
    }
  }
}

provider "google" {
  project = "iris-x-ai-production"
  region  = "us-central1"
  zone    = "us-central1-a"
}

resource "google_compute_network" "iris_vpc" {
  name                    = "iris-vpc-network"
  auto_create_subnetworks = true
}

resource "google_container_cluster" "iris_gke_cluster" {
  name     = "iris-ai-gpu-cluster"
  location = "us-central1-a"

  initial_node_count = 3

  node_config {
    machine_type = "g2-standard-8"
    disk_size_gb = 200
    
    guest_accelerator {
      type  = "nvidia-l4"
      count = 1
    }

    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }

  ip_allocation_policy {}
}

output "cluster_endpoint" {
  value = google_container_cluster.iris_gke_cluster.endpoint
}
