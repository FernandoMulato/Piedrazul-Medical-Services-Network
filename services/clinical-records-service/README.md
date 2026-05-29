# Clinical Records Service


**Version:** 1.0.0  
**Project:** Piedrazul Medical Service Network
**Authors:** Leider Ceron  
**Stack:** Java 21, Spring Boot, PostgreSQL, Maven
---
# Clinical Records Service

Microservicio encargado de la gestión de historias clínicas del sistema Piedra Azul Medical Services Network.

---

# Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Postman

---

# Arquitectura del proyecto

El proyecto implementa arquitectura por capas:

```text
Controller -> Facade -> Service -> Repository -> PostgreSQL
```

Estructura principal:

```text
src/main/java/com/piedrazul/clinicalrecords

├── controller
├── dto
├── entity
├── repository
├── service
├── facade
├── builder
├── strategy
├── observer
├── exception
```

---

# Patrones de diseño implementados

## Builder Pattern

Utilizado para construir objetos `ClinicalRecord`
de manera limpia y escalable.

Clase principal:

```text
ClinicalRecordBuilder
```

---

## Strategy Pattern

Permite aplicar validaciones dependiendo del tipo
de profesional de salud.

Clases principales:

```text
ClinicalRecordStrategy
DoctorClinicalRecordStrategy
ClinicalRecordStrategyFactory
```

---

## Observer Pattern

Permite notificar eventos cuando se crea una
historia clínica.

Clases principales:

```text
ClinicalRecordObserver
NotificationObserver
ClinicalRecordObserverManager
```

---

## Facade Pattern

Centraliza el acceso a la lógica del sistema.

Clase principal:

```text
ClinicalRecordFacade
```

---

# Configuración de la base de datos

Base de datos utilizada:

```text
PostgreSQL
```

Configuración en:

```properties
application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinical_records_db
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

---

# Ejecución del proyecto

## 1. Clonar repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

---

## 2. Entrar al proyecto

```bash
cd clinical-records-service
```

---

## 3. Ejecutar aplicación

```bash
mvn spring-boot:run
```

El microservicio correrá en:

```text
http://localhost:8084
```

---

# Endpoints REST

## Crear historia clínica

```http
POST /api/v1/clinical-records
```

Body:

```json
{
  "patientId": 1,
  "professionalId": 10,
  "professionalType": "DOCTOR",
  "diagnosis": "Migraña",
  "treatment": "Ibuprofeno",
  "observations": "Reposo",
  "consultationDate": "2026-05-26"
}
```

---

## Obtener historia clínica por ID

```http
GET /api/v1/clinical-records/{id}
```

---

## Obtener historias por paciente

```http
GET /api/v1/clinical-records/patient/{patientId}
```

---

## Actualizar historia clínica

```http
PUT /api/v1/clinical-records/{id}
```

Body:

```json
{
  "diagnosis": "Gripe",
  "treatment": "Acetaminofén",
  "observations": "Tomar líquidos"
}
```

---

## Eliminar historia clínica

```http
DELETE /api/v1/clinical-records/{id}
```

---

# Validaciones implementadas

Se implementaron validaciones utilizando:

```text
jakarta.validation
```

Ejemplos:
- @NotNull
- @NotBlank

---

# Manejo de excepciones

Se implementó manejo global de excepciones mediante:

```text
GlobalExceptionHandler
```

---

# Pruebas realizadas

Se realizaron pruebas completas en Postman:

- POST
- GET
- PUT
- DELETE

Todas funcionando correctamente.