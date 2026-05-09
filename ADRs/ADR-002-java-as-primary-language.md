# ADR-002: Java as Primary Programming Language

## Status
Accepted

## Date
2026-04-07

## Context
The development team requires a robust, enterprise-ready programming language for the Medical Services Network application. The system must handle sensitive patient data with reliability and maintainability.

## Decision
Java will be used as the primary programming language for the Medical Services Network application.

## Rationale
- **Strong typing**: Compile-time error detection reduces runtime issues
- **Mature ecosystem**: Extensive libraries and frameworks for enterprise applications
- **Spring Boot**: Rapid development with proven enterprise patterns
- **Cross-platform**: Write once, run anywhere (Windows, Linux, macOS)
- **Community**: Large community support and continuous evolution
- **Team experience**: Development team has existing Java skills
- **Type safety**: Generics and strong type system prevent common bugs

## Alternatives Considered
- **Python**: Good for prototyping but less suitable for enterprise-grade applications with strict type requirements
- **JavaScript/TypeScript**: Excellent for web but lacks the same level of enterprise tooling and maturity for complex business logic
- **C#**: Solid choice but limits deployment options due to Windows-centric ecosystem
- **Go**: Good for microservices but lacks the extensive library ecosystem for rapid enterprise development

## Consequences

### Positive
- Strong type safety and compile-time checks
- Extensive enterprise frameworks (Spring, Hibernate)
- Excellent tooling (IDEA, Eclipse, VS Code)
- Large talent pool in the job market
- Mature testing frameworks (JUnit, Mockito)
- Good performance for business applications

### Negative
- More verbose syntax compared to modern languages
- Slower compilation times compared to interpreted languages
- Higher memory footprint compared to lower-level languages

### Mitigation
- Use Lombok to reduce boilerplate
- Leverage IDE features for productivity
- Modern Java features (records, sealed classes) reduce verbosity

## Applies To
- `src/**/*.java` — all application source code
- `pom.xml` — build configuration

## Out of Scope
- Specific Java version (covered in technical implementation)
- IDE choice or development environment setup

## Related
- ADR-001: Microservices Architecture
- ADR-003: Design Patterns (Factory, Observer)
- ADR-005: Spring Boot for Microservices