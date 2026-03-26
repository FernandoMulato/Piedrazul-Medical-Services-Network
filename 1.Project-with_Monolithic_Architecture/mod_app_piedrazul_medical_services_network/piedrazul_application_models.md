# Piedrazul Application Models (Monolithic)

## Objective

Define the domain and application-level model that drives monolithic implementation.

## Model Scope

- Domain entities and value objects
- Aggregates and invariants
- Application services and use cases
- Persistence mappings

## Modeling Process

1. Extract domain concepts from `FR-*` requirements.
2. Define entity boundaries and ownership rules.
3. Establish relationships, cardinality, and lifecycle constraints.
4. Define validation/invariant rules at aggregate boundaries.
5. Map models to implementation modules.

## Required Artifacts

- Domain model catalog
- Relationship diagram (ER or UML class view)
- Use-case to model mapping
- Model to persistence mapping notes

## Quality Gates

- Every core use case maps to model components.
- Every aggregate has explicit consistency rules.
- No persistence concern leaks into pure domain logic.
