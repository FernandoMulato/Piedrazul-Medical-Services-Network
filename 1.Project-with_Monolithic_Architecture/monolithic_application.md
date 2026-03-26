# Monolithic Architecture - Technical Guide

## Scope

This guide defines the technical workflow to implement Piedrazul as a monolithic application.

## Preconditions

- Requirements baseline approved.
- Team roles and ownership model defined.
- Development environment and repository access available.

## Engineering Flow

1. [Requirements](./requirements/requirements.md)
2. [Architectural Decisions](./architectural_decisions/architectural_decisions.md)
3. [Piedrazul Application Models](./mod_app_piedrazul_medical_services_network/piedrazul_application_models.md)
4. [Piedrazul Application](./Piedrazul-Medical-Service-Network/piedrazul/README.md)

## Traceability Rule

Each stage must reference artifacts from the previous stage:

- Requirements -> ADRs
- ADRs -> Models
- Models -> Code modules and tests

## Exit Criteria

- Core use cases implemented and tested.
- Non-functional requirements validated.
- Deployment package and operational notes documented.