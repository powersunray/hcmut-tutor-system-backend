# Enrollment & Support Needs Module

## Overview
This module manages student program enrollments, course registrations, support need declarations, and subject metadata. It is designed as a self-contained vertical slice that communicates with other modules only via shared IDs (e.g., `studentId`).

## Entities
- **Student**: student profile with academic attributes and audit fields.
- **Enrollment**: program/course enrollment with status tracking (`ACTIVE`, `COMPLETED`, `DROPPED`).
- **SupportNeed**: declared support needs linked to a `SupportType` and priority (`LOW`, `MEDIUM`, `HIGH`).
- **SupportType**: catalog of support categories.
- **Subject**: subject catalog with prerequisites.

```
Student 1---* Enrollment
Student 1---* SupportNeed *---1 SupportType
Subject (prerequisites by code)
```

## Endpoints
- `POST /api/v1/enrollments` – create an enrollment.
- `GET /api/v1/enrollments/student/{studentId}` – list enrollments.
- `PUT /api/v1/enrollments/{id}/status?status=COMPLETED` – update status.
- `DELETE /api/v1/enrollments/{id}` – cancel enrollment.
- `POST /api/v1/support-needs` – declare support need.
- `PUT /api/v1/support-needs/{id}` – update support need.
- `GET /api/v1/support-needs/student/{studentId}?priority=HIGH` – list support needs.
- `DELETE /api/v1/support-needs/{id}` – delete support need.
- `DELETE /api/v1/support-needs/archive?threshold=2024-01-01` – archive old needs.
- `POST /api/v1/subjects` – create subject.
- `GET /api/v1/subjects` – list subjects with optional filters.
- `GET /api/v1/subjects/{id}` – get subject detail.
- `PUT /api/v1/subjects/{id}` – update subject.
- `DELETE /api/v1/subjects/{id}` – delete subject.

Example:
```bash
curl -X POST http://localhost:8080/api/v1/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"programName":"CS","courseCode":"CS101","semester":"2025A"}'
```

## Setup
- Uses Spring Boot 3, JPA, and H2 (runtime) with auditing enabled.
- Ensure database credentials (if not using H2) are configured via `application.properties`.

## Testing
Run:
```bash
mvn test
```

## Assumptions
- Authentication is handled by a shared security configuration; controllers expect JWT-protected context.
- Student validation currently relies on repository lookups by `studentId`.
