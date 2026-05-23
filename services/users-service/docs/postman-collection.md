# Users Service — Postman Collection

> Endpoints para probar el microservicio de usuarios.
> Base URL: `http://localhost:8081`

---

## Índice

| # | Método | Endpoint | Descripción |
|---|--------|----------|-------------|
| 1 | `POST` | `/api/users` | Crear usuario |
| 2 | `GET` | `/api/users` | Listar todos los usuarios |
| 3 | `GET` | `/api/users/{id}` | Obtener usuario por ID |
| 4 | `PUT` | `/api/users/{id}` | Actualizar usuario |
| 5 | `PATCH` | `/api/users/{id}/deactivate` | Desactivar usuario |
| 6 | `GET` | `/api/users/patients/validate/{documentNumber}` | Validar paciente por documento |
| 7 | `GET` | `/actuator/health` | Health check |

---

## 1. Crear usuario

> **`POST /api/users`**

Roles disponibles: `ADMIN`, `SCHEDULER`, `PATIENT`, `PROFESSIONAL`.

### ADMIN / SCHEDULER

```json
{
  "username": "admin1",
  "password": "Admin123!",
  "email": "admin1@medical.com",
  "role": "ADMIN"
}
```

### PATIENT

```json
{
  "username": "juanperez",
  "password": "Clave123!",
  "email": "juan@email.com",
  "role": "PATIENT",
  "firstName": "Juan",
  "lastName": "Pérez",
  "documentType": "CC",
  "documentNumber": "1234567890",
  "phone": "3001234567",
  "address": "Calle 123 #45-67",
  "eps": "Nueva EPS"
}
```

### PROFESSIONAL

```json
{
  "username": "dralopez",
  "password": "Clave123!",
  "email": "lopez@medical.com",
  "role": "PROFESSIONAL",
  "firstName": "María",
  "lastName": "López",
  "specialty": "Neuralterapia",
  "licenseNumber": "LIC-2024-001",
  "phone": "3109876543"
}
```

**Response — `201 Created`:**

```json
{
  "id": 1,
  "username": "juanperez",
  "email": "juan@email.com",
  "role": "PATIENT",
  "active": true,
  "createdAt": "2026-05-22T10:30:00",
  "updatedAt": "2026-05-22T10:30:00",
  "firstName": null,
  "lastName": null,
  "specialty": null,
  "licenseNumber": null
}
```

---

## 2. Listar todos los usuarios

> **`GET /api/users`**

**Response — `200 OK`:**

```json
[
  {
    "id": 1,
    "username": "juanperez",
    "email": "juan@email.com",
    "role": "PATIENT",
    "active": true,
    "createdAt": "2026-05-22T10:30:00",
    "updatedAt": "2026-05-22T10:30:00",
    "firstName": null,
    "lastName": null,
    "specialty": null,
    "licenseNumber": null
  }
]
```

---

## 3. Obtener usuario por ID

> **`GET /api/users/{id}`**

**Response — `200 OK`:**

```json
{
  "id": 1,
  "username": "juanperez",
  "email": "juan@email.com",
  "role": "PATIENT",
  "active": true,
  "createdAt": "2026-05-22T10:30:00",
  "updatedAt": "2026-05-22T10:30:00",
  "firstName": null,
  "lastName": null,
  "specialty": null,
  "licenseNumber": null
}
```

**Response — `400 Bad Request`** (usuario no encontrado):

```
User not found
```

---

## 4. Actualizar usuario

> **`PUT /api/users/{id}`**

Todos los campos son opcionales — solo se actualizan los que se envían.

```json
{
  "username": "juanperez2",
  "email": "juan2@email.com",
  "password": "NuevaClave1!",
  "role": "PATIENT"
}
```

**Response — `200 OK`:**

```json
{
  "id": 1,
  "username": "juanperez2",
  "email": "juan2@email.com",
  "role": "PATIENT",
  "active": true,
  "createdAt": "2026-05-22T10:30:00",
  "updatedAt": "2026-05-22T10:45:00",
  "firstName": null,
  "lastName": null,
  "specialty": null,
  "licenseNumber": null
}
```

---

## 5. Desactivar usuario

> **`PATCH /api/users/{id}/deactivate`**

**Response — `204 No Content`** (body vacío).

**Response — `400 Bad Request`:**

```
The user is already inactive
```

```
It is not possible to deactivate the last administrator of the system
```

---

## 6. Validar paciente por documento

> **`GET /api/users/patients/validate/{documentNumber}`**

Endpoint REST de respaldo. La validación asíncrona real va por RabbitMQ.

**Response — `200 OK`:**

```json
true
```

o

```json
false
```

---

## 7. Health Check

> **`GET /actuator/health`**

**Response — `200 OK`:**

```json
{
  "status": "UP"
}
```

---

## Reglas de validación

| Campo | Regla |
|-------|-------|
| `username` | 4–50 caracteres, único |
| `password` | Mínimo 8 chars, 1 mayúscula, 1 número, 1 especial |
| `email` | Formato válido, único |
| `role` | `ADMIN`, `SCHEDULER`, `PATIENT`, `PROFESSIONAL` |
| `documentNumber` | Único (para PATIENT) |
| `licenseNumber` | Único (para PROFESSIONAL) |
| Último admin activo | No se puede desactivar |

---

## Orden sugerido para pruebas en Postman

1. Crear un ADMIN → `POST /api/users`
2. Crear un PATIENT → `POST /api/users`
3. Crear un PROFESSIONAL → `POST /api/users`
4. Listar todos → `GET /api/users`
5. Obtener por ID → `GET /api/users/1`
6. Actualizar → `PUT /api/users/1`
7. Validar paciente → `GET /api/users/patients/validate/1234567890`
8. Desactivar → `PATCH /api/users/3/deactivate`
9. Health check → `GET /actuator/health`
