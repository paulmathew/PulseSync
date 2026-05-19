# PulseSync

PulseSync is a production-style Android infrastructure project that simulates
offline-first mobile synchronization under unreliable network conditions.

It is not a consumer CRUD app. It is designed as an internal reliability and
observability tool for understanding how sync systems behave across retries,
failures, conflicts, queues, and network degradation.

## What PulseSync Demonstrates

- Deterministic sync state transitions
- Runtime-backed observability surfaces
- Offline-first synchronization concepts
- Retry and failure recovery modeling
- Conflict detection and resolution flows
- Network condition simulation
- Modern Android UI architecture with Compose, StateFlow, ViewModels, and Hilt