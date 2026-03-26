# Microservices Requirements - Technical Specification

Status: Not implemented yet.

## Inputs

- Baseline specification: [requirements_specification.xlsx](../../1.Project-with_Monolithic_Architecture/requirements/requirements_specification.xlsx)
- Domain events and integration constraints

## Process

1. Refine requirements by bounded context.
2. Classify requirement types:
   - Service-local (`SFR-*`)
   - Cross-service (`CFR-*`)
   - Non-functional (`NFR-*`)
3. Define contract-level acceptance criteria for APIs/events.
4. Define SLO/SLA targets for latency, availability, and recovery.

## Output Artifacts

- Service-oriented requirements baseline
- Cross-service requirement matrix
- Contract acceptance criteria list
