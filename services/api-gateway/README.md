# API Gateway

**Version:** 1.0.0  
**Project:** Piedrazul Medical Services Network  
**Authors:** Henry Fernando Mulato Llanten [henrymulato@unicauca.edu.co](mailto:henrymulato@unicauca.edu.co)  
**Stack:** Java 21, Spring Cloud Gateway 2024.0.0, Resilience4j, JWT (jjwt), Maven

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Arquitectura](#arquitectura)
- [Stack Tecnológico](#stack-tecnológico)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Ejecución](#ejecución)
- [Rutas](#rutas)
- [Flujo de Autenticación](#flujo-de-autenticación)
- [Autorización por Roles](#autorización-por-roles)
- [Circuit Breakers](#circuit-breakers)
- [Variables de Entorno](#variables-de-entorno)
- [Pruebas](#pruebas)
- [Documentación Relacionada](#documentación-relacionada)

---

## Descripción

**API Gateway** es el punto de entrada único para todos los microservicios del sistema de agendamiento de citas médicas del **Centro de Salud Piedra Azul**. Centraliza la autenticación JWT, el control de acceso por roles, el enrutamiento de peticiones y la resiliencia mediante circuit breakers.

Actúa como proxy inverso: todas las peticiones externas pasan por el gateway, que valida la identidad del usuario, verifica sus permisos y reenvía la petición al servicio correspondiente.

---

## Arquitectura

El gateway sigue los principios definidos en los ADRs del proyecto:

| ADR | Descripción |
|-----|-------------|
| [ADR-001](/ADRs/ADR-001-microservices-architecture.md) | Arquitectura de microservicios |
| [ADR-002](/ADRs/ADR-002-java-as-primary-language.md) | Java como lenguaje principal |
| [ADR-005](/ADRs/ADR-005-spring-boot-for-microservices.md) | Spring Boot para microservicios |
| [ADR-006](/ADRs/ADR-006-postgresql-as-database.md) | PostgreSQL como base de datos |

### Cadena de Filtros (Filter Chain)

Cada petición atraviesa una cadena de filtros globales en orden específico:

```
Request → CorrelationIdFilter → JwtAuthFilter → RoleAuthorizationFilter → Route → CircuitBreaker → Service
```

| Orden | Filtro | Propósito |
|-------|--------|-----------|
| 0 | `CorrelationIdFilter` | Asigna un `X-Correlation-Id` a cada request para trazabilidad entre servicios |
| 100 | `JwtAuthFilter` | Valida el token JWT en rutas protegidas; salta rutas públicas (`/auth/login`, `POST /api/users`) |
| 200 | `RoleAuthorizationFilter` | Verifica que el rol del usuario tenga permiso para acceder a la ruta solicitada |

---

## Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.14 |
| Gateway | Spring Cloud Gateway 2024.0.0 (WebFlux reactivo) |
| Seguridad | Spring Security (reactivo) + JWT (jjwt 0.12.6) |
| Circuit Breaker | Resilience4j (spring-cloud-starter-circuitbreaker-reactor-resilience4j) |
| Build | Maven |

---

## Estructura del Proyecto

```
services/api-gateway/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/medical/
        │   ├── ApiGatewayApplication.java
        │   ├── config/
        │   │   ├── JwtConfig.java          # Configuración JWT (secret, expiración)
        │   │   └── SecurityConfig.java     # Spring Security (permite todo, stateless)
        │   ├── controller/
        │   │   ├── AuthController.java     # POST /auth/login — emisión de JWT
        │   │   └── FallbackController.java # Fallbacks 503 para circuit breakers
        │   ├── dto/
        │   │   ├── LoginRequest.java
        │   │   ├── LoginResponse.java
        │   │   ├── AuthValidationRequest.java
        │   │   └── AuthValidationResponse.java
        │   ├── exception/
        │   │   └── GlobalErrorAttributes.java
        │   ├── filter/
        │   │   ├── CorrelationIdFilter.java
        │   │   ├── JwtAuthFilter.java
        │   │   └── RoleAuthorizationFilter.java
        │   └── service/
        │       ├── AuthService.java        # Orquesta login: valida credenciales + genera JWT
        │       └── JwtService.java         # Generación y validación de tokens JWT
        └── resources/
            └── application.yml            # Rutas, circuit breakers, JWT config
```

---

## Requisitos

- Java 21+
- Maven 3.9+
- Los microservicios destino corriendo en sus puertos correspondientes (ver [Rutas](#rutas))

---

## Ejecución

```bash
cd services/api-gateway
mvn spring-boot:run
```

El gateway arranca en el puerto `8080` por defecto.

> **Nota:** El gateway depende de que los servicios destino estén disponibles. Si un servicio no responde, el circuit breaker retornará `503 Service Unavailable` con un mensaje descriptivo.

---

## Rutas

El gateway expone las siguientes rutas hacia los microservicios:

| Ruta | Servicio Destino | Puerto | Métodos Soportados |
|------|------------------|--------|-------------------|
| `/api/users/**` | users-service | 8081 | GET, POST, PUT, PATCH |
| `/api/appointments/**` | appointments-service | 8082 | GET, POST, PUT, PATCH |
| `/api/professionals/**` | professionals-service | 8083 | GET |
| `/api/clinical-records/**` | clinical-records-service | 8084 | GET, POST, PUT, DELETE |
| `/api/reports/**` | reports-service | 8085 | GET |
| `/api/audits/**` | audits-service | 8086 | GET |

### Rutas del Gateway

| Ruta | Propósito | Auth |
|------|-----------|------|
| `POST /auth/login` | Emisión de JWT (autenticación) | Pública |
| `POST /api/users` | Registro de usuario | Pública |
| `/fallback/{service}` | Respuesta de fallback cuando el circuit breaker está abierto | Interna |

---

## Flujo de Autenticación

```
Cliente                     Gateway                        users-service
  │                           │                               │
  │  POST /auth/login         │                               │
  │  {username, password}     │                               │
  │──────────────────────────>│                               │
  │                           │  POST /api/users/auth/login   │
  │                           │  {username, password}         │
  │                           │──────────────────────────────>│
  │                           │                               │
  │                           │  200 {userId, username, role} │
  │                           │<──────────────────────────────│
  │                           │                               │
  │                           │  Genera JWT                   │
  │                           │  (userId, username, role)     │
  │                           │                               │
  │  200 {token, expiresIn}   │                               │
  │<──────────────────────────│                               │
```

1. El cliente envía credenciales a `POST /auth/login`
2. El gateway delega la validación a `users-service` (`POST /api/users/auth/login`)
3. Si las credenciales son válidas, el gateway genera un **JWT** firmado con HS256 que contiene: `userId`, `username`, `role`
4. El cliente debe incluir el token en el header `Authorization: Bearer <token>` en adelante

### Rutas Públicas

Las siguientes rutas NO requieren autenticación:

- `POST /auth/login` — inicio de sesión
- `POST /api/users` — auto-registro de usuarios

---

## Autorización por Roles

El `RoleAuthorizationFilter` restringe el acceso según el rol del usuario (extraído del JWT):

| Ruta | Roles Permitidos |
|------|-----------------|
| `/api/users` (GET exacto) | ADMIN |
| `/api/users/search/**` | ADMIN |
| `/api/users/patients/validate/**` | ADMIN, PROFESSIONAL |
| `/api/users/{id}` y sub-rutas | ADMIN, PROFESSIONAL |
| `/api/appointments/**` | ADMIN, SCHEDULER, PROFESSIONAL, PATIENT |
| `/api/professionals/**` | ADMIN, SCHEDULER, PROFESSIONAL, PATIENT |
| `/api/clinical-records/**` | ADMIN, PROFESSIONAL |
| `/api/reports/**` | ADMIN |
| `/api/audits/**` | ADMIN |

---

## Circuit Breakers

Cada ruta está protegida por un circuit breaker de Resilience4j con la siguiente configuración por defecto:

| Parámetro | Valor |
|-----------|-------|
| Ventana de deslizamiento | 10 llamadas |
| Umbral de fallo | 50% |
| Llamadas mínimas | 5 |
| Espera en estado abierto | 30 segundos |
| Llamadas en semi-abierto | 3 |
| Timeout | 4 segundos |

Cuando un circuit breaker se abre, la respuesta es `503 Service Unavailable` con un mensaje específico del servicio.

---

## Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `JWT_SECRET` | Clave secreta para firmar tokens JWT (mínimo 256 bits) | `default-dev-secret-key-that-is-at-least-256-bits-long` |

---

## Pruebas

El gateway incluye **39 tests unitarios** que cubren:

- `AuthServiceTest` — flujo de login exitoso, credenciales inválidas, error del servicio
- `JwtServiceTest` — generación y validación de tokens, token expirado, token malformado
- `JwtAuthFilterTest` — rutas públicas saltan auth, token válido pasa, token inválido da 401
- `RoleAuthorizationFilterTest` — acceso por rol: ADMIN, PROFESSIONAL, SCHEDULER, PATIENT, roles desconocidos
- `CorrelationIdFilterTest` — asignación de correlation id
- `GlobalErrorAttributesTest` — manejo de errores

```bash
cd services/api-gateway
mvn test
```

---

## Documentación Relacionada

- [ADR-001: Microservices Architecture](/ADRs/ADR-001-microservices-architecture.md)
- [ADR-005: Spring Boot for Microservices](/ADRs/ADR-005-spring-boot-for-microservices.md)
- [Documentación de usuarios-service](/services/users-service/README.md)
- [Documentación de appointments-service](/services/appointments-service/README.md)
- [Especificación del Sistema](/docs/specs/system.spec.md)
