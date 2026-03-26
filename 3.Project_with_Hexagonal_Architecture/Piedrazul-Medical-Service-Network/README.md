# Piedrazul Application (Hexagonal) - Implementation Specification

Status: Not implemented yet.

## Objective

Implement the application core independently from frameworks and infrastructure.

## Implementation Workflow

1. Implement domain and application core modules.
2. Define and test inbound/outbound ports.
3. Implement adapters (HTTP, persistence, external integrations).
4. Wire dependencies through inversion-of-control mechanisms.
5. Execute testing layers:
   - Core unit tests
   - Port contract tests
   - Adapter integration tests
   - End-to-end use-case tests

## Delivery Artifacts

- Deployable application package
- Adapter configuration and environment setup documentation
- Operational and maintenance runbook
